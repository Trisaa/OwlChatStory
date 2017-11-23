package com.owl.chatstory.data.chatsource;

import com.owl.chatstory.data.chatsource.model.FictionDetailModel;
import com.owl.chatstory.data.chatsource.model.FictionModel;

/**
 * Created by lebron on 2017/9/15.
 */

public interface IFictionData {
    void clearSubscriptions();

    void getFictionDetail(String id, String language, OnFictionListener listener);

    void getChapterDetail(String id, String language, OnFictionListener listener);

    interface OnFictionListener {
        void onFiction(FictionModel model);

        void onFictionDetail(FictionDetailModel model);
    }
}
