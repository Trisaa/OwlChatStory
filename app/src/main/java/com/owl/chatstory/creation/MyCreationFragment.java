package com.owl.chatstory.creation;

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

public class MyCreationFragment extends BaseFragment implements MyCreationContract.View {
    @BindView(R.id.mycreation_recycler)
    RecyclerView mRecyclerview;

    private List<FictionDetailModel> mDatas = new ArrayList<>();
    private MultiItemTypeAdapter<FictionDetailModel> mAdapter;

    @Override
    protected int getContentViewID() {
        return R.layout.fragment_my_creations;
    }

    @Override
    protected void initViewsAndData(View view) {
        mDatas.add(new FictionDetailModel());
        mDatas.add(new FictionDetailModel());
        mDatas.add(new FictionDetailModel());
        mDatas.add(new FictionDetailModel());
        mDatas.add(new FictionDetailModel());
        mAdapter = new MultiItemTypeAdapter<>(getActivity(), mDatas);
        mAdapter.addItemViewDelegate(new OldCreationDelegate());
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mAdapter);
    }

    @OnClick(R.id.mycreation_add_img)
    public void clickAdd() {
        BasicCreateActivity.start(getActivity(), null);
    }

    @Override
    public void showMyCreations(List<FictionDetailModel> list) {

    }

    @Override
    public void setPresenter(MyCreationContract.Presenter presenter) {

    }
}
