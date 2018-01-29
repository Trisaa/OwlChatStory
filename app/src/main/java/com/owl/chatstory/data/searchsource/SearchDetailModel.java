package com.owl.chatstory.data.searchsource;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lebron on 2018/1/29.
 */

public class SearchDetailModel {
    @SerializedName("id")
    private String id;
    @SerializedName("type")
    private int type;
    @SerializedName("fid")
    private String fictionId;
    @SerializedName("title")
    private String title;
    @SerializedName("cover")
    private String cover;
    @SerializedName("tags")
    private String tags;
    @SerializedName("summary")
    private String summary;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFictionId() {
        return fictionId;
    }

    public void setFictionId(String fictionId) {
        this.fictionId = fictionId;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
