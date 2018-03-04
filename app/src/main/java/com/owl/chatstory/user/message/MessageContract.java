package com.owl.chatstory.user.message;

import com.owl.chatstory.base.BasePresenter;
import com.owl.chatstory.base.BaseView;
import com.owl.chatstory.data.usersource.model.MessagesModel;

import java.util.List;

/**
 * Created by lebron on 2018/3/2.
 */

public interface MessageContract {
    interface Presenter extends BasePresenter {
        void getUnreadMessageCount();

        void readMessage(String id);

        void getMessageList(int page);
    }

    interface View extends BaseView<Presenter> {
        void showMessageList(List<MessagesModel> list);

        void showMessageCount(int count);
    }
}
