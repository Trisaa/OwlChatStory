package com.owl.chatstory.search;

import com.owl.chatstory.base.BasePresenter;
import com.owl.chatstory.base.BaseView;
import com.owl.chatstory.data.searchsource.SearchModel;

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
