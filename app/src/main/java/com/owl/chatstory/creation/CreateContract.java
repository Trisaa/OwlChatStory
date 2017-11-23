package com.owl.chatstory.creation;

import com.owl.chatstory.base.BasePresenter;
import com.owl.chatstory.base.BaseView;
import com.owl.chatstory.data.chatsource.model.ActorModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;
import com.owl.chatstory.data.usersource.model.UserModel;

import java.util.List;

/**
 * Created by lebron on 2017/11/3.
 */

public interface CreateContract {
    interface Presenter extends BasePresenter {
        void publishChapter(FictionModel model);

        void saveChapter(FictionModel model);

        void getRoleList(String id, String language);

        void updateRoleList(String id, String language, List<ActorModel> list);
    }

    interface View extends BaseView<Presenter> {
        void publishSuccess();

        void showRoleList(List<ActorModel> list);

        void publishFailed();
    }
}
