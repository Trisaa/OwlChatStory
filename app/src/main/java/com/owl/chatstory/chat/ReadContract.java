package com.owl.chatstory.chat;

import com.owl.chatstory.base.BasePresenter;
import com.owl.chatstory.base.BaseView;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;
import com.owl.chatstory.data.chatsource.model.FictionStatusResponse;

/**
 * Created by lebron on 2017/9/14.
 */

public interface ReadContract {
    interface Presenter extends BasePresenter {

        void getChapterData(String id, int vip, boolean allow);

        void addToHistory(String id);

        void collectFiction(String id);

        void uncollectFiction(String id);

        void likeFiction(int status, String id);

        void prayUpdate(String id);
    }

    interface View extends BaseView<Presenter> {
        void showFictionData(FictionModel model);

        void showWaittingDialog(String chapterId);
    }
}
