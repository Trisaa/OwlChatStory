package com.owl.chatstory.data.chatsource;

import android.util.Log;

import com.owl.chatstory.common.util.network.HttpUtils;
import com.owl.chatstory.common.util.network.request.FictionListRequest;
import com.owl.chatstory.data.chatsource.model.CollectResponse;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lebron on 2017/12/18.
 */

public class ICollectDataImpl implements ICollectData {
    private CompositeSubscription mSubscriptions;

    public ICollectDataImpl() {
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void clearSubscriptions() {
        mSubscriptions.clear();
    }

    @Override
    public void collectFiction(String fictionId) {
        Subscription subscription = HttpUtils.getInstance().collectFiction(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("Lebron", "collectFiction " + e.toString());
            }

            @Override
            public void onNext(Object o) {
                Log.i("Lebron", "collectFiction success");
            }
        }, fictionId);
        mSubscriptions.add(subscription);
    }

    @Override
    public void uncollectFiction(String fictionId) {
        Subscription subscription = HttpUtils.getInstance().uncollectFiction(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("Lebron", "uncollectFiction " + e.toString());
            }

            @Override
            public void onNext(Object o) {
                Log.i("Lebron", "uncollectFiction success");
            }
        }, fictionId);
        mSubscriptions.add(subscription);
    }

    @Override
    public void getCollectionList(final OnFictionListListener listener, FictionListRequest request, final boolean refresh) {
        Subscription subscription = HttpUtils.getInstance().getCollectionList(new Subscriber<List<FictionDetailModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<FictionDetailModel> fictionDetailModels) {
                if (listener != null) {
                    listener.onFictionList(fictionDetailModels, refresh);
                }
            }
        }, request);
        mSubscriptions.add(subscription);
    }

    @Override
    public void isFictionCollected(String fictionId, final CollectListener listener) {
        Subscription subscription = HttpUtils.getInstance().isCollect(new Subscriber<CollectResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(CollectResponse collectResponse) {
                if (listener != null && collectResponse != null) {
                    listener.isCollect(collectResponse.getCollect());
                }
            }
        }, fictionId);
        mSubscriptions.add(subscription);
    }
}
