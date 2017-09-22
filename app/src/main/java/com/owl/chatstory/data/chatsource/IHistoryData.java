package com.owl.chatstory.data.chatsource;

import com.owl.chatstory.base.SimpleResponseListener;
import com.owl.chatstory.common.util.network.request.FictionListRequest;

/**
 * Created by lebron on 2017/9/21.
 */

public interface IHistoryData {
    void clearSubscriptions();

    void addToHistory(String id);

    void getFictionList(OnFictionListListener listener, FictionListRequest request, boolean refresh);

    void clearHistory(SimpleResponseListener listener);
}
