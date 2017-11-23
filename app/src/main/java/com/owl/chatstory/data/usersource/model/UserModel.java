package com.owl.chatstory.data.usersource.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lebron on 2017/9/14.
 */

public class UserModel implements Parcelable {
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
        dest.writeInt(this.vipType);
    }

    public UserModel() {
    }

    protected UserModel(Parcel in) {
        this.id = in.readString();
        this.platform = in.readString();
        this.platformId = in.readString();
        this.name = in.readString();
        this.icon = in.readString();
        this.gender = in.readInt();
        this.vipType = in.readInt();
    }

    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel source) {
            return new UserModel(source);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
}
