package com.owl.chatstory.home;

import com.owl.chatstory.data.homesource.ICategoryData;
import com.owl.chatstory.data.homesource.ICategoryDataImpl;
import com.owl.chatstory.data.homesource.model.CategoryModel;

import java.util.List;

/**
 * Created by lebron on 2017/9/14.
 */

public class HomePresenter implements HomeContract.Presenter, ICategoryData.OnCategoryListListener {
    private HomeContract.View mView;
    private ICategoryData mCategoryData;

    public HomePresenter(HomeContract.View view) {
        mCategoryData = new ICategoryDataImpl();
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        mCategoryData.getCategoryList(this);
    }

    @Override
    public void unsubscribe() {
        mCategoryData.clearSubscriptions();
    }

    @Override
    public void onCategoryList(List<CategoryModel> list) {
        mView.showCategoryList(list);
    }
}