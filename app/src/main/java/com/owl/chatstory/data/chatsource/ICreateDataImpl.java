package com.owl.chatstory.data.chatsource;

import android.util.Log;

import com.owl.chatstory.common.util.network.HttpUtils;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;
import com.owl.chatstory.data.chatsource.model.RoleListRequest;
import com.owl.chatstory.data.usersource.model.UserModel;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lebron on 2017/11/20.
 */

public class ICreateDataImpl implements ICreateData {
    private CompositeSubscription mSubscriptions;

    public ICreateDataImpl() {
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void clearSubscription() {
        mSubscriptions.clear();
    }

    @Override
    public void updateFictionBasicInfo(FictionDetailModel model, final OnCreateListener listener) {
        Subscription subscription = HttpUtils.getInstance().updateFictionBasicInfo(new Subscriber<FictionDetailModel>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.i("Lebron", " exception " + e.toString());
                if (listener != null) {
                    listener.onFailed();
                }
            }

            @Override
            public void onNext(FictionDetailModel model) {
                if (listener != null) {
                    listener.onUpdateSuccess(model);
                }
            }
        }, model);
        mSubscriptions.add(subscription);
    }

    @Override
    public void getUserFictionList(final OnFictionListListener listener, String language) {
        Subscription subscription = HttpUtils.getInstance().getUserFictionList(new Subscriber<List<FictionDetailModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("Lebron", e.toString());
                if (listener != null) {
                    listener.onError();
                }
            }

            @Override
            public void onNext(List<FictionDetailModel> fictionDetailModels) {
                if (listener != null) {
                    listener.onFictionList(fictionDetailModels, true);
                }
            }
        }, language);
        mSubscriptions.add(subscription);
    }

    @Override
    public void publishChapter(FictionModel model, final OnCreateListener listener) {
        Subscription subscription = HttpUtils.getInstance().publishChapter(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onFailed();
                }
            }

            @Override
            public void onNext(Object o) {
                if (listener != null) {
                    listener.onUpdateSuccess(null);
                }
            }
        }, model);
        mSubscriptions.add(subscription);
    }

    @Override
    public void getRoleList(String id, String language, final OnRoleListener listener) {
        Subscription subscription = HttpUtils.getInstance().getRoleList(new Subscriber<List<UserModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onGetRoleList(null);
                }
            }

            @Override
            public void onNext(List<UserModel> list) {
                if (listener != null) {
                    listener.onGetRoleList(list);
                }
            }
        }, language, id);
        mSubscriptions.add(subscription);
    }

    @Override
    public void updateRoleList(RoleListRequest request, final List<UserModel> list, final OnRoleListener listener) {
        Subscription subscription = HttpUtils.getInstance().updateRoleList(new Subscriber<List<UserModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onGetRoleList(null);
                }
            }

            @Override
            public void onNext(List<UserModel> list) {
                if (listener != null) {
                    listener.onGetRoleList(list);
                }
            }
        }, request);
        mSubscriptions.add(subscription);
    }
}
