package com.owl.chatstory.chat;

import com.owl.chatstory.base.BasePresenter;
import com.owl.chatstory.base.BaseView;
import com.owl.chatstory.common.util.network.request.FictionListRequest;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;

import java.util.List;

/**
 * Created by lebron on 2017/12/7.
 */

public interface FavoriteContract {
    interface Presenter extends BasePresenter {
        void getFictionList(FictionListRequest request, boolean refresh);
    }

    interface View extends BaseView<Presenter> {
        void showCollectionList(List<FictionDetailModel> list, boolean refresh);

        void showErrorView();
    }
}
