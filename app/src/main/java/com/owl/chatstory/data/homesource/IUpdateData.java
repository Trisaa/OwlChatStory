package com.owl.chatstory.data.homesource;

import com.owl.chatstory.data.homesource.model.UpdateModel;

/**
 * Created by lebron on 2017/8/1.
 */

public interface IUpdateData {
    void clearSubscription();

    void checkUpdateState(OnUpdateListener listener);

    interface OnUpdateListener {
        void onUpdate(UpdateModel model);
    }
}
