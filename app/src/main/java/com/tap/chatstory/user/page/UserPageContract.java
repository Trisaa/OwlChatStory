package com.tap.chatstory.user.page;

import com.tap.chatstory.base.BasePresenter;
import com.tap.chatstory.base.BaseView;
import com.tap.chatstory.data.chatsource.model.FictionDetailModel;
import com.tap.chatstory.data.usersource.model.UserPageModel;

import java.util.List;

/**
 * Created by lebron on 2018/2/26.
 */

public interface UserPageContract {

    interface Presenter extends BasePresenter {
        void getUserPageInfo(String id);

        void getUserRelatedFictionList(String id, int page);
    }

    interface View extends BaseView<Presenter> {
        void showUserPageInfo(UserPageModel model);

        void showFictionList(List<FictionDetailModel> list);
    }

}
