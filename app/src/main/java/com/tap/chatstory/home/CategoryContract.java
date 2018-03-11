package com.tap.chatstory.home;

import com.tap.chatstory.base.BasePresenter;
import com.tap.chatstory.base.BaseView;
import com.tap.chatstory.common.util.network.request.FictionListRequest;
import com.tap.chatstory.data.chatsource.model.FictionDetailModel;

import java.util.List;

/**
 * Created by lebron on 2017/9/15.
 */

public interface CategoryContract {
    interface Presenter extends BasePresenter {
        void getFictionList(FictionListRequest request, boolean refresh);
    }

    interface View extends BaseView<Presenter> {
        void showFictionList(List<FictionDetailModel> list, boolean refresh);

        void showRefreshing(boolean show);

        void showErrorLayout(boolean show);
    }
}
