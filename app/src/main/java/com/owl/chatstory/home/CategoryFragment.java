package com.owl.chatstory.home;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseFragment;
import com.owl.chatstory.chat.ReadActivity;
import com.owl.chatstory.common.util.CommonVerticalItemDecoration;
import com.owl.chatstory.common.util.ImageLoaderUtils;
import com.owl.chatstory.common.util.network.request.FictionListRequest;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lebron on 2017/9/10.
 */

public class CategoryFragment extends BaseFragment implements CategoryContract.View
        , SwipeRefreshLayout.OnRefreshListener, LoadMoreWrapper.OnLoadMoreListener {
    public static final String KEY_CATEGORY_ID = "KEY_CATEGORY_ID";

    @BindView(R.id.category_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.category_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.empty_layout)
    View mEmptyLayout;

    private CategoryContract.Presenter mPresenter;
    private LoadMoreWrapper<FictionDetailModel> mLoadMoreWrapper;
    private List<FictionDetailModel> mDatas = new ArrayList<>();
    private FictionListRequest mRequest;
    private View mLoadMoreView;
    private int mPage = 1, mPrePage;

    public static CategoryFragment newInstance(String categoryId) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_CATEGORY_ID, categoryId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initViewsAndData(View view) {
        CommonAdapter<FictionDetailModel> mAdapter = new CommonAdapter<FictionDetailModel>(getActivity(), R.layout.story_category_item, mDatas) {
            @Override
            protected void convert(ViewHolder holder, FictionDetailModel fictionModel, int position) {
                ImageLoaderUtils.getInstance().loadImage(getActivity(), fictionModel.getCover(), (ImageView) holder.getView(R.id.category_item_cover_img), R.color.colorPrimaryDark);
                holder.setText(R.id.category_item_title_txv, fictionModel.getTitle());
                holder.setText(R.id.category_item_description_txv, fictionModel.getSummary());
                holder.setText(R.id.category_item_watchers_txv, fictionModel.getViews() + "");
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ReadActivity.start(getActivity(), mDatas.get(position).getId(), mDatas.get(position).getTitle());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mLoadMoreWrapper = new LoadMoreWrapper(mAdapter);
        mLoadMoreView = LayoutInflater.from(getContext()).inflate(R.layout.common_loading_more_layout, null);
        mLoadMoreView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        mLoadMoreWrapper.setLoadMoreView(mLoadMoreView);
        mLoadMoreWrapper.setOnLoadMoreListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new CommonVerticalItemDecoration(24));
        mRecyclerView.setAdapter(mLoadMoreWrapper);
        mRefreshLayout.setOnRefreshListener(this);

        new CategoryPresenter(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(CategoryContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.subscribe();
    }

    @Override
    public void showFictionList(List<FictionDetailModel> list, boolean refresh) {
        if (refresh) {
            if (mDatas != null) {
                mDatas.clear();
            }
        }
        if (list.size() > 0) {
            mLoadMoreView.findViewById(R.id.loading_more_layout).setVisibility(View.VISIBLE);
            mPage++;
        } else {
            mLoadMoreView.findViewById(R.id.loading_more_layout).setVisibility(View.GONE);
        }
        mDatas.addAll(list);
        mLoadMoreWrapper.notifyDataSetChanged();
    }

    @Override
    public void showRefreshing(boolean show) {
        if (show) {
            mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(true);
                }
            });
        } else {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showErrorLayout(boolean show) {
        mEmptyLayout.setVisibility(show && mDatas.size() <= 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            Log.i("Lebron", " onRefresh ");
            mPage = 1;
            mPrePage = 0;
            mPresenter.getFictionList(getRequest(mPage), true);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (mPresenter != null) {
            Log.i("Lebron", " onLoadMoreRequested " + mPage + " " + mPrePage);
            if (mPage != mPrePage) {
                mPresenter.getFictionList(getRequest(mPage), false);
                mPrePage = mPage;
            }
        }
    }

    private FictionListRequest getRequest(int page) {
        if (mRequest == null) {
            mRequest = new FictionListRequest();
        }
        mRequest.setCategoryId(getArguments().getString(KEY_CATEGORY_ID));
        mRequest.setPage(page);
        mRequest.setCount(20);
        return mRequest;
    }
}
