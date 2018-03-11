package com.tap.chatstory.data.chatsource.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lebron on 2017/11/22.
 */

public class FictionResponse implements Serializable{
    @SerializedName("id")
    private int id;
    @SerializedName("is_end")
    private int ended;
    @SerializedName("serials")
    private int serials;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEnded() {
        return ended;
    }

    public void setEnded(int ended) {
        this.ended = ended;
    }

    public int getSerials() {
        return serials;
    }

    public void setSerials(int serials) {
        this.serials = serials;
    }
}
