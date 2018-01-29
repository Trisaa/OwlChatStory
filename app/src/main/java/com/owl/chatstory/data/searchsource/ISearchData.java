package com.owl.chatstory.data.searchsource;

/**
 * Created by lebron on 2018/1/29.
 */

public interface ISearchData {
    void clearSubscriptions();

    void getSearchData(String keyword, OnSearchDataListener listener);

    interface OnSearchDataListener {
        void onResult(SearchModel model);
    }
}
