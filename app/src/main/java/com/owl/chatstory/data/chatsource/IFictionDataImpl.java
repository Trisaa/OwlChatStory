package com.owl.chatstory.data.chatsource;

import com.owl.chatstory.common.util.network.HttpUtils;
import com.owl.chatstory.data.chatsource.model.ChapterModel;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;

import java.util.List;

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
    public void getFictionDetail(String id, String language, final OnFictionListener listener) {
        Subscription subscription = HttpUtils.getInstance(language).getFictionDetail(new Subscriber<FictionDetailModel>() {
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
    public void getChapterDetail(String id, String language, final OnFictionListener listener) {
        Subscription subscription = HttpUtils.getInstance(language).getChapterDetail(new Subscriber<FictionModel>() {
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

    @Override
    public void getChapterList(String id, int page, final OnChapterListener listener) {
        Subscription subscription = HttpUtils.getInstance().getFictionChapterList(new Subscriber<List<ChapterModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<ChapterModel> list) {
                if (listener != null) {
                    listener.onChapterList(list);
                }
            }
        }, id, page);
        mSubscriptions.add(subscription);
    }
}
