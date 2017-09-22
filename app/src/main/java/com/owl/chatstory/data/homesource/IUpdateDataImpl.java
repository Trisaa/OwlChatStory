package com.owl.chatstory.data.homesource;

import android.support.annotation.NonNull;
import android.util.Log;

import com.owl.chatstory.data.homesource.model.UpdateModel;
import com.owl.chatstory.common.util.network.HttpUtils;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lebron on 2017/8/1.
 */

public class IUpdateDataImpl implements IUpdateData {
    @NonNull
    private CompositeSubscription mSubscriptions;

    public IUpdateDataImpl() {
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void clearSubscription() {
        mSubscriptions.clear();
    }

    @Override
    public void checkUpdateState(final OnUpdateListener listener) {
        Subscription subscription = HttpUtils.getInstance()
                .checkUpdate(new Subscriber<UpdateModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("IUpdateData", e.toString());
                    }

                    @Override
                    public void onNext(UpdateModel model) {
                        if (listener != null && model != null) {
                            Log.i("IUpdateData", model.getContent());
                            listener.onUpdate(model);
                        }
                    }
                });
        mSubscriptions.add(subscription);
    }
}
