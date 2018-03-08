package com.owl.chatstory.chat;

import com.owl.chatstory.base.BasePresenter;
import com.owl.chatstory.base.BaseView;
import com.owl.chatstory.data.chatsource.model.ChapterModel;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionStatusResponse;

import java.util.List;

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

        void getChapterList(String id, int page);

        void getFictionDetail(String id);
    }

    interface View extends BaseView<Presenter> {
        void showChapterList(List<ChapterModel> list);

        void showFictionDetail(FictionDetailModel model);

        void showFictionStatus(FictionStatusResponse response);
    }
}
