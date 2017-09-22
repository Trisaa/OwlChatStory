package com.owl.chatstory.home;

import com.owl.chatstory.base.BasePresenter;
import com.owl.chatstory.base.BaseView;
import com.owl.chatstory.data.homesource.model.CategoryModel;

import java.util.List;

/**
 * Created by lebron on 2017/9/14.
 */

public interface HomeContract {
    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {
        void showCategoryList(List<CategoryModel> list);
    }
}
