package com.owl.chatstory.creation;

import com.owl.chatstory.data.chatsource.ICreateData;
import com.owl.chatstory.data.chatsource.ICreateDataImpl;
import com.owl.chatstory.data.chatsource.IFictionData;
import com.owl.chatstory.data.chatsource.IFictionDataImpl;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;

import java.util.List;

/**
 * Created by lebron on 2017/11/16.
 */

public class CreationDetailPresenter implements CreationDetailContract.Presenter, IFictionData.OnFictionListener, ICreateData.OnChapterListener {
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
    public void onFiction(FictionModel model) {

    }

    @Override
    public void onFictionDetail(FictionDetailModel model) {
        mView.showFictionDetail(model);
    }

    @Override
    public void onGetChapterList(List<FictionModel> list) {
        mView.showChapterList(list);
    }
}
