package com.owl.chatstory.data.chatsource;

import com.owl.chatstory.data.chatsource.model.ActorModel;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;
import com.owl.chatstory.data.chatsource.model.RoleListRequest;
import com.owl.chatstory.data.usersource.model.UserModel;

import java.util.List;

/**
 * Created by lebron on 2017/11/20.
 */

public interface ICreateData {
    void clearSubscription();

    void updateFictionBasicInfo(FictionDetailModel model, OnCreateListener listener);

    void getUserFictionList(OnFictionListListener listener, String language);

    void publishChapter(FictionModel model, OnCreateListener listener);

    void getRoleList(String id, String language, OnRoleListener listener);

    void updateRoleList(RoleListRequest request, List<ActorModel> list, OnRoleListener listener);

    void getChapterList(String id, String language, OnChapterListener listener);

    interface OnCreateListener {
        void onUpdateSuccess(FictionDetailModel model);

        void onFailed();
    }

    interface OnRoleListener {
        void onGetRoleList(List<ActorModel> list);
    }

    interface OnChapterListener {
        void onGetChapterList(List<FictionModel> list);
    }
}
