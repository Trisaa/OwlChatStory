package com.owl.chatstory.creation;

import com.owl.chatstory.data.chatsource.model.FictionDetailModel;

/**
 * Created by lebron on 2017/10/31.
 */

public class BasicCreatePresenter implements BasicCreateContract.Presenter {
    private BasicCreateContract.View mView;


    public BasicCreatePresenter(BasicCreateContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void saveFictionBasicInfo(FictionDetailModel model) {

    }
}
