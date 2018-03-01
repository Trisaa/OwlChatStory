package com.owl.chatstory.data.chatsource;

import com.owl.chatstory.data.chatsource.model.ChapterModel;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;

import java.util.List;

/**
 * Created by lebron on 2017/9/15.
 */

public interface IFictionData {
    void clearSubscriptions();

    void getFictionDetail(String id, String language, OnFictionListener listener);

    void getChapterDetail(String id, String language, OnFictionListener listener);

    void getChapterList(String id, int page, OnChapterListener listener);

    interface OnFictionListener {
        void onFiction(FictionModel model);

        void onFictionDetail(FictionDetailModel model);
    }

    interface OnChapterListener {
        void onChapterList(List<ChapterModel> list);
    }
}
