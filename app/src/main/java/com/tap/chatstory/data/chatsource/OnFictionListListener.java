package com.tap.chatstory.data.chatsource;

import com.tap.chatstory.data.chatsource.model.FictionDetailModel;

import java.util.List;

/**
 * Created by lebron on 2017/9/21.
 */

public interface OnFictionListListener {
    void onFictionList(List<FictionDetailModel> list, boolean refresh);

    void onError();
}
