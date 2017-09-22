package com.owl.chatstory.data.usersource;

import android.content.Intent;

import com.owl.chatstory.data.usersource.model.UserModel;

/**
 * Created by lebron on 2017/9/14.
 */

public interface ILoginData {
    void clearSubscriptions();

    void auth(SignInAuth auth, OnAuthListener listener);

    void resultLogin(int requestCode, int resultCode, Intent data);

    void login(UserModel userModel, OnLoginListener listener);

    interface OnAuthListener {
        void onAuthSuccess(UserModel model);

        void onAuthError(String message);

        void onAuthCancel();

        void onRevoke();
    }

    interface OnLoginListener {
        void onLoginSuccess(UserModel userModel);

        void onLoginFailed();
    }
}
