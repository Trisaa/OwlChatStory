package com.owl.chatstory.data.chatsource.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lebron on 2017/10/24.
 */

public class ChapterModel implements Parcelable {
    public static final Creator<ChapterModel> CREATOR = new Creator<ChapterModel>() {
        @Override
        public ChapterModel createFromParcel(Parcel source) {
            return new ChapterModel(source);
        }

        @Override
        public ChapterModel[] newArray(int size) {
            return new ChapterModel[size];
        }
    };
    @SerializedName("id")
    private String chapterId;
    @SerializedName("num")
    private int num;
    @SerializedName("name")
    private String chapterName;
    @SerializedName("vip")
    private int vip;
    @SerializedName("updateline")
    private long updateTime;
    @SerializedName("createline")
    private long createTime;
    private int progress = 0;

    public ChapterModel() {
    }

    protected ChapterModel(Parcel in) {
        this.chapterId = in.readString();
        this.num = in.readInt();
        this.chapterName = in.readString();
        this.vip = in.readInt();
        this.updateTime = in.readLong();
        this.createTime = in.readLong();
        this.progress = in.readInt();
    }

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

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
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
        dest.writeLong(this.updateTime);
        dest.writeLong(this.createTime);
        dest.writeInt(this.progress);
    }
}
