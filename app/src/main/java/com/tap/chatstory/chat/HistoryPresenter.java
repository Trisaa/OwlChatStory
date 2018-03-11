package com.tap.chatstory.chat;

import com.tap.chatstory.base.SimpleResponseListener;
import com.tap.chatstory.common.util.network.request.FictionListRequest;
import com.tap.chatstory.data.chatsource.IHistoryData;
import com.tap.chatstory.data.chatsource.IHistoryDataImpl;
import com.tap.chatstory.data.chatsource.OnFictionListListener;
import com.tap.chatstory.data.chatsource.model.FictionDetailModel;

import java.util.List;

/**
 * Created by lebron on 2017/9/14.
 */

public class HistoryPresenter implements HistoryContract.Presenter, OnFictionListListener, SimpleResponseListener {
    private HistoryContract.View mView;
    private IHistoryData mHistoryData;

    public HistoryPresenter(HistoryContract.View view) {
        mView = view;
        mHistoryData = new IHistoryDataImpl();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        mHistoryData.clearSubscriptions();
    }

    @Override
    public void onFictionList(List<FictionDetailModel> list, boolean refresh) {
        mView.showHistoryList(list, refresh);
    }

    @Override
    public void onError() {
        mView.showErrorView();
    }

    @Override
    public void getFictionList(FictionListRequest request, boolean refresh) {
        mHistoryData.getFictionList(this, request, refresh);
    }

    @Override
    public void clearHistory() {
        mHistoryData.clearHistory(this);
    }

    @Override
    public void onSuccess() {
        mView.clearHistory(true);
    }

    @Override
    public void onFailed(String error) {
        mView.clearHistory(false);
    }
}
