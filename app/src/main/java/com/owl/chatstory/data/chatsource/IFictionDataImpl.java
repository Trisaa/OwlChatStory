package com.owl.chatstory.data.chatsource;

import com.owl.chatstory.common.util.network.HttpUtils;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lebron on 2017/9/15.
 */

public class IFictionDataImpl implements IFictionData {
    private CompositeSubscription mSubscriptions;

    public IFictionDataImpl() {
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void clearSubscriptions() {
        mSubscriptions.clear();
    }

    @Override
    public void getFictionDetail(String id, final OnFictionListener listener) {
        Subscription subscription = HttpUtils.getInstance().getFictionDetail(new Subscriber<FictionDetailModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(FictionDetailModel model) {
                if (listener != null) {
                    listener.onFictionDetail(model);
                }
            }
        }, id);
        mSubscriptions.add(subscription);
    }

    @Override
    public void getChapterDetail(String id, final OnFictionListener listener) {
        Subscription subscription = HttpUtils.getInstance().getChapterDetail(new Subscriber<FictionModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(FictionModel model) {
                if (listener != null) {
                    listener.onFiction(model);
                }
            }
        }, id);
        mSubscriptions.add(subscription);
    }
}
