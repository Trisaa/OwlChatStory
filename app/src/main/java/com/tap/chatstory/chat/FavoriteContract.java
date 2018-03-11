package com.tap.chatstory.chat;

import com.tap.chatstory.base.BasePresenter;
import com.tap.chatstory.base.BaseView;
import com.tap.chatstory.common.util.network.request.FictionListRequest;
import com.tap.chatstory.data.chatsource.model.FictionDetailModel;

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
