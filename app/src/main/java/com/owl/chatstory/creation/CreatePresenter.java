package com.owl.chatstory.creation;


import com.owl.chatstory.data.chatsource.ICreateData;
import com.owl.chatstory.data.chatsource.ICreateDataImpl;
import com.owl.chatstory.data.chatsource.model.ActorModel;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;
import com.owl.chatstory.data.chatsource.model.RoleListRequest;
import com.owl.chatstory.data.usersource.model.UserModel;

import java.util.List;

/**
 * Created by lebron on 2017/11/3.
 */

public class CreatePresenter implements CreateContract.Presenter, ICreateData.OnCreateListener, ICreateData.OnRoleListener {
    private CreateContract.View mView;
    private ICreateData mData;

    public CreatePresenter(CreateContract.View view) {
        mView = view;
        mData = new ICreateDataImpl();
        mView.setPresenter(this);
    }

    @Override
    public void publishChapter(FictionModel model) {
        mData.publishChapter(model, this);
    }

    @Override
    public void saveChapter(FictionModel model) {

    }

    @Override
    public void getRoleList(String id, String language) {
        mData.getRoleList(id, language, this);
    }

    @Override
    public void updateRoleList(String id, String language, List<ActorModel> list) {
        RoleListRequest request = new RoleListRequest();
        request.setIfiction_id(id);
        request.setList(list);
        request.setLanguage(language);
        mData.updateRoleList(request, list, this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mData.clearSubscription();
    }

    @Override
    public void onUpdateSuccess(FictionDetailModel model) {
        mView.publishSuccess();
    }

    @Override
    public void onFailed() {
        mView.publishFailed();
    }

    @Override
    public void onGetRoleList(List<ActorModel> list) {
        mView.showRoleList(list);
    }
}
