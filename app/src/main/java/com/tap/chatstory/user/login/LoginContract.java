package com.tap.chatstory.user.login;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.tap.chatstory.base.BasePresenter;
import com.tap.chatstory.base.BaseView;
import com.tap.chatstory.common.view.CustomTwitterLoginButton;
import com.tap.chatstory.data.usersource.model.UserModel;

/**
 * Created by lebron on 2017/9/4.
 */

public interface LoginContract {
    interface Presenter extends BasePresenter {
        /**
         * 登录Facebook
         *
         * @param activity
         */
        void signinWithFacebook(FragmentActivity activity);

        /**
         * 登录Twitter
         */
        void signinWithTwitter(CustomTwitterLoginButton customTwitterLoginButton);

        /**
         * 从登陆界面返回
         */
        void onActivityResult(int requestCode, int responseCode, Intent intent);
    }

    interface View extends BaseView<Presenter> {
        void loginResult(UserModel userModel);

        void showProgressBar(boolean show);
    }
}
