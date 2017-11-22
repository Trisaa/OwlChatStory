package com.owl.chatstory.creation;

import com.owl.chatstory.base.BasePresenter;
import com.owl.chatstory.base.BaseView;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;

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
