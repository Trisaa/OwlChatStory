package com.owl.chatstory.base;

/**
 * Created by lebron on 2017/9/22.
 */

public interface SimpleResponseListener {
    void onSuccess();

    void onFailed(String error);
}
