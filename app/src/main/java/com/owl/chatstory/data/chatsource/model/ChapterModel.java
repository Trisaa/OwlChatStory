package com.owl.chatstory.data.chatsource.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lebron on 2017/10/24.
 */

public class ChapterModel implements Parcelable {
    @SerializedName("id")
    private String chapterId;
    @SerializedName("num")
    private int num;
    @SerializedName("name")
    private String chapterName;
    @SerializedName("vip")
    private int vip;

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.chapterId);
        dest.writeInt(this.num);
        dest.writeString(this.chapterName);
        dest.writeInt(this.vip);
    }

    public ChapterModel() {
    }

    protected ChapterModel(Parcel in) {
        this.chapterId = in.readString();
        this.num = in.readInt();
        this.chapterName = in.readString();
        this.vip = in.readInt();
    }

    public static final Parcelable.Creator<ChapterModel> CREATOR = new Parcelable.Creator<ChapterModel>() {
        @Override
        public ChapterModel createFromParcel(Parcel source) {
            return new ChapterModel(source);
        }

        @Override
        public ChapterModel[] newArray(int size) {
            return new ChapterModel[size];
        }
    };
}
