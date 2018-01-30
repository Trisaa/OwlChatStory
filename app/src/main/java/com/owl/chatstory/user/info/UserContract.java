package com.owl.chatstory.user.info;

import com.owl.chatstory.base.BasePresenter;
import com.owl.chatstory.base.BaseView;
import com.owl.chatstory.data.usersource.model.UserModel;

/**
 * Created by lebron on 2018/1/30.
 */

public interface UserContract {
    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {
        void showUserInfo(UserModel userModel);
    }

}
