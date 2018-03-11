package com.tap.chatstory.data.chatsource;

import android.util.Log;

import com.tap.chatstory.common.util.PreferencesHelper;
import com.tap.chatstory.common.util.network.HttpUtils;
import com.tap.chatstory.data.chatsource.model.ActorModel;
import com.tap.chatstory.data.chatsource.model.FictionDetailModel;
import com.tap.chatstory.data.chatsource.model.FictionModel;
import com.tap.chatstory.data.chatsource.model.FictionResponse;
import com.tap.chatstory.data.chatsource.model.OperationRequest;
import com.tap.chatstory.data.chatsource.model.RoleListRequest;

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
    public void updateFictionBasicInfo(final FictionDetailModel model, final OnCreateListener listener) {
        Subscription subscription = HttpUtils.getInstance().updateFictionBasicInfo(new Subscriber<FictionResponse>() {
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
            public void onNext(FictionResponse response) {
                if (listener != null) {
                    model.setId(response.getId() + "");
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
                Log.i("Lebron", "getUserFictionList " + e.toString());
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
    public void publishChapter(final FictionModel model, final OnCreateListener listener) {
        Subscription subscription = HttpUtils.getInstance().publishChapter(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("Lebron", "publishChapter " + e.toString());
                if (listener != null) {
                    listener.onFailed();
                }
            }

            @Override
            public void onNext(Object o) {
                PreferencesHelper.getInstance().removeLocalChapter(model);
                if (listener != null) {
                    listener.onUpdateSuccess(null);
                }
            }
        }, model);
        mSubscriptions.add(subscription);
    }

    @Override
    public void getRoleList(String id, String language, final OnRoleListener listener) {
        Subscription subscription = HttpUtils.getInstance().getRoleList(new Subscriber<List<ActorModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("Lebron", "getRoleList " + e.toString());
                if (listener != null) {
                    listener.onGetRoleList(null);
                }
            }

            @Override
            public void onNext(List<ActorModel> list) {
                if (listener != null) {
                    listener.onGetRoleList(list);
                }
            }
        }, language, id);
        mSubscriptions.add(subscription);
    }

    @Override
    public void updateRoleList(RoleListRequest request, final List<ActorModel> list, final OnRoleListener listener) {
        Subscription subscription = HttpUtils.getInstance().updateRoleList(new Subscriber<List<ActorModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("Lebron", "updateRoleList " + e.toString());
                if (listener != null) {
                    listener.onGetRoleList(null);
                }
            }

            @Override
            public void onNext(List<ActorModel> list) {
                if (listener != null) {
                    listener.onGetRoleList(list);
                }
            }
        }, request);
        mSubscriptions.add(subscription);
    }

    @Override
    public void getChapterList(final String id, final String language, final OnChapterListener listener) {
        Subscription subscription = HttpUtils.getInstance().getChapterList(new Subscriber<List<FictionModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("Lebron", "getChapterList " + e.toString());
                if (listener != null) {
                    listener.onGetChapterList(null);
                }
            }

            @Override
            public void onNext(List<FictionModel> list) {
                if (listener != null) {
                    String key = id + language + "chapter";
                    List<FictionModel> localChapterList = PreferencesHelper.getInstance().getLocalChapterList(key);
                    list.addAll(localChapterList);
                    listener.onGetChapterList(list);
                }
            }
        }, language, id);
        mSubscriptions.add(subscription);
    }

    @Override
    public void operateFiction(final OperationRequest request, final OnOperateFictionListener listener) {
        Subscription subscription = HttpUtils.getInstance().operateFiction(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.operateFictionFinished(false, request.getOp());
                }
            }

            @Override
            public void onNext(Object o) {
                if (listener != null) {
                    listener.operateFictionFinished(true, request.getOp());
                }
            }
        }, request);
        mSubscriptions.add(subscription);
    }

    @Override
    public void operateChapter(OperationRequest request, final OnOperateFictionListener listener) {
        Subscription subscription = HttpUtils.getInstance().operateChapter(new Subscriber() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.operateChapterFinished(false);
                }
            }

            @Override
            public void onNext(Object o) {
                if (listener != null) {
                    listener.operateChapterFinished(true);
                }
            }
        }, request);
        mSubscriptions.add(subscription);
    }


}
