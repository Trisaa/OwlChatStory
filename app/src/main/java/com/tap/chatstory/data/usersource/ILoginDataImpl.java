package com.tap.chatstory.data.usersource;

import android.content.Intent;
import android.util.Base64;
import android.util.Log;

import com.tap.chatstory.common.util.AES256;
import com.tap.chatstory.common.util.PreferencesHelper;
import com.tap.chatstory.common.util.network.HttpUtils;
import com.tap.chatstory.common.util.network.request.UserRequest;
import com.tap.chatstory.data.usersource.model.UserModel;
import com.tap.chatstory.data.usersource.model.UserResponse;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lebron on 2017/9/14.
 */

public class ILoginDataImpl implements ILoginData {
    private static final String TOKEN_SECRET = "87we#Oe6h7k2YRw6asf3*Kel45DFj43q";
    private SignInAuth mAuth;
    private CompositeSubscription mSubscriptions;

    public ILoginDataImpl() {
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void clearSubscriptions() {
        mSubscriptions.clear();
    }

    @Override
    public void auth(SignInAuth auth, OnAuthListener listener) {
        mAuth = auth;
        mAuth.setOnAuthListener(listener);
        mAuth.signIn();
    }

    @Override
    public void resultLogin(int requestCode, int resultCode, Intent data) {
        if (mAuth != null) {
            mAuth.resultLogin(requestCode, resultCode, data);
        }
    }

    @Override
    public void login(UserModel userModel, final OnLoginListener listener) {
        Subscription subscription = HttpUtils.getInstance().signUpOrIn(new Subscriber<UserResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onLoginFailed();
                }
                Log.i("Lebron", " error " + e.toString());
            }

            @Override
            public void onNext(UserResponse userResponse) {
                if (listener != null && userResponse != null) {
                    Log.i("Lebron", " token " + userResponse.getToken());
                    PreferencesHelper.getInstance().setString(PreferencesHelper.KEY_TOKEN, userResponse.getToken());
                    listener.onLoginSuccess(userResponse.getUserModel());
                }
            }
        }, createUserRequest(userModel));
        mSubscriptions.add(subscription);
    }

    private UserRequest createUserRequest(UserModel userModel) {
        UserRequest userRequest = new UserRequest();
        userRequest.setName(userModel.getName());
        userRequest.setGender(userModel.getGender());
        userRequest.setAvatar(userModel.getIcon());
        String token = System.currentTimeMillis() + "_" + userModel.getPlatform() + "_" + userModel.getPlatformId() + "_ifiction";
        Log.i("Lebron", " request origin token " + token);
        String output2 = AES256.encrypt(token, TOKEN_SECRET);
        String output = Base64.encodeToString(output2.getBytes(), Base64.NO_WRAP);
        userRequest.setToken(output);
        Log.i("Lebron", " request token " + output);
        return userRequest;
    }
}
