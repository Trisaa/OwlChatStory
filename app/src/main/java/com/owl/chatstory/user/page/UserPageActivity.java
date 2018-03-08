package com.owl.chatstory.user.page;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseActivity;
import com.owl.chatstory.chat.DirectoryActivity;
import com.owl.chatstory.chat.ReadActivity;
import com.owl.chatstory.common.util.CommonVerticalItemDecoration;
import com.owl.chatstory.common.util.ImageLoaderUtils;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.usersource.model.UserPageModel;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by lebron on 2018/2/26.
 */

public class UserPageActivity extends BaseActivity implements UserPageContract.View, LoadMoreWrapper.OnLoadMoreListener {
    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";
    @BindView(R.id.common_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.userpage_recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.common_blur_img)
    ImageView mBlurView;
    @BindView(R.id.userpage_user_icon)
    ImageView mUserIcon;
    @BindView(R.id.userpage_user_name)
    TextView mNameView;
    @BindView(R.id.userpage_user_describe)
    TextView mDescribeView;
    @BindView(R.id.userpage_watches_txv)
    TextView mWatchesView;
    @BindView(R.id.userpage_likes_txv)
    TextView mLikesView;
    @BindView(R.id.userpage_favorites_txv)
    TextView mCollectsView;
    @BindView(R.id.empty_layout)
    View mEmptyView;
    @BindView(R.id.userpage_appbar_layout)
    AppBarLayout mAppBarLayout;

    private UserPageContract.Presenter mPresenter;
    private List<FictionDetailModel> mDatas = new ArrayList<>();
    private LoadMoreWrapper<FictionDetailModel> mAdapter;
    private View mLoadMoreView;
    private String mUserId;
    private int mPage = 2, mPrePage;

    public static void start(Context context) {
        Intent intent = new Intent(context, UserPageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_userpage;
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
    protected void initViewsAndData() {
        mUserId = getIntent().getStringExtra(EXTRA_USER_ID);
        CommonAdapter<FictionDetailModel> adapter = new CommonAdapter<FictionDetailModel>(this, R.layout.story_category_item, mDatas) {
            @Override
            protected void convert(ViewHolder holder, FictionDetailModel fictionModel, int position) {
                ImageLoaderUtils.getInstance().loadImage(UserPageActivity.this, fictionModel.getCover(), (ImageView) holder.getView(R.id.category_item_cover_img), R.color.colorPrimaryDark);
                holder.setText(R.id.category_item_title_txv, fictionModel.getTitle());
                holder.setText(R.id.category_item_description_txv, fictionModel.getSummary());
                holder.setText(R.id.category_item_watchers_txv, fictionModel.getViews() + "");
                holder.setText(R.id.category_upinfo_txv, getString(R.string.common_update_chapter, fictionModel.getUpinfo()));
                holder.setVisible(R.id.category_item_vip_img, fictionModel.getVip() == 0 ? false : true);
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position < mDatas.size()) {
                    DirectoryActivity.start(UserPageActivity.this, mDatas.get(position).getId());
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mAdapter = new LoadMoreWrapper<>(adapter);
        mLoadMoreView = LayoutInflater.from(this).inflate(R.layout.common_loading_more_layout, null);
        mLoadMoreView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        mAdapter.setLoadMoreView(mLoadMoreView);
        mAdapter.setOnLoadMoreListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new CommonVerticalItemDecoration(32));
        mRecyclerView.setAdapter(mAdapter);
        new UserPagePresenter(this);
    }

    @Override
    public void setPresenter(UserPageContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.getUserPageInfo(mUserId);
    }

    @Override
    public void showUserPageInfo(UserPageModel model) {
        if (model != null) {
            Glide.with(this)
                    .load(model.getIcon())
                    .crossFade(1000)
                    .bitmapTransform(new BlurTransformation(this, 14, 1))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                    .into(mBlurView);
            ImageLoaderUtils.getInstance().loadCircleImage(this, model.getIcon(), mUserIcon, R.mipmap.user_default_icon);
            mNameView.setText(model.getName());
            mDescribeView.setText(model.getName());
            mLikesView.setText(String.valueOf(model.getLikes()));
            mWatchesView.setText(String.valueOf(model.getWatches()));
            mCollectsView.setText(String.valueOf(model.getFavorites()));

            if (model.getList() != null && model.getList().size() > 0) {
                mDatas.clear();
                mDatas.addAll(model.getList());
                mAdapter.notifyDataSetChanged();
                mEmptyView.setVisibility(View.GONE);
            } else {
                mEmptyView.setVisibility(View.VISIBLE);
            }

        } else {
            Toast.makeText(this, R.string.common_network_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showFictionList(List<FictionDetailModel> list) {
        if (list.size() > 0) {
            mLoadMoreView.findViewById(R.id.loading_more_layout).setVisibility(View.VISIBLE);
            mPage++;
        } else {
            mLoadMoreView.findViewById(R.id.loading_more_layout).setVisibility(View.GONE);
        }
        mDatas.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMoreRequested() {
        if (mPresenter != null) {
            if (mPage != mPrePage) {
                mPresenter.getUserRelatedFictionList(mUserId, mPage);
                mPrePage = mPage;
            }
        }
    }
}
