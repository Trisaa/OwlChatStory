package com.owl.chatstory;

import com.owl.chatstory.base.BasePresenter;
import com.owl.chatstory.base.BaseView;
import com.owl.chatstory.data.homesource.model.UpdateModel;

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
