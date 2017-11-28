package com.owl.chatstory.creation;

import com.owl.chatstory.data.chatsource.ICreateData;
import com.owl.chatstory.data.chatsource.ICreateDataImpl;
import com.owl.chatstory.data.chatsource.IFictionData;
import com.owl.chatstory.data.chatsource.IFictionDataImpl;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;
import com.owl.chatstory.data.chatsource.model.OperationRequest;

import java.util.List;

/**
 * Created by lebron on 2017/11/16.
 */

public class CreationDetailPresenter implements CreationDetailContract.Presenter, IFictionData.OnFictionListener
        , ICreateData.OnChapterListener, ICreateData.OnOperateFictionListener, ICreateData.OnCreateListener {
    private CreationDetailContract.View mView;
    private IFictionData mData;
    private ICreateData mICreateData;

    public CreationDetailPresenter(CreationDetailContract.View view) {
        mView = view;
        mData = new IFictionDataImpl();
        mICreateData = new ICreateDataImpl();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mData.clearSubscriptions();
    }

    @Override
    public void getFictionDetail(String id, String language) {
        mData.getFictionDetail(id, language, this);
    }

    @Override
    public void getChapterList(String id, String language) {
        mICreateData.getChapterList(id, language, this);
    }

    @Override
    public void getChapterDetail(String chapterId, String language) {
        mData.getChapterDetail(chapterId, language, this);
    }

    @Override
    public void operateFiction(OperationRequest request) {
        mICreateData.operateFiction(request, this);
    }

    @Override
    public void operateChapter(OperationRequest request) {
        mICreateData.operateChapter(request, this);
    }

    @Override
    public void publishChapter(FictionModel model) {
        mICreateData.publishChapter(model, this);
    }

    @Override
    public void onFiction(FictionModel model) {
        mView.showFictionModel(model);
    }

    @Override
    public void onFictionDetail(FictionDetailModel model) {
        mView.showFictionDetail(model);
    }

    @Override
    public void onGetChapterList(List<FictionModel> list) {
        mView.showChapterList(list);
    }

    @Override
    public void operateFictionFinished(boolean success) {
        mView.operateFictionFinished(success);
    }

    @Override
    public void operateChapterFinished(boolean success) {
        mView.operateChapterFinished(success);
    }

    @Override
    public void onUpdateSuccess(FictionDetailModel model) {
        mView.publishSuccess();
    }

    @Override
    public void onFailed() {
        mView.publishFailed();
    }
}
