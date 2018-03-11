package com.tap.chatstory;

import com.tap.chatstory.base.BasePresenter;
import com.tap.chatstory.base.BaseView;
import com.tap.chatstory.data.homesource.model.UpdateModel;

/**
 * Created by lebron on 2017/9/22.
 */

public interface MainContract {
    interface Presenter extends BasePresenter {
    }

    interface View extends BaseView<Presenter> {
        void showUpdateDialog(UpdateModel model);
    }
}
