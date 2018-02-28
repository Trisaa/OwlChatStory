package com.owl.chatstory.user.page;

import com.owl.chatstory.base.BasePresenter;
import com.owl.chatstory.base.BaseView;
import com.owl.chatstory.data.usersource.model.UserPageModel;

/**
 * Created by lebron on 2018/2/26.
 */

public interface UserPageContract {

    interface Presenter extends BasePresenter {
        void getUserPageInfo(String id);
    }

    interface View extends BaseView<Presenter> {
        void showUserPageInfo(UserPageModel model);
    }

}
