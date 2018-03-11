package com.tap.chatstory.creation;

import com.tap.chatstory.base.BasePresenter;
import com.tap.chatstory.base.BaseView;
import com.tap.chatstory.data.chatsource.model.FictionDetailModel;

/**
 * Created by lebron on 2017/10/31.
 */

public interface BasicCreateContract {
    interface Presenter extends BasePresenter {
        void saveFictionBasicInfo(FictionDetailModel model);
    }

    interface View extends BaseView<Presenter> {
        void showLoadingView(boolean show);

        void onSuccess(FictionDetailModel model);

        void onFailure();
    }
}
