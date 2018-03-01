package com.owl.chatstory.data.usersource;

import com.owl.chatstory.data.usersource.model.UserModel;
import com.owl.chatstory.data.usersource.model.UserPageModel;

/**
 * Created by lebron on 2018/1/29.
 */

public interface IUserData {
    void clearSubscriptions();

    void updateInfo(UserModel userModel, OnUpdateListener listener);

    void getUserInfo(OnUserInfoListener listener);

    void getUserPageInfo(OnUserPageListener listener, String id);

    void getOwnPageInfo(OnUserPageListener listener);

    interface OnUpdateListener {
        void onResult(boolean success);
    }

    interface OnUserInfoListener {
        void onUserInfo(UserModel model);
    }

    interface OnUserPageListener {
        void onUserPageInfo(UserPageModel model);
    }
}
