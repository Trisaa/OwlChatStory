package com.tap.chatstory.data.chatsource;

import com.tap.chatstory.common.util.network.request.FictionListRequest;
import com.tap.chatstory.data.chatsource.model.FictionStatusResponse;

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

    void prayUpdate(String fictionId);

    interface FictionStatusListener {
        void onStatus(FictionStatusResponse response);
    }
}
