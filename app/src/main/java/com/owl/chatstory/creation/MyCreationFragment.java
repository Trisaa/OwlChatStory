package com.owl.chatstory.creation;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.owl.chatstory.R;
import com.owl.chatstory.base.BaseFragment;
import com.owl.chatstory.common.util.Constants;
import com.owl.chatstory.common.util.ImageLoaderUtils;
import com.owl.chatstory.common.util.PreferencesHelper;
import com.owl.chatstory.common.util.TimeUtils;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.user.login.LoginActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.Subscribe;

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
    @BindView(R.id.error_btn)
    TextView mErrorBtn;
    @BindView(R.id.error_txv)
    TextView mErrorTextView;

    private MyCreationContract.Presenter mPresenter;
    private List<FictionDetailModel> mDatas = new ArrayList<>();
    private CommonAdapter<FictionDetailModel> mAdapter;
    private String mLanguage = "english";

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_my_creations;
    }

    @Override
    protected void initViewsAndData(View view) {
        mAdapter = new CommonAdapter<FictionDetailModel>(getActivity(), R.layout.my_creation_item, mDatas) {
            @Override
            protected void convert(ViewHolder holder, FictionDetailModel model, int position) {
                holder.setText(R.id.my_creation_title_txv, model.getTitle());
                holder.setText(R.id.my_creation_time_txv, TimeUtils.getTimeFormat(model.getCreateLine()));
                holder.setText(R.id.my_creation_chapters_txv, model.getSummary());
                holder.setText(R.id.my_creation_texts_txv, Constants.getStatus(model.getStatus()));
                ImageLoaderUtils.getInstance().loadImage(holder.getConvertView().getContext(), model.getCover(), (ImageView) holder.getView(R.id.my_creation_img), R.color.colorPrimaryDark);
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                CreationDetailActivity.start(getActivity(), mDatas.get(position).getId(), mLanguage);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(this);

        new MyCreationPresenter(this);
    }

    @OnClick(R.id.mycreation_add_img)
    public void clickAdd() {
        if (PreferencesHelper.getInstance().isLogined()) {
            BasicCreateActivity.start(getActivity(), null);
        } else {
            Toast.makeText(getActivity(), "您还未登录", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.error_btn)
    public void clickErrorBtn() {
        if (PreferencesHelper.getInstance().isLogined()) {
            mPresenter.getUserFictionList(mLanguage);
        } else {
            LoginActivity.start(getActivity());
        }
    }

    @Subscribe
    public void onEvent(String event) {
        if (event != null && event.equals(Constants.RELOAD_DATA_AFTER_LOGINED)) {
            mPresenter.getUserFictionList(mLanguage);
        }
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
        if (PreferencesHelper.getInstance().isLogined()) {
            mErrorTextView.setText(R.string.common_network_error);
            mErrorBtn.setText("重新加载");
        } else {
            mErrorTextView.setText("登录后获取更多");
            mErrorBtn.setText("登录");
        }
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
