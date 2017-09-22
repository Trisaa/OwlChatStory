package com.owl.chatstory.common.util.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lebron on 2017/7/31.
 */

public class BaseResponse<T> {
    @SerializedName("status")
    private int status;
    @SerializedName("msg")
    private String message;
    @SerializedName("data")
    private T data;

    public int getCode() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
