package com.owl.chatstory.chat;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseActivity;
import com.owl.chatstory.chat.adapter.ChatAsideDelegate;
import com.owl.chatstory.chat.adapter.ChatLeftDelegate;
import com.owl.chatstory.chat.adapter.ChatNextDelegate;
import com.owl.chatstory.chat.adapter.ChatRightDelegate;
import com.owl.chatstory.chat.adapter.ReadItemDecoration;
import com.owl.chatstory.common.util.DialogUtils;
import com.owl.chatstory.common.util.PreferencesHelper;
import com.owl.chatstory.common.util.ShareUtils;
import com.owl.chatstory.common.view.SpaceRecyclerView;
import com.owl.chatstory.data.chatsource.model.ChapterModel;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;
import com.owl.chatstory.data.chatsource.model.FictionStatusResponse;
import com.owl.chatstory.data.chatsource.model.MessageModel;
import com.owl.chatstory.data.eventsource.FictionEvent;
import com.owl.chatstory.data.homesource.model.ShareModel;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;

/**
 * Created by lebron on 2017/9/12.
 */

public class ReadActivity extends BaseActivity implements ReadContract.View, RewardedVideoAdListener {
    public static final String EXTRA_FICTION_ID = "EXTRA_FICTION_ID";
    public static final String EXTRA_FICTION_NAME = "EXTRA_FICTION_NAME";
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.read_recyclerview)
    SpaceRecyclerView mRecyclerView;
    @BindView(R.id.read_appbar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.read_progressbar)
    ProgressBar mProgressbar;
    @BindView(R.id.read_loading_progressbar)
    ProgressBar mLoadingBar;
    @BindView(R.id.read_click_tips_txv)
    View mClickTipsView;
    @BindView(R.id.read_loading_progressbar_layout)
    View mLoadingView;

    private ReadContract.Presenter mPresenter;
    private MultiItemTypeAdapter<MessageModel> mAdapter;
    private List<MessageModel> mDatas;
    private List<MessageModel> mShowDatas = new ArrayList<>();
    private Subscription mSubscription;
    private FictionDetailModel mFictionDetailModel;
    private String mLastChapterId;
    private int mCurrentChapterIndex;//当前阅读的章节
    private List<String> mProgressList;//阅读进度
    private MenuItem mFavoriteMenu;
    private FictionStatusResponse mFictionStatus;
    private RewardedVideoAd mRewardedVideoAd;
    private String mChapterId;
    private boolean isRewarded;

    private ChatNextDelegate.OnClickListener mNextListener = new ChatNextDelegate.OnClickListener() {
        @Override
        public void onClick() {
            stopAutoRead();
            clickNext();
        }

        @Override
        public void onShareClick(boolean shareOrUpdate) {
            if (!shareOrUpdate) {
                if (mPresenter != null) {
                    mPresenter.prayUpdate(mFictionDetailModel.getId());
                }
                return;
            }
            ShareModel shareModel = new ShareModel();
            shareModel.setContent(getString(R.string.share_content));
            if (mFictionDetailModel.getLanguage() == null || mLastChapterId == null) {
                shareModel.setUrl(ShareUtils.getShareAppUrl(ReadActivity.this));
            } else {
                shareModel.setUrl(ShareUtils.getShareChapterUrl(mFictionDetailModel.getLanguage(), mLastChapterId));
                shareModel.setImage(mFictionDetailModel.getCover());
            }
            DialogUtils.showShareDialog(ReadActivity.this, shareModel);
        }

        @Override
        public void onLongClick() {
            /*mSubscription = Observable.interval(1, TimeUnit.SECONDS).subscribe(new Subscriber<Long>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Long aLong) {
                    clickNext();
                }
            });*/
        }

        @Override
        public void onNextClick() {
            if (mCurrentChapterIndex < mProgressList.size()) {
                mProgressList.set(mCurrentChapterIndex, String.valueOf(100));
                PreferencesHelper.getInstance().setFictionProgressList(mFictionDetailModel.getId(), mProgressList);
            }
            mCurrentChapterIndex++;
            if (mCurrentChapterIndex < mFictionDetailModel.getChapters().size()) {
                String chapterId = mFictionDetailModel.getChapters().get(mCurrentChapterIndex).getChapterId();
                mPresenter.getChapterData(chapterId, mFictionDetailModel.getChapters().get(mCurrentChapterIndex).getVip(), false);
                if (mShowDatas != null) {
                    mShowDatas.clear();
                }
                mAdapter.notifyDataSetChanged();
                mLoadingView.setVisibility(View.VISIBLE);
            }
        }
    };

    public static void start(Context context, String fictionId, String fictionName) {
        Intent intent = new Intent(context, ReadActivity.class);
        intent.putExtra(EXTRA_FICTION_ID, fictionId);
        intent.putExtra(EXTRA_FICTION_NAME, fictionName);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_read;
    }

    @Override
    protected void initToolBar() {
        if (mToolbar != null) {
            mToolbar.setTitle(getIntent().getStringExtra(EXTRA_FICTION_NAME));
            setSupportActionBar(mToolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    protected void initViewsAndData() {
        mClickTipsView.setVisibility(PreferencesHelper.getInstance().getBoolean(PreferencesHelper.KEY_CLICK_TIPS_SHOWED, false) ? View.GONE : View.VISIBLE);
        mAdapter = new MultiItemTypeAdapter<>(this, mShowDatas);
        mAdapter.addItemViewDelegate(new ChatLeftDelegate());
        mAdapter.addItemViewDelegate(new ChatRightDelegate());
        mAdapter.addItemViewDelegate(new ChatAsideDelegate());
        mAdapter.addItemViewDelegate(new ChatNextDelegate(mNextListener));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new ReadItemDecoration(24));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setBlankListener(new SpaceRecyclerView.BlankListener() {
            @Override
            public void onBlankClick() {
                stopAutoRead();
                clickNext();
            }
        });

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideo();

        new ReadPresenter(this);
    }

    private void loadRewardedVideo() {
        mRewardedVideoAd.loadAd("ca-app-pub-8805953710729771/6340985560",
                new AdRequest.Builder().build());
    }

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
        stopAutoRead();
        mPresenter.unsubscribe();
    }

    @Override
    protected void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
        try {
            if (mShowDatas != null && mDatas != null) {
                int progress = (mShowDatas.size() - 1) * 100 / mDatas.size();
                if (progress < 0) {
                    progress = 0;
                }
                mProgressList.set(mCurrentChapterIndex, String.valueOf(progress));
                PreferencesHelper.getInstance().setFictionProgressList(mFictionDetailModel.getId(), mProgressList);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.read_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.read_menu_directory:
                if (mFictionDetailModel != null) {
                    DirectoryActivity.start(this, mFictionDetailModel, mFictionStatus);
                }
                break;
            case R.id.read_menu_share:
                if (mFictionDetailModel != null) {
                    ShareModel shareModel = new ShareModel();
                    shareModel.setContent(getString(R.string.share_content));
                    if (mFictionDetailModel.getLanguage() == null || mLastChapterId == null) {
                        shareModel.setUrl(ShareUtils.getShareAppUrl(this));
                    } else {
                        shareModel.setUrl(ShareUtils.getShareChapterUrl(mFictionDetailModel.getLanguage(), mLastChapterId));
                        shareModel.setImage(mFictionDetailModel.getCover());
                    }
                    DialogUtils.showShareDialog(this, shareModel);
                }
                break;
            case R.id.read_menu_favorite:
                if (PreferencesHelper.getInstance().isLogined() && mFictionDetailModel != null) {
                    if (!mFictionStatus.getCollect()) {
                        mPresenter.collectFiction(mFictionDetailModel.getId());
                        mFictionStatus.setCollect(true);
                        mFictionDetailModel.setFavorites(mFictionDetailModel.getFavorites() + 1);
                        updateFictionStatus(mFictionStatus);
                    } else {
                        mPresenter.uncollectFiction(mFictionDetailModel.getId());
                        mFictionStatus.setCollect(false);
                        mFictionDetailModel.setFavorites(mFictionDetailModel.getFavorites() - 1);
                        updateFictionStatus(mFictionStatus);
                    }
                } else {
                    Toast.makeText(this, R.string.common_login_first, Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mFavoriteMenu = menu.findItem(R.id.read_menu_favorite);
        updateFictionStatus(mFictionStatus);
        return super.onPrepareOptionsMenu(menu);
    }

    @Subscribe
    public void onEvent(ChapterModel model) {
        if (model != null) {
            if (!mLastChapterId.equals(model.getChapterId()) && mPresenter != null) {
                for (int i = 0; i < mFictionDetailModel.getChapters().size(); i++) {
                    if (mFictionDetailModel.getChapters().get(i).getChapterId().equals(model.getChapterId())) {
                        mCurrentChapterIndex = i;
                        break;
                    }
                }
                mPresenter.getChapterData(model.getChapterId(), model.getVip(), false);
                if (mShowDatas != null) {
                    mShowDatas.clear();
                }
                mProgressbar.setProgress(0);
                mAdapter.notifyDataSetChanged();
                mLoadingView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Subscribe
    public void onStatusEvent(FictionStatusResponse response) {
        if (response != null) {
            updateFictionStatus(response);
        }
    }

    @Subscribe
    public void onFictionEvent(FictionEvent event) {
        if (event != null) {
            mFictionDetailModel.setLikes(event.getLikes());
            mFictionDetailModel.setFavorites(event.getStars());
        }
    }

    @OnClick(R.id.read_click_tips_txv)
    public void clickTips() {
        mClickTipsView.setVisibility(View.GONE);
        PreferencesHelper.getInstance().setBoolean(PreferencesHelper.KEY_CLICK_TIPS_SHOWED, true);
        clickNext();
    }

    private void clickNext() {
        if (mDatas != null && mShowDatas.size() > 0) {
            try {
                int i = mShowDatas.size() - 1;
                mProgressbar.setProgress(100 * i / mDatas.size());
                if (i < mDatas.size()) {
                    mShowDatas.add(i, mDatas.get(i));
                    mAdapter.notifyItemInserted(i);
                    mRecyclerView.smoothScrollToPosition(i + 1);
                    mAppBarLayout.setExpanded(false);
                } else {
                    mShowDatas.get(i).setEnded(true);
                    if (mCurrentChapterIndex + 1 == mFictionDetailModel.getChapters().size()) {
                        mShowDatas.get(i).setLastChapter(true);
                    } else {
                        mShowDatas.get(i).setLastChapter(false);
                        mShowDatas.get(i).setNextChapterModel(mFictionDetailModel.getChapters().get(mCurrentChapterIndex + 1));
                        mShowDatas.get(i).setFictionName(mFictionDetailModel.getTitle());
                    }
                    mAdapter.notifyItemChanged(i);
                    mRecyclerView.smoothScrollToPosition(i + 1);
                    mPresenter.addToHistory(getIntent().getStringExtra(EXTRA_FICTION_ID));
                }
            } catch (Exception e) {
            }
        }
    }

    private void stopAutoRead() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void showFictionData(FictionModel model) {
        mToolbar.setTitle(mFictionDetailModel.getTitle() + " (" + model.getNum() + ")");
        mLastChapterId = model.getId();
        mLoadingView.setVisibility(View.GONE);
        mDatas = model.getList();
        if (mShowDatas != null) {
            mShowDatas.clear();
        }
        MessageModel messageModel = new MessageModel();
        messageModel.setLocation("end");
        int progress = mDatas.size() * Integer.valueOf(mProgressList.get(mCurrentChapterIndex)) / 100;
        mProgressbar.setProgress(progress);
        mShowDatas.addAll(mDatas.subList(0, progress > 0 ? progress : 1));
        mShowDatas.add(messageModel);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFictionDetailData(FictionDetailModel model) {
        mFictionDetailModel = model;
        mProgressList = PreferencesHelper.getInstance().getFictionProgressList(mFictionDetailModel.getId(), mFictionDetailModel.getChapters().size());
        mCurrentChapterIndex = 0;
    }

    @Override
    public void updateFictionStatus(FictionStatusResponse response) {
        if (response == null) {
            return;
        }
        mFictionStatus = response;
        if (mFavoriteMenu != null) {
            mFavoriteMenu.setIcon(mFictionStatus.getCollect() ? R.drawable.vector_stared : R.drawable.vector_unstar);
        }
    }

    @Override
    public void showWaittingDialog(final String chapterId) {
        DialogUtils.showWaittingDialog(this, new DialogUtils.OnWaittingDialogClickListener() {
            @Override
            public void onOK() {
                mPresenter.getChapterData(chapterId, 0, true);
            }

            @Override
            public void onCancel() {
                finish();
            }

            @Override
            public void watch() {
                mChapterId = chapterId;
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }
            }
        }, mRewardedVideoAd.isLoaded());
    }

    @Override
    public void setPresenter(ReadContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.getFictionData(getIntent().getStringExtra(EXTRA_FICTION_ID));
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Log.i("Lebron", "onRewardedVideoAdLoaded");
        isRewarded = false;
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {
        Log.i("Lebron", "onRewardedVideoStarted");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Log.i("Lebron", "onRewardedVideoAdClosed");
        loadRewardedVideo();
        if (!isRewarded) {
            finish();
        }
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Log.i("Lebron", "onRewarded");
        isRewarded = true;
        mPresenter.getChapterData(mChapterId, 0, true);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Log.i("Lebron", "onRewardedVideoAdFailedToLoad");
    }
}
