package com.tap.chatstory.data.chatsource;

import android.util.Log;

import com.tap.chatstory.base.SimpleResponseListener;
import com.tap.chatstory.common.util.network.HttpUtils;
import com.tap.chatstory.common.util.network.request.FictionListRequest;
import com.tap.chatstory.data.chatsource.model.FictionDetailModel;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lebron on 2017/9/21.
 */

public class IHistoryDataImpl implements IHistoryData {
    private CompositeSubscription mSubscriptions;

    public IHistoryDataImpl() {
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void clearSubscriptions() {
        mSubscriptions.clear();
    }

    @Override
    public void addToHistory(String id) {
        Subscription subscription = HttpUtils.getInstance().addToHistory(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("Lebron", " error " + e.toString());
            }

            @Override
            public void onNext(Object o) {
                Log.i("Lebron", " add history success " + o.toString());
            }
        }, id);
        mSubscriptions.add(subscription);
    }

    @Override
    public void getFictionList(final OnFictionListListener listener, FictionListRequest request, final boolean refresh) {
        Subscription subscription = HttpUtils.getInstance().getHistoryList(new Subscriber<List<FictionDetailModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("Lebron", " history list error " + e.toString());
                if (listener != null) {
                    listener.onError();
                }
            }

            @Override
            public void onNext(List<FictionDetailModel> list) {
                Log.i("Lebron", " history list success " + list.size());
                if (listener != null) {
                    listener.onFictionList(list, refresh);
                }
            }
        }, request.getQueryMap());
        mSubscriptions.add(subscription);
    }

    @Override
    public void clearHistory(final SimpleResponseListener listener) {
        Subscription subscription = HttpUtils.getInstance().clearHistory(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onFailed(e.toString());
                }
            }

            @Override
            public void onNext(Object o) {
                if (listener != null) {
                    listener.onSuccess();
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}
