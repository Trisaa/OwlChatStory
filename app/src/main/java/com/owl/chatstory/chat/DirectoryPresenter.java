package com.owl.chatstory.chat;

import com.owl.chatstory.data.chatsource.ICollectData;
import com.owl.chatstory.data.chatsource.ICollectDataImpl;

/**
 * Created by lebron on 2017/10/30.
 */

public class DirectoryPresenter implements DirectoryContract.Presenter {
    private DirectoryContract.View mView;
    private ICollectData mData;

    public DirectoryPresenter(DirectoryContract.View view) {
        mView = view;
        mData = new ICollectDataImpl();
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
    public void collectFiction(String operation, String id) {
        if (operation.equals(DirectoryContract.COLLECT_FICTION)) {
            mData.collectFiction(id);
        } else {
            mData.uncollectFiction(id);
        }
    }

    @Override
    public void likeFiction(int status, String id) {
        mData.likeFiction(status, id);
    }
}
