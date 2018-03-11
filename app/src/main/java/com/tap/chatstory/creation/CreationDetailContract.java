package com.tap.chatstory.creation;

import com.tap.chatstory.base.BasePresenter;
import com.tap.chatstory.base.BaseView;
import com.tap.chatstory.data.chatsource.model.FictionDetailModel;
import com.tap.chatstory.data.chatsource.model.FictionModel;
import com.tap.chatstory.data.chatsource.model.OperationRequest;

import java.util.List;

/**
 * Created by lebron on 2017/11/16.
 */

public interface CreationDetailContract {
    interface Presenter extends BasePresenter {
        void getFictionDetail(String id, String language);

        void getChapterList(String id, String language);

        void getChapterDetail(String chapterId, String language);

        void operateFiction(OperationRequest request);

        void operateChapter(OperationRequest request);

        void publishChapter(FictionModel model);
    }

    interface View extends BaseView<Presenter> {
        void showFictionDetail(FictionDetailModel model);

        void showChapterList(List<FictionModel> list);

        void showFictionModel(FictionModel model);

        void operateFictionFinished(boolean success, String operation);

        void operateChapterFinished(boolean success);

        void publishSuccess();

        void publishFailed();
    }
}
