package com.owl.chatstory.creation;

import com.owl.chatstory.data.chatsource.ICreateData;
import com.owl.chatstory.data.chatsource.ICreateDataImpl;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;

/**
 * Created by lebron on 2017/10/31.
 */

public class BasicCreatePresenter implements BasicCreateContract.Presenter, ICreateData.OnCreateListener {
    private BasicCreateContract.View mView;
    private ICreateData mData;


    public BasicCreatePresenter(BasicCreateContract.View view) {
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
    public void saveFictionBasicInfo(FictionDetailModel model) {
        mData.updateFictionBasicInfo(model, this);
        mView.showLoadingView(true);
    }

    @Override
    public void onUpdateSuccess(FictionDetailModel model) {
        mView.onSuccess(model);
        mView.showLoadingView(false);
    }

    @Override
    public void onFailed() {
        mView.onFailure();
        mView.showLoadingView(false);
    }
}
