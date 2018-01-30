package com.owl.chatstory.user.info;

import com.owl.chatstory.data.usersource.IUserData;
import com.owl.chatstory.data.usersource.IUserDataImpl;
import com.owl.chatstory.data.usersource.model.UserModel;

/**
 * Created by lebron on 2018/1/29.
 */

public class EditUserPresenter implements EditUserContract.Presenter, IUserData.OnUpdateListener {
    private EditUserContract.View mView;
    private IUserData mData;

    public EditUserPresenter(EditUserContract.View view) {
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
    public void updateUserInfo(UserModel userModel) {
        mData.updateInfo(userModel, this);
    }

    @Override
    public void onResult(boolean success) {
        if (success) {
            mView.showUserInfo();
        } else {
            mView.updateFailed();
        }
    }
}
