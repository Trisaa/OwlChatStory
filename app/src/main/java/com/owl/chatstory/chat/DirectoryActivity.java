package com.owl.chatstory.chat;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseActivity;
import com.owl.chatstory.common.util.DialogUtils;
import com.owl.chatstory.common.util.ImageLoaderUtils;
import com.owl.chatstory.common.util.PreferencesHelper;
import com.owl.chatstory.common.util.ShareUtils;
import com.owl.chatstory.common.util.TimeUtils;
import com.owl.chatstory.data.chatsource.model.ChapterModel;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionStatusResponse;
import com.owl.chatstory.data.chatsource.model.ProgressModel;
import com.owl.chatstory.data.eventsource.FictionEvent;
import com.owl.chatstory.data.homesource.model.ShareModel;
import com.owl.chatstory.user.page.UserPageActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.owl.chatstory.chat.ReadActivity.EVENT_UPDATE_PROGRESS;

/**
 * Created by lebron on 2017/10/30.
 */

public class DirectoryActivity extends BaseActivity implements DirectoryContract.View, LoadMoreWrapper.OnLoadMoreListener {
    public static final String EXTRA_FICTION_ID = "EXTRA_FICTION_ID";
    private static final int COLLAPSE_STATE_EXPANDED = 0;
    private static final int COLLAPSE_STATE_INTERNEDIATE = 1;
    private static final int COLLAPSE_STATE_COLLAPSED = 2;
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.directory_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.directory_fiction_cover)
    ImageView mCoverImage;
    @BindView(R.id.chapter_header_title_txv)
    TextView mTitleView;
    @BindView(R.id.chapter_header_tags_txv)
    TextView mTagsView;
    @BindView(R.id.chapter_header_toolbar_title_txv)
    TextView mToolbarTitleView;
    @BindView(R.id.userpage_appbar_layout)
    AppBarLayout mAppBarLayout;

    private DirectoryContract.Presenter mPresenter;
    private LoadMoreWrapper<FictionDetailModel> mAdapter;
    private View mLoadMoreView;
    private int mPage = 2, mPrePage;
    private List<ChapterModel> mDatas = new ArrayList<>();
    private List<ProgressModel> mProgressList;
    private FictionStatusResponse mFictionStatusResponse;
    private FictionDetailModel fictionDetailModel;
    private int mCollapsingState = COLLAPSE_STATE_EXPANDED;
    private View mHeaderView;
    private String mFictionId;

    private AppBarLayout.OnOffsetChangedListener onOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (verticalOffset == 0) {
                if (mCollapsingState != COLLAPSE_STATE_EXPANDED) {
                    mCollapsingState = COLLAPSE_STATE_EXPANDED;
                    mToolbarTitleView.animate().alpha(0f);
                }
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                if (mCollapsingState != COLLAPSE_STATE_COLLAPSED) {
                    mToolbarTitleView.animate().alpha(1f).setDuration(200);
                    mToolbarTitleView.setVisibility(View.VISIBLE);
                    mCollapsingState = COLLAPSE_STATE_COLLAPSED;//修改状态标记为折叠
                }
            } else {
                if (mCollapsingState != COLLAPSE_STATE_INTERNEDIATE) {
                    if (mCollapsingState == COLLAPSE_STATE_COLLAPSED) {
                        mToolbarTitleView.animate().alpha(0f);//由折叠变为中间状态
                    }
                    mCollapsingState = COLLAPSE_STATE_INTERNEDIATE;//修改状态标记为中间
                }
            }
        }
    };

    public static void start(Context context, String fictionId) {
        Intent intent = new Intent(context, DirectoryActivity.class);
        intent.putExtra(EXTRA_FICTION_ID, fictionId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_directory;
    }

    @Override
    protected void initToolBar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setDisplayShowTitleEnabled(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                ShareModel shareModel = new ShareModel();
                shareModel.setContent(getString(R.string.share_content));
                shareModel.setUrl(ShareUtils.getShareAppUrl(this));
                DialogUtils.showShareDialog(this, shareModel);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initViewsAndData() {
        mFictionId = getIntent().getStringExtra(EXTRA_FICTION_ID);
        CommonAdapter<ChapterModel> adapter = new CommonAdapter<ChapterModel>(this, R.layout.directory_chapter_item, mDatas) {
            @Override
            protected void convert(ViewHolder holder, final ChapterModel chapterModel, int position) {
                try {
                    holder.setText(R.id.chapter_item_title_txv, getString(R.string.chapter_num, chapterModel.getNum(), chapterModel.getChapterName()));
                    holder.setText(R.id.chapter_item_time_txv, TimeUtils.getTimeFormat(chapterModel.getCreateTime()));
                    holder.setVisible(R.id.chapter_item_vip_img, chapterModel.getVip() == 0 ? false : true);
                    holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ReadActivity.start(DirectoryActivity.this, chapterModel, fictionDetailModel, mFictionStatusResponse);
                        }
                    });
                    holder.setText(R.id.chapter_item_progress_txv, chapterModel.getProgress() + "%");
                } catch (Exception e) {
                }
            }
        };

        HeaderAndFooterWrapper<ChapterModel> headerAndFooterWrapper = new HeaderAndFooterWrapper<>(adapter);
        mHeaderView = getLayoutInflater().inflate(R.layout.directory_chapter_headerview, null);
        headerAndFooterWrapper.addHeaderView(mHeaderView);
        mAdapter = new LoadMoreWrapper<>(headerAndFooterWrapper);
        mLoadMoreView = LayoutInflater.from(this).inflate(R.layout.common_loading_more_layout, null);
        mLoadMoreView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        mAdapter.setLoadMoreView(mLoadMoreView);
        mAdapter.setOnLoadMoreListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAppBarLayout.addOnOffsetChangedListener(onOffsetChangedListener);
        new DirectoryPresenter(this);
    }

    private void setHeaderView() {
        mHeaderView.findViewById(R.id.chapter_header_author_layout).setVisibility(View.VISIBLE);
        mHeaderView.findViewById(R.id.chapter_header_author_divider).setVisibility(View.VISIBLE);
        mHeaderView.findViewById(R.id.chapter_header_status_layout).setVisibility(View.VISIBLE);
        final ImageView icon = mHeaderView.findViewById(R.id.chapter_header_author_icon);
        TextView mWatchesView = mHeaderView.findViewById(R.id.chapter_header_watches_txv);
        final TextView mLikesView = mHeaderView.findViewById(R.id.chapter_header_likes_txv);
        final TextView mStarsView = mHeaderView.findViewById(R.id.chapter_header_collects_txv);

        ImageLoaderUtils.getInstance().loadCircleImage(this, fictionDetailModel.getWriter().getIcon(), icon, R.mipmap.user_default_icon);
        String author = TextUtils.isEmpty(fictionDetailModel.getWriter().getName()) ? getString(R.string.app_name) : fictionDetailModel.getWriter().getName();
        ((TextView) mHeaderView.findViewById(R.id.chapter_header_author_txv)).setText(author);
        ((TextView) mHeaderView.findViewById(R.id.chapter_header_describe_txv)).setText(fictionDetailModel.getSummary());
        ((TextView) mHeaderView.findViewById(R.id.chapter_header_chapters_txv)).setText(getString(R.string.common_update_chapter, fictionDetailModel.getUpinfo()));
        mWatchesView.setText(getString(R.string.common_views_format, fictionDetailModel.getViews()));
        mLikesView.setText(getString(R.string.common_likes_format, fictionDetailModel.getLikes()));
        mStarsView.setText(getString(R.string.common_stars_format, fictionDetailModel.getFavorites()));

        mHeaderView.findViewById(R.id.chapter_header_author_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DirectoryActivity.this, UserPageActivity.class);
                intent.putExtra(UserPageActivity.EXTRA_USER_ID, fictionDetailModel.getWriter().getId());
                ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(DirectoryActivity.this, icon, getString(R.string.share_user_icon));
                startActivity(intent, transitionActivityOptions.toBundle());
            }
        });
    }

    void setHeaderViewStatus() {
        final ImageView mLikeImage = mHeaderView.findViewById(R.id.chapter_header_likes_img);
        final ImageView mStarImage = mHeaderView.findViewById(R.id.chapter_header_collects_img);
        final TextView mLikesView = mHeaderView.findViewById(R.id.chapter_header_likes_txv);
        final TextView mStarsView = mHeaderView.findViewById(R.id.chapter_header_collects_txv);
        updateFictionStatus(mStarImage, mLikeImage, mFictionStatusResponse);
        mHeaderView.findViewById(R.id.chapter_header_collects_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PreferencesHelper.getInstance().isLogined() && mFictionStatusResponse != null) {
                    if (!mFictionStatusResponse.getCollect()) {
                        mPresenter.collectFiction(DirectoryContract.COLLECT_FICTION, fictionDetailModel.getId());
                        mFictionStatusResponse.setCollect(true);
                        updateFictionStatus(mStarImage, mLikeImage, mFictionStatusResponse);
                        fictionDetailModel.setFavorites(fictionDetailModel.getFavorites() + 1);
                        mStarsView.setText(getString(R.string.common_stars_format, fictionDetailModel.getFavorites()));
                    } else {
                        mPresenter.collectFiction(DirectoryContract.UNCOLLECT_FICTION, fictionDetailModel.getId());
                        mFictionStatusResponse.setCollect(false);
                        updateFictionStatus(mStarImage, mLikeImage, mFictionStatusResponse);
                        fictionDetailModel.setFavorites(fictionDetailModel.getFavorites() - 1 > 0 ? fictionDetailModel.getFavorites() - 1 : 0);
                        mStarsView.setText(getString(R.string.common_stars_format, fictionDetailModel.getFavorites()));
                    }
                } else {
                    Toast.makeText(DirectoryActivity.this, R.string.common_login_first, Toast.LENGTH_SHORT).show();
                }
            }
        });
        mHeaderView.findViewById(R.id.chapter_header_likes_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PreferencesHelper.getInstance().isLogined() && mFictionStatusResponse != null) {
                    if (!mFictionStatusResponse.getLiked()) {
                        mPresenter.likeFiction(DirectoryContract.LIKE_FICTION, fictionDetailModel.getId());
                        mFictionStatusResponse.setLiked(true);
                        updateFictionStatus(mStarImage, mLikeImage, mFictionStatusResponse);
                        fictionDetailModel.setLikes(fictionDetailModel.getLikes() + 1);
                        mLikesView.setText(getString(R.string.common_likes_format, fictionDetailModel.getLikes()));
                    } else {
                        mPresenter.likeFiction(DirectoryContract.DISLIKE_FICTION, fictionDetailModel.getId());
                        mFictionStatusResponse.setLiked(false);
                        updateFictionStatus(mStarImage, mLikeImage, mFictionStatusResponse);
                        fictionDetailModel.setLikes(fictionDetailModel.getLikes() - 1 > 0 ? fictionDetailModel.getLikes() - 1 : 0);
                        mLikesView.setText(getString(R.string.common_likes_format, fictionDetailModel.getLikes()));
                    }
                } else {
                    Toast.makeText(DirectoryActivity.this, R.string.common_login_first, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateFictionStatus(ImageView view1, ImageView view2, FictionStatusResponse response) {
        if (response != null) {
            view1.setBackgroundResource(response.getCollect() ? R.drawable.vector_stared : R.drawable.vector_unstar);
            view2.setBackgroundResource(response.getLiked() ? R.drawable.vector_favorited : R.drawable.vector_unfavorite);
        }
    }

    private void updateProgressData() {
        mProgressList = PreferencesHelper.getInstance().getFictionProgressList(mFictionId);
        for (ChapterModel chapterModel : mDatas) {
            for (ProgressModel progressModel : mProgressList) {
                if (chapterModel.getChapterId().equals(progressModel.getChapterId())) {
                    chapterModel.setProgress(progressModel.getProgress());
                    break;
                }
            }
        }
    }

    @Subscribe
    public void onFictionEvent(FictionEvent event) {
        if (event != null && mHeaderView != null) {
            mFictionStatusResponse = event.getResponse();
            fictionDetailModel.setLikes(event.getLikes());
            fictionDetailModel.setFavorites(event.getStars());
            ImageView mLikeImage = mHeaderView.findViewById(R.id.chapter_header_likes_img);
            ImageView mStarImage = mHeaderView.findViewById(R.id.chapter_header_collects_img);
            TextView mLikesView = mHeaderView.findViewById(R.id.chapter_header_likes_txv);
            TextView mStarsView = mHeaderView.findViewById(R.id.chapter_header_collects_txv);

            updateFictionStatus(mStarImage, mLikeImage, mFictionStatusResponse);
            mLikesView.setText(getString(R.string.common_likes_format, fictionDetailModel.getLikes()));
            mStarsView.setText(getString(R.string.common_stars_format, fictionDetailModel.getFavorites()));
        }
    }

    @Subscribe
    public void onProgresEvent(String event) {
        if (EVENT_UPDATE_PROGRESS.equals(event)) {
            updateProgressData();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setPresenter(DirectoryContract.Presenter presenter) {
        mPresenter = presenter;
        if (!TextUtils.isEmpty(mFictionId)) {
            mPresenter.getFictionDetail(mFictionId);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (mPresenter != null && fictionDetailModel != null) {
            if (mPage != mPrePage) {
                mPresenter.getChapterList(mFictionId, mPage);
                mPrePage = mPage;
            }
        }
    }

    @Override
    public void showChapterList(List<ChapterModel> list) {
        if (list.size() > 0) {
            mLoadMoreView.findViewById(R.id.loading_more_layout).setVisibility(View.VISIBLE);
            mPage++;
        } else {
            mLoadMoreView.findViewById(R.id.loading_more_layout).setVisibility(View.GONE);
        }
        mDatas.addAll(list);
        fictionDetailModel.setChapters(mDatas);
        updateProgressData();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFictionDetail(FictionDetailModel model) {
        fictionDetailModel = model;
        if (fictionDetailModel != null) {
            setHeaderView();
            mTitleView.setText(fictionDetailModel.getTitle());
            mToolbarTitleView.setText(fictionDetailModel.getTitle());
            mTagsView.setText(fictionDetailModel.getTags().get(0));
            ImageLoaderUtils.getInstance().loadImage(this, fictionDetailModel.getCover(), mCoverImage, R.color.colorPrimaryDark);
            if (mDatas == null) {
                mDatas = new ArrayList<>();
            }
            mDatas.clear();
            mDatas.addAll(fictionDetailModel.getChapters());
            updateProgressData();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showFictionStatus(FictionStatusResponse response) {
        mFictionStatusResponse = response;
        setHeaderViewStatus();
    }
}
