package com.owl.chatstory.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.CallbackManager;
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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lebron on 2017/9/12.
 */

public class ReadActivity extends BaseActivity implements ReadContract.View, RewardedVideoAdListener {
    public static final String EXTRA_CHAPTER_MODEL = "EXTRA_CHAPTER_MODEL";
    public static final String EXTRA_FICTION_MODEL = "EXTRA_FICTION_MODEL";
    public static final String EXTRA_FICTION_STATUS = "EXTRA_FICTION_STATUS";
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
    private CallbackManager mCallbackManager;
    private MultiItemTypeAdapter<MessageModel> mAdapter;
    private List<MessageModel> mDatas;
    private List<MessageModel> mShowDatas = new ArrayList<>();
    private MenuItem mFavoriteMenu, mStarMenu;
    private FictionStatusResponse mFictionStatus;
    private FictionDetailModel mFictionDetailModel;
    private RewardedVideoAd mRewardedVideoAd;
    private String mChapterId;
    private boolean isRewarded, isHistoryAdded;
    private ChapterModel mChapterModel;
    private FictionModel mFictionModel;

    private ChatNextDelegate.OnClickListener mNextListener = new ChatNextDelegate.OnClickListener() {
        @Override
        public void onClick() {
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
            if (mFictionModel == null) {
                shareModel.setUrl(ShareUtils.getShareAppUrl(ReadActivity.this));
            } else {
                shareModel.setUrl(ShareUtils.getShareChapterUrl(mFictionModel.getLanguage(), mFictionModel.getId()));
                shareModel.setImage(mFictionDetailModel.getCover());
            }
            DialogUtils.showShareDialog(ReadActivity.this, shareModel);
        }

        @Override
        public void onLongClick() {
        }

        @Override
        public void onNextClick(ChapterModel chapterModel) {
            if (chapterModel != null) {
                mPresenter.getChapterData(chapterModel.getChapterId(), chapterModel.getVip(), false);
                if (mShowDatas != null) {
                    mShowDatas.clear();
                }
                mAdapter.notifyDataSetChanged();
                mLoadingView.setVisibility(View.VISIBLE);
            }
        }
    };

    public static void start(Context context, ChapterModel model, FictionDetailModel fictionDetailModel, FictionStatusResponse response) {
        Intent intent = new Intent(context, ReadActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CHAPTER_MODEL, model);
        bundle.putParcelable(EXTRA_FICTION_MODEL, fictionDetailModel);
        bundle.putParcelable(EXTRA_FICTION_STATUS, response);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_read;
    }

