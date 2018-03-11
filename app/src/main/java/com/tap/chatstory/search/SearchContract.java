package com.tap.chatstory.search;

import com.tap.chatstory.base.BasePresenter;
import com.tap.chatstory.base.BaseView;
import com.tap.chatstory.data.searchsource.SearchModel;

/**
 * Created by lebron on 2018/1/29.
 */

public interface SearchContract {
    interface Presenter extends BasePresenter {
        void searchData(String keyword);
    }

    interface View extends BaseView<Presenter> {
        void showSearchData(SearchModel model);
    }
}
