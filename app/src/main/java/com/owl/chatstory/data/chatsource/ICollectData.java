package com.owl.chatstory.data.chatsource;

import com.owl.chatstory.common.util.network.request.FictionListRequest;

/**
 * Created by lebron on 2017/12/18.
 */

public interface ICollectData {
    void clearSubscriptions();

    void collectFiction(String fictionId);

    void uncollectFiction(String fictionId);

    void getCollectionList(OnFictionListListener listener, FictionListRequest request, boolean refresh);

    void isFictionCollected(String fictionId, CollectListener listener);

    interface CollectListener {
        void isCollect(boolean collected);
    }
}
