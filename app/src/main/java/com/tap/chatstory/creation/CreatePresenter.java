package com.tap.chatstory.creation;


import com.google.gson.Gson;
import com.tap.chatstory.common.util.PreferencesHelper;
import com.tap.chatstory.data.chatsource.ICreateData;
import com.tap.chatstory.data.chatsource.ICreateDataImpl;
import com.tap.chatstory.data.chatsource.model.ActorModel;
import com.tap.chatstory.data.chatsource.model.FictionDetailModel;
import com.tap.chatstory.data.chatsource.model.FictionModel;
import com.tap.chatstory.data.chatsource.model.RoleListRequest;

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
        String key = model.getIfiction_id() + model.getLanguage() + "chapter";
        List<FictionModel> list = PreferencesHelper.getInstance().getLocalChapterList(key);
        boolean existed = false;
        for (int i = 0; i < list.size(); i++) {
            FictionModel fictionModel = list.get(i);
            if (fictionModel.getNum() == model.getNum()) {
                existed = true;
                list.set(i, model);
                break;
            }
        }
        if (!existed) {
            list.add(model);
        }
        PreferencesHelper.getInstance().setString(key, new Gson().toJson(list).toString());
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
