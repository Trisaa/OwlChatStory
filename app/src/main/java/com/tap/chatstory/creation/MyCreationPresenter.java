package com.tap.chatstory.creation;

import com.tap.chatstory.data.chatsource.ICreateData;
import com.tap.chatstory.data.chatsource.ICreateDataImpl;
import com.tap.chatstory.data.chatsource.OnFictionListListener;
import com.tap.chatstory.data.chatsource.model.FictionDetailModel;

import java.util.List;

/**
 * Created by lebron on 2017/10/30.
 */

public class MyCreationPresenter implements MyCreationContract.Presenter, OnFictionListListener {
    private MyCreationContract.View mView;
    private ICreateData mData;

    public MyCreationPresenter(MyCreationContract.View view) {
        mView = view;
        mData = new ICreateDataImpl();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mData.clearSubscription();
    }

    @Override
    public void onFictionList(List<FictionDetailModel> list, boolean refresh) {
        mView.showMyCreations(list);
    }

    @Override
    public void onError() {
        mView.showErrorView();
    }

    @Override
    public void getUserFictionList(String language) {
        mData.getUserFictionList(this, language);
    }
}
