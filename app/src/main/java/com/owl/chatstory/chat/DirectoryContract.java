package com.owl.chatstory.chat;

import com.owl.chatstory.base.BasePresenter;
import com.owl.chatstory.base.BaseView;

/**
 * Created by lebron on 2017/10/30.
 */

public interface DirectoryContract {
    String COLLECT_FICTION = "COLLECT_FICTION";
    String UNCOLLECT_FICTION = "UNCOLLECT_FICTION";
    int LIKE_FICTION = 1;
    int DISLIKE_FICTION = 0;

    interface Presenter extends BasePresenter {
        void collectFiction(String operation, String id);

        void likeFiction(int status, String id);
    }

    interface View extends BaseView<Presenter> {

    }
}
