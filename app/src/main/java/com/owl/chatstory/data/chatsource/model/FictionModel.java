package com.owl.chatstory.data.chatsource.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lebron on 2017/9/12.
 */

public class FictionModel {
    @SerializedName("id")
    private String id;
    @SerializedName("num")
    private String watchers;
    @SerializedName("name")
    private String name;
    @SerializedName("cover")
    private String cover;
    @SerializedName("content")
    private List<MessageModel> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWatchers() {
        return watchers;
    }

    public void setWatchers(String watchers) {
        this.watchers = watchers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<MessageModel> getList() {
        return list;
    }

    public void setList(List<MessageModel> list) {
        this.list = list;
    }
}