    @Override
    protected void initToolBar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    protected void initViewsAndData() {
        mCallbackManager = CallbackManager.Factory.create();
        mChapterModel = getIntent().getParcelableExtra(EXTRA_CHAPTER_MODEL);
        mFictionDetailModel = getIntent().getParcelableExtra(EXTRA_FICTION_MODEL);
        mFictionStatus = getIntent().getParcelableExtra(EXTRA_FICTION_STATUS);

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
                clickNext();
            }
        });

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideo();

        new ReadPresenter(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
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
        mPresenter.unsubscribe();
    }

    @Override
    protected void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.read_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.read_menu_favorite:
                if (PreferencesHelper.getInstance().isLogined() && mFictionDetailModel != null) {
                    if (!mFictionStatus.getLiked()) {
                        mPresenter.likeFiction(1, mFictionDetailModel.getId());
                        mFictionStatus.setLiked(true);
                        mFictionDetailModel.setLikes(mFictionDetailModel.getLikes() + 1);
                        updateFictionStatus(mFictionStatus);
                    } else {
                        mPresenter.likeFiction(0, mFictionDetailModel.getId());
                        mFictionStatus.setLiked(false);
                        mFictionDetailModel.setLikes(mFictionDetailModel.getLikes() - 1);
                        updateFictionStatus(mFictionStatus);
                    }
                    EventBus.getDefault().post(new FictionEvent(mFictionDetailModel.getLikes(), mFictionDetailModel.getFavorites(), mFictionStatus));
                } else {
                    Toast.makeText(this, R.string.common_login_first, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.read_menu_share:
                if (mFictionDetailModel != null) {
                    ShareModel shareModel = new ShareModel();
                    shareModel.setContent(getString(R.string.share_content));
                    if (mFictionModel == null) {
                        shareModel.setUrl(ShareUtils.getShareAppUrl(this));
                    } else {
                        shareModel.setUrl(ShareUtils.getShareChapterUrl(mFictionModel.getLanguage(), mFictionModel.getId()));
                        shareModel.setImage(mFictionDetailModel.getCover());
                    }
                    DialogUtils.showShareDialog(this, shareModel);
                }
                break;
            case R.id.read_menu_star:
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
                    EventBus.getDefault().post(new FictionEvent(mFictionDetailModel.getLikes(), mFictionDetailModel.getFavorites(), mFictionStatus));
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
        mStarMenu = menu.findItem(R.id.read_menu_star);
        updateFictionStatus(mFictionStatus);
        return super.onPrepareOptionsMenu(menu);
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
                    if (mFictionModel.getNum() == mFictionDetailModel.getUpinfo()) {
                        mShowDatas.get(i).setLastChapter(true);
                    } else {
                        mShowDatas.get(i).setLastChapter(false);
                        ChapterModel chapterModel = getNextChapter();
                        if (chapterModel != null) {
                            mShowDatas.get(i).setNextChapterModel(chapterModel);
                            mShowDatas.get(i).setFictionName(mFictionDetailModel.getTitle());
                        }
                    }
                    mAdapter.notifyItemChanged(i);
                    mRecyclerView.smoothScrollToPosition(i + 1);
                    if (!isHistoryAdded) {
                        mPresenter.addToHistory(mFictionDetailModel.getId());
                        isHistoryAdded = true;
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    /**
     * 找出当前章节的下一章信息
     */
    private ChapterModel getNextChapter() {
        if (mFictionDetailModel != null && mFictionModel != null) {
            List<ChapterModel> list = mFictionDetailModel.getChapters();
            for (int i = 0; i < list.size(); i++) {
                if (mFictionModel.getId().equals(list.get(i).getChapterId())) {
                    if (i + 1 < list.size()) {
                        return list.get(i + 1);
                    } else {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void showFictionData(FictionModel model) {
        mFictionModel = model;
        mToolbar.setTitle(TextUtils.isEmpty(model.getName()) ? mFictionDetailModel.getTitle() : model.getName());
        mLoadingView.setVisibility(View.GONE);

        mDatas = model.getList();
        if (mShowDatas != null) {
            mShowDatas.clear();
        }
        MessageModel messageModel = new MessageModel();
        messageModel.setLocation("end");
        int progress = 0 / 100;
        mProgressbar.setProgress(progress);
        mShowDatas.addAll(mDatas.subList(0, progress > 0 ? progress : 1));
        mShowDatas.add(messageModel);
        mAdapter.notifyDataSetChanged();
    }

    private void updateFictionStatus(FictionStatusResponse response) {
        if (response == null) {
            return;
        }
        mFictionStatus = response;
        if (mStarMenu != null) {
            mStarMenu.setIcon(mFictionStatus.getCollect() ? R.drawable.vector_stared : R.drawable.vector_unstar);
        }
        if (mFavoriteMenu != null) {
            mFavoriteMenu.setIcon(mFictionStatus.getLiked() ? R.drawable.vector_favorited : R.drawable.vector_unfavorite);
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
        }, mRewardedVideoAd.isLoaded(), mCallbackManager);
    }

    @Override
    public void setPresenter(ReadContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.getChapterData(mChapterModel.getChapterId(), mChapterModel.getVip(), false);
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
