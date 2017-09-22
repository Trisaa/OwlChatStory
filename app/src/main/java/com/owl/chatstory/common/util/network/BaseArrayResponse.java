package com.owl.chatstory.common.util.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by lebron on 2017/9/1.
 */

public class BaseArrayResponse<T> {
    @SerializedName("status")
    private int status;
    @SerializedName("msg")
    private String message;
    @SerializedName("data")
    private ArrayList<T> data;

    public int getCode() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<T> getData() {
        return data;
    }
}
