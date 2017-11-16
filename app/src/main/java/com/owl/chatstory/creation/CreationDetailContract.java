package com.owl.chatstory.creation;

import com.owl.chatstory.base.BasePresenter;
import com.owl.chatstory.base.BaseView;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;

/**
 * Created by lebron on 2017/11/16.
 */

public interface CreationDetailContract {
    interface Presenter extends BasePresenter {
        void getFictionDetail(String id);
    }

    interface View extends BaseView<Presenter> {
        void showFictionDetail(FictionDetailModel model);
    }
}
