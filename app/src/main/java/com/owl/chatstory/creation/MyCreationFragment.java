package com.owl.chatstory.creation;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseFragment;
import com.owl.chatstory.creation.adapter.OldCreationDelegate;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lebron on 2017/10/30.
 */

public class MyCreationFragment extends BaseFragment implements MyCreationContract.View, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.mycreation_recycler)
    RecyclerView mRecyclerview;
    @BindView(R.id.empty_layout)
    View mEmptyView;
    @BindView(R.id.error_layout)
    View mErrorView;
    @BindView(R.id.mycreation_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    private MyCreationContract.Presenter mPresenter;
    private List<FictionDetailModel> mDatas = new ArrayList<>();
    private MultiItemTypeAdapter<FictionDetailModel> mAdapter;
    private String mLanguage = "english";

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_my_creations;
    }

    @Override
    protected void initViewsAndData(View view) {
        mAdapter = new MultiItemTypeAdapter<>(getActivity(), mDatas);
        mAdapter.addItemViewDelegate(new OldCreationDelegate());
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(this);

        new MyCreationPresenter(this);
    }

    @OnClick(R.id.mycreation_add_img)
    public void clickAdd() {
        BasicCreateActivity.start(getActivity(), null);
    }

    @Override
    public void showMyCreations(List<FictionDetailModel> list) {
        mErrorView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mRefreshLayout.setRefreshing(false);
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.clear();
        mDatas.addAll(list);
        if (mDatas.size() <= 0) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorView() {
        mRefreshLayout.setRefreshing(false);
        mErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(MyCreationContract.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.getUserFictionList(mLanguage);
    }

    @Override
    public void onRefresh() {
        mPresenter.getUserFictionList(mLanguage);
    }
}
