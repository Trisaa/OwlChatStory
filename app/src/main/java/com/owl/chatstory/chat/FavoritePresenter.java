package com.owl.chatstory.chat;

import com.owl.chatstory.common.util.network.request.FictionListRequest;
import com.owl.chatstory.data.chatsource.ICollectData;
import com.owl.chatstory.data.chatsource.ICollectDataImpl;
import com.owl.chatstory.data.chatsource.OnFictionListListener;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;

import java.util.List;

/**
 * Created by lebron on 2017/12/7.
 */

public class FavoritePresenter implements FavoriteContract.Presenter, OnFictionListListener {
    private ICollectData mDatas;
    private FavoriteContract.View mView;

    public FavoritePresenter(FavoriteContract.View view) {
        mDatas = new ICollectDataImpl();
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mDatas.clearSubscriptions();
    }

    @Override
    public void getFictionList(FictionListRequest request, boolean refresh) {
        mDatas.getCollectionList(this, request, refresh);
    }

    @Override
    public void onFictionList(List<FictionDetailModel> list, boolean refresh) {
        mView.showCollectionList(list, refresh);
    }

    @Override
    public void onError() {
        mView.showErrorView();
    }
}
