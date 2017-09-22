package com.owl.chatstory.data.usersource.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lebron on 2017/9/14.
 */

public class UserModel {
    @SerializedName("id")
    private String id;//这个ID是注册成功后你那边生成的用户主键
    @SerializedName("platform")
    private String platform;//平台（facebook/twitter）
    @SerializedName("platformID")
    private String platformId;//用户在平台中的userId
    @SerializedName("username")
    private String name; //用户昵称
    @SerializedName("picture")
    private String icon; //用户头像
    @SerializedName("gender")
    private int gender;//用户性别

    private int vipType;//用户VIP类型

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getVipType() {
        return vipType;
    }

    public void setVipType(int vipType) {
        this.vipType = vipType;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
