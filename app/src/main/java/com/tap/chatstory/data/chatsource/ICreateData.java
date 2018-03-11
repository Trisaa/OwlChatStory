package com.tap.chatstory.data.chatsource;

import com.tap.chatstory.data.chatsource.model.ActorModel;
import com.tap.chatstory.data.chatsource.model.FictionDetailModel;
import com.tap.chatstory.data.chatsource.model.FictionModel;
import com.tap.chatstory.data.chatsource.model.OperationRequest;
import com.tap.chatstory.data.chatsource.model.RoleListRequest;

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

    void operateFiction(OperationRequest request, OnOperateFictionListener listener);

    void operateChapter(OperationRequest request, OnOperateFictionListener listener);

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

    interface OnOperateFictionListener {
        void operateFictionFinished(boolean success, String operation);

        void operateChapterFinished(boolean success);
    }
}
