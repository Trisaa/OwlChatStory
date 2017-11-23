package com.owl.chatstory.data.chatsource.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lebron on 2017/9/12.
 */

public class FictionModel implements Parcelable {
    @SerializedName("id")
    private String id;
    @SerializedName("num")
    private int num;
    @SerializedName("name")
    private String name;
    @SerializedName("cover")
    private String cover;
    @SerializedName("content")
    private List<MessageModel> list;
    @SerializedName("status")
    private int status;

    private String token;
    private String ifiction_id;
    private String language;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIfiction_id() {
        return ifiction_id;
    }

    public void setIfiction_id(String ifiction_id) {
        this.ifiction_id = ifiction_id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.num);
        dest.writeString(this.name);
        dest.writeString(this.cover);
        dest.writeTypedList(this.list);
        dest.writeString(this.token);
        dest.writeString(this.ifiction_id);
        dest.writeString(this.language);
    }

    public FictionModel() {
    }

    protected FictionModel(Parcel in) {
        this.id = in.readString();
        this.num = in.readInt();
        this.name = in.readString();
        this.cover = in.readString();
        this.list = in.createTypedArrayList(MessageModel.CREATOR);
        this.token = in.readString();
        this.ifiction_id = in.readString();
        this.language = in.readString();
    }

    public static final Parcelable.Creator<FictionModel> CREATOR = new Parcelable.Creator<FictionModel>() {
        @Override
        public FictionModel createFromParcel(Parcel source) {
            return new FictionModel(source);
        }

        @Override
        public FictionModel[] newArray(int size) {
            return new FictionModel[size];
        }
    };
}
