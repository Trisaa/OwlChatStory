package com.owl.chatstory.user.page;

import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.usersource.IUserData;
import com.owl.chatstory.data.usersource.IUserDataImpl;
import com.owl.chatstory.data.usersource.model.UserPageModel;

import java.util.List;

/**
 * Created by lebron on 2018/2/26.
 */

public class UserPagePresenter implements UserPageContract.Presenter, IUserData.OnUserPageListener {
    private UserPageContract.View mView;
    private IUserData mData;

    public UserPagePresenter(UserPageContract.View view) {
        mView = view;
        mData = new IUserDataImpl();
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
    public void getUserPageInfo(String id) {
        if (id != null) {
            mData.getUserPageInfo(this, id);
        } else {
            mData.getOwnPageInfo(this);
        }
    }

    @Override
    public void getUserRelatedFictionList(String id, int page) {
        if (id != null) {
            mData.getUserFictionList(this, id, page);
        } else {
            mData.getOwnFictionList(this, page);
        }
    }

    @Override
    public void onUserPageInfo(UserPageModel model) {
        mView.showUserPageInfo(model);
    }

    @Override
    public void onUserFictionList(List<FictionDetailModel> list) {
        mView.showFictionList(list);
    }
}
