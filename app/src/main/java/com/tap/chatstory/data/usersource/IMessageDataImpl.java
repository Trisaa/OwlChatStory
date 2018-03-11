package com.tap.chatstory.data.usersource;

import com.tap.chatstory.common.util.network.HttpUtils;
import com.tap.chatstory.data.usersource.model.MessagesModel;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lebron on 2018/3/2.
 */

public class IMessageDataImpl implements IMessageData {

    private CompositeSubscription mSubscriptions;

    public IMessageDataImpl() {
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void clearSubscriptions() {
        mSubscriptions.clear();
    }

    @Override
    public void getMessageList(int page, final OnMessageListListener listener) {
        Subscription subscription = HttpUtils.getInstance().getUserMessageList(new Subscriber<List<MessagesModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<MessagesModel> list) {
                if (listener != null) {
                    listener.onMessageList(list);
                }
            }
        }, page);
        mSubscriptions.add(subscription);
    }

    @Override
    public void getUnreadCount(final OnMessageListListener listener) {
        Subscription subscription = HttpUtils.getInstance().getUnreadMessageCount(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                if (listener != null) {
                    listener.onMessageCount(integer);
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void readMessage(String id) {
        Subscription subscription = HttpUtils.getInstance().readMessage(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        }, id);
        mSubscriptions.add(subscription);
    }
}
