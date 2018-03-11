package com.tap.chatstory.creation;

import com.tap.chatstory.base.BasePresenter;
import com.tap.chatstory.base.BaseView;
import com.tap.chatstory.data.chatsource.model.FictionDetailModel;

import java.util.List;

/**
 * Created by lebron on 2017/10/30.
 */

public interface MyCreationContract {
    interface Presenter extends BasePresenter {
        void getUserFictionList(String language);
    }

    interface View extends BaseView<Presenter> {
        void showMyCreations(List<FictionDetailModel> list);

        void showErrorView();
    }
}
