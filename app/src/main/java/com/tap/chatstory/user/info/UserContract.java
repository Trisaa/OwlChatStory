package com.tap.chatstory.user.info;

import com.tap.chatstory.base.BasePresenter;
import com.tap.chatstory.base.BaseView;
import com.tap.chatstory.data.usersource.model.UserModel;

/**
 * Created by lebron on 2018/1/30.
 */

public interface UserContract {
    interface Presenter extends BasePresenter {
        void uploadDeviceToken(String token);

        void getUnreadCount();
    }

    interface View extends BaseView<Presenter> {
        void showUserInfo(UserModel userModel);

        void showMessageCount(int count);
    }

}
