package com.tap.chatstory.data.usersource;

import com.tap.chatstory.data.usersource.model.TaskModel;
import com.tap.chatstory.data.usersource.model.WalletModel;

import java.util.List;

/**
 * Created by lebron on 2018/4/16.
 */

public interface ICoinData {
    void clearSubscriptions();

    void getUserCoins(OnUserCoinListener listener);

    void getRewards(String source, OnRewardedListener listener);

    void inputInviteCode(String code,OnRewardedListener listener);

    void getTaskList(OnTaskListListener listener);

    interface OnUserCoinListener {
        void onCoin(WalletModel model);
    }

    interface OnRewardedListener {
        void onRewarded();
    }

    interface OnTaskListListener {
        void onTaskList(TaskModel taskModel);
    }
}
