package com.owl.chatstory.user.info;

import com.owl.chatstory.data.usersource.IUserData;
import com.owl.chatstory.data.usersource.IUserDataImpl;
import com.owl.chatstory.data.usersource.model.UserModel;

/**
 * Created by lebron on 2018/1/30.
 */

public class UserPresenter implements UserContract.Presenter, IUserData.OnUserInfoListener {
    private UserContract.View mView;
    private IUserData mData;

    public UserPresenter(UserContract.View view) {
        mView = view;
        mData = new IUserDataImpl();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mData.getUserInfo(this);
    }

    @Override
    public void unsubscribe() {
        mData.clearSubscriptions();
    }

    @Override
    public void onUserInfo(UserModel model) {
        mView.showUserInfo(model);
    }
}
