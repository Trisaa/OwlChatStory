package com.owl.chatstory.data.usersource;

import android.util.Log;

import com.owl.chatstory.common.util.PreferencesHelper;
import com.owl.chatstory.common.util.network.HttpUtils;
import com.owl.chatstory.common.util.network.request.UserRequest;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.usersource.model.UserModel;
import com.owl.chatstory.data.usersource.model.UserPageModel;

import java.util.List;

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

    @Override
    public void getUserFictionList(final OnUserPageListener listener, String id, int page) {
        Subscription subscription = HttpUtils.getInstance().getUserRelatedFictionList(new Subscriber<List<FictionDetailModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<FictionDetailModel> list) {
                if (listener != null) {
                    listener.onUserFictionList(list);
                }
            }
        }, id, page);
        mSubscriptions.add(subscription);
    }

    @Override
    public void getOwnPageInfo(final OnUserPageListener listener) {
        Subscription subscription = HttpUtils.getInstance().getOwnPageInfo(new Subscriber<UserPageModel>() {
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
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getOwnFictionList(final OnUserPageListener listener, int page) {
        Subscription subscription = HttpUtils.getInstance().getOwnRelatedFictionList(new Subscriber<List<FictionDetailModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<FictionDetailModel> list) {
                if (listener != null) {
                    listener.onUserFictionList(list);
                }
            }
        }, page);
        mSubscriptions.add(subscription);
    }

    @Override
    public void uploadDeviceToken(String token) {
        Subscription subscription = HttpUtils.getInstance().uploadDeviceToken(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("Lebron", " upload device failed");
            }

            @Override
            public void onNext(Object o) {
                Log.i("Lebron", " upload device success");
                PreferencesHelper.getInstance().setBoolean(PreferencesHelper.KEY_DEVICE_TOKEN_UPLOADED, true);
            }
        }, token);
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
