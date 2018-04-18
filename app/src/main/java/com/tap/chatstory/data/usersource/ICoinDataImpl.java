package com.tap.chatstory.data.usersource;

import android.widget.Toast;

import com.tap.chatstory.MainApplication;
import com.tap.chatstory.R;
import com.tap.chatstory.common.util.network.HttpUtils;
import com.tap.chatstory.data.usersource.model.TaskModel;
import com.tap.chatstory.data.usersource.model.WalletModel;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lebron on 2018/4/16.
 */

public class ICoinDataImpl implements ICoinData {
    public static final String REWARDS_READED = "read";
    public static final String REWARDS_SHARED = "share";
    public static final String REWARDS_LOGINED = "login";
    public static final String REWARDS_CREATED = "create";
    private CompositeSubscription mSubscriptions;

    public ICoinDataImpl() {
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void clearSubscriptions() {
        mSubscriptions.clear();
    }

    @Override
    public void getUserCoins(final OnUserCoinListener listener) {
        Subscription subscription = HttpUtils.getInstance().getUserCoins(new Subscriber<WalletModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(WalletModel model) {
                if (listener != null) {
                    listener.onCoin(model);
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getRewards(String source, final OnRewardedListener listener) {
        Subscription subscription = HttpUtils.getInstance().getRewards(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                if (listener != null) {
                    listener.onRewarded();
                }
            }
        }, source);
        mSubscriptions.add(subscription);
    }

    @Override
    public void inputInviteCode(final String code, final OnRewardedListener listener) {
        Subscription subscription = HttpUtils.getInstance().inputInviteCode(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainApplication.getAppContext(), R.string.invite_input_code_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Object o) {
                if (listener != null) {
                    listener.onRewarded();
                }
            }
        }, code);
        mSubscriptions.add(subscription);
    }

    @Override
    public void getTaskList(final OnTaskListListener listener) {
        Subscription subscription = HttpUtils.getInstance().getTaskList(new Subscriber<TaskModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(TaskModel taskModel) {
                if (listener != null) {
                    listener.onTaskList(taskModel);
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}
