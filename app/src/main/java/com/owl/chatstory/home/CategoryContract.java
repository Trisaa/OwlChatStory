package com.owl.chatstory.home;

import com.owl.chatstory.base.BasePresenter;
import com.owl.chatstory.base.BaseView;
import com.owl.chatstory.common.util.network.request.FictionListRequest;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;

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
