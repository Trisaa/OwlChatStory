package com.tap.chatstory.home;

import com.tap.chatstory.base.BasePresenter;
import com.tap.chatstory.base.BaseView;
import com.tap.chatstory.data.homesource.model.CategoryModel;

import java.util.List;

/**
 * Created by lebron on 2017/9/14.
 */

public interface HomeContract {
    interface Presenter extends BasePresenter {
        void getCreateCategoryList();
    }

    interface View extends BaseView<Presenter> {
        void showCategoryList(List<CategoryModel> list);
    }
}
