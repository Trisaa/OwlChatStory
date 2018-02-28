package com.owl.chatstory.data.chatsource.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lebron on 2017/12/20.
 */

public class CollectResponse {
    @SerializedName("collect")
    private int collect;
    @SerializedName("thumbsup")
    private int liked;

    public boolean getCollect() {
        return collect == 1 ? true : false;
    }

    public void setCollect(boolean collect) {
        this.collect = collect ? 1 : 0;
    }

    public boolean getLiked() {
        return liked == 1 ? true : false;
    }
}
