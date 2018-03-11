package com.tap.chatstory.home;

import android.widget.Toast;

import com.tap.chatstory.MainApplication;
import com.tap.chatstory.R;
import com.tap.chatstory.common.util.network.request.FictionListRequest;
import com.tap.chatstory.data.chatsource.OnFictionListListener;
import com.tap.chatstory.data.chatsource.model.FictionDetailModel;
import com.tap.chatstory.data.homesource.ICategoryData;
import com.tap.chatstory.data.homesource.ICategoryDataImpl;

import java.util.List;

/**
 * Created by lebron on 2017/9/15.
 */

public class CategoryPresenter implements CategoryContract.Presenter, OnFictionListListener {
    private CategoryContract.View mView;
    private ICategoryData mCategoryData;

    public CategoryPresenter(CategoryContract.View view) {
        mCategoryData = new ICategoryDataImpl();
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mView.showRefreshing(true);
    }

    @Override
    public void unsubscribe() {
        mCategoryData.clearSubscriptions();
    }

    @Override
    public void getFictionList(FictionListRequest request, boolean refresh) {
        mCategoryData.getFictionList(this, request, refresh);
    }

    @Override
    public void onFictionList(List<FictionDetailModel> list, boolean refresh) {
        mView.showErrorLayout(false);
        mView.showFictionList(list, refresh);
        mView.showRefreshing(false);
    }

    @Override
    public void onError() {
        mView.showRefreshing(false);
        mView.showErrorLayout(true);
        Toast.makeText(MainApplication.getAppContext(), R.string.common_network_error, Toast.LENGTH_SHORT).show();
    }
}
