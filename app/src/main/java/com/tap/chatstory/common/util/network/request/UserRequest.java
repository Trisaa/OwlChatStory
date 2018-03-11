package com.tap.chatstory.common.util.network.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lebron on 2017/9/20.
 */

public class UserRequest implements Serializable {
    @SerializedName("token")
    private String token;
    @SerializedName("name")
    private String name;
    @SerializedName("gender")
    private int gender;
    @SerializedName("picture")
    private String avatar;
    @SerializedName("summary")
    private String summary;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
