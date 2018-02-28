package com.owl.chatstory.data.usersource.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.owl.chatstory.data.chatsource.model.FictionDetailModel;

import java.util.List;

/**
 * Created by lebron on 2018/2/28.
 */

public class UserPageModel implements Parcelable {
    public static final Parcelable.Creator<UserPageModel> CREATOR = new Parcelable.Creator<UserPageModel>() {
        @Override
        public UserPageModel createFromParcel(Parcel source) {
            return new UserPageModel(source);
        }

        @Override
        public UserPageModel[] newArray(int size) {
            return new UserPageModel[size];
        }
    };
    @SerializedName("user_id")
    private String userId;
    @SerializedName("name")
    private String name; //用户昵称
    @SerializedName("picture")
    private String icon; //
    @SerializedName("views")
    private int watches;
    @SerializedName("favorite")
    private int favorites;
    @SerializedName("thumbsup")
    private int likes;
    @SerializedName("list")
    private List<FictionDetailModel> list;

    public UserPageModel() {
    }

    protected UserPageModel(Parcel in) {
        this.userId = in.readString();
        this.name = in.readString();
        this.icon = in.readString();
        this.watches = in.readInt();
        this.favorites = in.readInt();
        this.likes = in.readInt();
        this.list = in.createTypedArrayList(FictionDetailModel.CREATOR);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getWatches() {
        return watches;
    }

    public void setWatches(int watches) {
        this.watches = watches;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<FictionDetailModel> getList() {
        return list;
    }

    public void setList(List<FictionDetailModel> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.name);
        dest.writeString(this.icon);
        dest.writeInt(this.watches);
        dest.writeInt(this.favorites);
        dest.writeInt(this.likes);
        dest.writeTypedList(this.list);
    }
}
