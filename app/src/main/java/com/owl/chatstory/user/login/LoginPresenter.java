package com.owl.chatstory.user.login;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.owl.chatstory.common.view.CustomTwitterLoginButton;
import com.owl.chatstory.data.usersource.FacebookAuth;
import com.owl.chatstory.data.usersource.ILoginData;
import com.owl.chatstory.data.usersource.ILoginDataImpl;
import com.owl.chatstory.data.usersource.TwitterAuth;
import com.owl.chatstory.data.usersource.model.UserModel;

/**
 * Created by lebron on 2017/9/4.
 */

public class LoginPresenter implements LoginContract.Presenter, ILoginData.OnAuthListener, ILoginData.OnLoginListener {
    private LoginContract.View mView;
    private ILoginData mLoginData;
    private TwitterAuth mTwitterAuth;
    private FacebookAuth mFacebookAuth;

    public LoginPresenter(LoginContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mLoginData = new ILoginDataImpl();
    }

    @Override
    public void signinWithFacebook(FragmentActivity activity) {
        /*if (mFacebookAuth == null) {
            mFacebookAuth = new FacebookAuth(activity);
        }
        mLoginData.auth(mFacebookAuth, this);*/
        UserModel model = new UserModel();
        model.setPlatform("facebook");
        model.setGender(1);
        model.setPlatformId("123465068394570");
        model.setName("Yong He");
        model.setIcon("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505930560108&di=125dafb4017534b6eb316e33cf6ce1d8&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201505%2F31%2F20150531181218_GnXtc.thumb.224_0.jpeg");
        onAuthSuccess(model);
    }

    @Override
    public void signinWithTwitter(CustomTwitterLoginButton customTwitterLoginButton) {
        if (mTwitterAuth == null) {
            mTwitterAuth = new TwitterAuth(customTwitterLoginButton);
        }
        mLoginData.auth(mTwitterAuth, this);
        /*UserModel model = new UserModel();
        model.setPlatform("twitter");
        model.setGender(0);
        model.setPlatformId("704614290976714752");
        model.setName("Future");
        model.setIcon("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505930560108&di=125dafb4017534b6eb316e33cf6ce1d8&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201505%2F31%2F20150531181218_GnXtc.thumb.224_0.jpeg");
        onAuthSuccess(model);*/
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        mLoginData.resultLogin(requestCode, responseCode, intent);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mLoginData.clearSubscriptions();
    }

    @Override
    public void onAuthSuccess(UserModel userModel) {
        mLoginData.login(userModel, this);
    }

    @Override
    public void onAuthError(String message) {
        if (mView != null) {
            mView.loginResult(null);
        }
    }

    @Override
    public void onAuthCancel() {
        if (mView != null) {
            mView.loginResult(null);
        }
    }

    @Override
    public void onRevoke() {

    }

    @Override
    public void onLoginSuccess(UserModel model) {
        if (mView != null) {
            mView.loginResult(model);
        }
    }

    @Override
    public void onLoginFailed() {
        if (mView != null) {
            mView.loginResult(null);
        }
    }
}
