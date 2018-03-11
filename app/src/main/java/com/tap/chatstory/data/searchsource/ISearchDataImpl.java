package com.tap.chatstory.data.searchsource;

import android.util.Log;

import com.tap.chatstory.common.util.network.HttpUtils;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lebron on 2018/1/29.
 */

public class ISearchDataImpl implements ISearchData {
    private CompositeSubscription mSubscriptions;

    public ISearchDataImpl() {
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void clearSubscriptions() {
        mSubscriptions.clear();
    }

    @Override
    public void getSearchData(String keyword, final OnSearchDataListener listener) {
        Subscription subscription = HttpUtils.getInstance().searchData(new Subscriber<SearchModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("Lebron"," error "+e.toString());
            }

            @Override
            public void onNext(SearchModel model) {
                if (listener != null) {
                    listener.onResult(model);
                }
            }
        }, keyword);
        mSubscriptions.add(subscription);
    }
}
