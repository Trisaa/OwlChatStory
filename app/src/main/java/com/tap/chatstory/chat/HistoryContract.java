package com.tap.chatstory.chat;

import com.tap.chatstory.base.BasePresenter;
import com.tap.chatstory.base.BaseView;
import com.tap.chatstory.common.util.network.request.FictionListRequest;
import com.tap.chatstory.data.chatsource.model.FictionDetailModel;

import java.util.List;

/**
 * Created by lebron on 2017/9/14.
 */

public interface HistoryContract {
    interface Presenter extends BasePresenter {
        void getFictionList(FictionListRequest request, boolean refresh);

        void clearHistory();
    }

    interface View extends BaseView<Presenter> {
        void showHistoryList(List<FictionDetailModel> list, boolean refresh);

        void showErrorView();

        void clearHistory(boolean success);
    }
}
