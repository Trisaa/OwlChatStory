package com.tap.chatstory.data.chatsource;

import com.tap.chatstory.base.SimpleResponseListener;
import com.tap.chatstory.common.util.network.request.FictionListRequest;

/**
 * Created by lebron on 2017/9/21.
 */

public interface IHistoryData {
    void clearSubscriptions();

    void addToHistory(String id);

    void getFictionList(OnFictionListListener listener, FictionListRequest request, boolean refresh);

    void clearHistory(SimpleResponseListener listener);
}
