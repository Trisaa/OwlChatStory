package com.tap.chatstory.search;

import com.tap.chatstory.data.searchsource.ISearchData;
import com.tap.chatstory.data.searchsource.ISearchDataImpl;
import com.tap.chatstory.data.searchsource.SearchModel;

/**
 * Created by lebron on 2018/1/29.
 */

public class SearchPresenter implements SearchContract.Presenter, ISearchData.OnSearchDataListener {
    private SearchContract.View mView;
    private ISearchData mData;

    public SearchPresenter(SearchContract.View view) {
        mView = view;
        mData = new ISearchDataImpl();
        mView.setPresenter(this);
    }

    @Override
    public void searchData(String keyword) {
        mData.getSearchData(keyword, this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mData.clearSubscriptions();
    }

    @Override
    public void onResult(SearchModel model) {
        mView.showSearchData(model);
    }
}
