package com.owl.chatstory.data.usersource;

import android.util.Log;

import com.owl.chatstory.common.util.network.HttpUtils;
import com.owl.chatstory.common.util.network.request.UserRequest;
import com.owl.chatstory.data.usersource.model.UserModel;
import com.owl.chatstory.data.usersource.model.UserPageModel;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lebron on 2018/1/29.
 */

public class IUserDataImpl implements IUserData {
    private CompositeSubscription mSubscriptions;

    public IUserDataImpl() {
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void clearSubscriptions() {
        mSubscriptions.clear();
    }

    @Override
    public void updateInfo(UserModel userModel, final OnUpdateListener listener) {
        Subscription subscription = HttpUtils.getInstance().updateUserinfo(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onResult(false);
                }
                Log.i("Lebron", e.toString());
            }

            @Override
            public void onNext(Object o) {
                if (listener != null) {
                    listener.onResult(true);
                }
            }
        }, convertModel2Request(userModel));
        mSubscriptions.add(subscription);
    }

    @Override
    public void getUserInfo(final OnUserInfoListener listener) {
        Subscription subscription = HttpUtils.getInstance().getUserInfo(new Subscriber<UserModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onUserInfo(null);
                }
                Log.i("Lebron", " getUserInfo failed " + e.toString());
            }

            @Override
            public void onNext(UserModel userModel) {
                if (listener != null) {
                    listener.onUserInfo(userModel);
                }
                Log.i("Lebron", " getUserInfo success ");
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getUserPageInfo(final OnUserPageListener listener, String id) {
        Subscription subscription = HttpUtils.getInstance().getUserPageInfo(new Subscriber<UserPageModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onUserPageInfo(null);
                }
            }

            @Override
            public void onNext(UserPageModel userPageModel) {
                if (listener != null) {
                    listener.onUserPageInfo(userPageModel);
                }
            }
        }, id);
        mSubscriptions.add(subscription);
    }

    private UserRequest convertModel2Request(UserModel model) {
        UserRequest request = new UserRequest();
        request.setName(model.getName());
        request.setAvatar(model.getIcon());
        request.setGender(model.getGender());
        request.setSummary(model.getSummary());
        return request;
    }
}
