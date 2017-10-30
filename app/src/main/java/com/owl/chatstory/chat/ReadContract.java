package com.owl.chatstory.chat;

import com.owl.chatstory.base.BasePresenter;
import com.owl.chatstory.base.BaseView;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;

/**
 * Created by lebron on 2017/9/14.
 */

public interface ReadContract {
    interface Presenter extends BasePresenter {
        void getFictionData(String id);

        void getChapterData(String id);

        void addToHistory(String id);
    }

    interface View extends BaseView<Presenter> {
        void showFictionData(FictionModel model);

        void showFictionDetailData(FictionDetailModel model);
    }
}
