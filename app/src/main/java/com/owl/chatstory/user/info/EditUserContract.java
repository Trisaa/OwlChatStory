package com.owl.chatstory.user.info;

import com.owl.chatstory.base.BasePresenter;
import com.owl.chatstory.base.BaseView;
import com.owl.chatstory.data.usersource.model.UserModel;

/**
 * Created by lebron on 2018/1/29.
 */

public interface EditUserContract {
    interface Presenter extends BasePresenter {
        void updateUserInfo(UserModel userModel);
    }

    interface View extends BaseView<Presenter> {
        void showUserInfo();

        void updateFailed();
    }
}
