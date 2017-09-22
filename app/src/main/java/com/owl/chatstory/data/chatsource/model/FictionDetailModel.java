package com.owl.chatstory.data.chatsource.model;

import com.google.gson.annotations.SerializedName;
import com.owl.chatstory.data.usersource.model.UserModel;

import java.util.List;

/**
 * Created by lebron on 2017/9/17.
 */

public class FictionDetailModel {
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("cover")
    private String cover;
    /*@SerializedName("tags")
    private List<String> tags;*/
    @SerializedName("summary")
    private String summary;
    @SerializedName("views")
    private int views;
    @SerializedName("writer")
    private UserModel writer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    /*public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }*/

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public UserModel getWriter() {
        return writer;
    }

    public void setWriter(UserModel writer) {
        this.writer = writer;
    }
}
