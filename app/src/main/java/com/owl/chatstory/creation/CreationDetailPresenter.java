package com.owl.chatstory.creation;

import com.owl.chatstory.data.chatsource.IFictionData;
import com.owl.chatstory.data.chatsource.IFictionDataImpl;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;

/**
 * Created by lebron on 2017/11/16.
 */

public class CreationDetailPresenter implements CreationDetailContract.Presenter, IFictionData.OnFictionListener {
    private CreationDetailContract.View mView;
    private IFictionData mData;

    public CreationDetailPresenter(CreationDetailContract.View view) {
        mView = view;
        mData = new IFictionDataImpl();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mData.clearSubscriptions();
    }

    @Override
    public void getFictionDetail(String id) {
        mData.getFictionDetail(id, this);
    }

    @Override
    public void onFiction(FictionModel model) {

    }

    @Override
    public void onFictionDetail(FictionDetailModel model) {
        mView.showFictionDetail(model);
    }
}
