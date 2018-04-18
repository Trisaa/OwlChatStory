package com.tap.chatstory.user.info;

import com.tap.chatstory.data.usersource.ICoinData;
import com.tap.chatstory.data.usersource.ICoinDataImpl;
import com.tap.chatstory.data.usersource.model.TaskModel;

import java.util.List;

/**
 * Created by lebron on 2018/4/16.
 */

public class DailyTaskPresenter implements DailyTaskContract.Presenter, ICoinData.OnTaskListListener,ICoinData.OnRewardedListener {
    private DailyTaskContract.View mView;
    private ICoinData mData;

    public DailyTaskPresenter(DailyTaskContract.View view) {
        mView = view;
        mData = new ICoinDataImpl();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mData.getTaskList(this);
    }

    @Override
    public void unsubscribe() {
        mData.clearSubscriptions();
    }

    @Override
    public void onTaskList(TaskModel taskModel) {
        mView.showTaskList(taskModel);
    }

    @Override
    public void getRewards(String source) {
        mData.getRewards(source,this);
    }

    @Override
    public void onRewarded() {
        mView.showRewards();
    }
}
