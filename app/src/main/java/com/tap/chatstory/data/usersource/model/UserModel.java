package com.tap.chatstory.data.usersource.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lebron on 2017/9/14.
 */

public class UserModel implements Parcelable {
    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel source) {
            return new UserModel(source);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
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
    @SerializedName("summary")
    private String summary;//用户简介
    @SerializedName("email")
    private String email;
    @SerializedName("device_token")
    private String deviceToken;

    public UserModel() {
    }

    protected UserModel(Parcel in) {
        this.id = in.readString();
        this.platform = in.readString();
        this.platformId = in.readString();
        this.name = in.readString();
        this.icon = in.readString();
        this.gender = in.readInt();
        this.summary = in.readString();
        this.email = in.readString();
        this.deviceToken = in.readString();
    }

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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.platform);
        dest.writeString(this.platformId);
        dest.writeString(this.name);
        dest.writeString(this.icon);
        dest.writeInt(this.gender);
        dest.writeString(this.summary);
        dest.writeString(this.email);
        dest.writeString(this.deviceToken);
    }
}
