package com.owl.chatstory.data.homesource.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lebron on 2017/8/1.
 */

public class UpdateModel {
    @SerializedName("content")
    private String content;//更新提示，更新了哪些内容
    @SerializedName("path")
    private String path;//更新下载链接
    @SerializedName("force_version")
    private int forceVersion;//必须强制更新的版本，低于这个版本的将不能使用
    @SerializedName("latest_version")
    private int latestVersion;//GooglePlay上最新版本


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getForceVersion() {
        return forceVersion;
    }

    public void setForceVersion(int forceVersion) {
        this.forceVersion = forceVersion;
    }

    public int getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(int latestVersion) {
        this.latestVersion = latestVersion;
    }
}
