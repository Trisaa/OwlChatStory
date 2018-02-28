package com.owl.chatstory.data.chatsource;

import com.owl.chatstory.common.util.network.request.FictionListRequest;
import com.owl.chatstory.data.chatsource.model.FictionStatusResponse;

/**
 * Created by lebron on 2017/12/18.
 */

public interface ICollectData {
    void clearSubscriptions();

    void collectFiction(String fictionId);

    void uncollectFiction(String fictionId);

    void likeFiction(int status, String fictionId);

    void getCollectionList(OnFictionListListener listener, FictionListRequest request, boolean refresh);

    void isFictionCollected(String fictionId, FictionStatusListener listener);

    interface FictionStatusListener {
        void onStatus(FictionStatusResponse response);
    }
}
