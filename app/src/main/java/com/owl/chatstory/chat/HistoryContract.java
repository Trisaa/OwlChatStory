package com.owl.chatstory.chat;

import com.owl.chatstory.base.BasePresenter;
import com.owl.chatstory.base.BaseView;
import com.owl.chatstory.common.util.network.request.FictionListRequest;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;

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
