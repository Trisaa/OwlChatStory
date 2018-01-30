package com.owl.chatstory.data.usersource;

import com.owl.chatstory.data.usersource.model.UserModel;

/**
 * Created by lebron on 2018/1/29.
 */

public interface IUserData {
    void clearSubscriptions();

    void updateInfo(UserModel userModel, OnUpdateListener listener);

    void getUserInfo(OnUserInfoListener listener);

    interface OnUpdateListener {
        void onResult(boolean success);
    }

    interface OnUserInfoListener {
        void onUserInfo(UserModel model);
    }

}
