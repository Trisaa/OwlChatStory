package com.tap.chatstory.user.info;

import com.tap.chatstory.base.BasePresenter;
import com.tap.chatstory.base.BaseView;
import com.tap.chatstory.data.usersource.model.TaskModel;

/**
 * Created by lebron on 2018/4/16.
 */

public interface DailyTaskContract {
    interface Presenter extends BasePresenter {
        void getRewards(String source);
    }

    interface View extends BaseView<Presenter> {
        void showTaskList(TaskModel taskModel);

        void showRewards();
    }
}
