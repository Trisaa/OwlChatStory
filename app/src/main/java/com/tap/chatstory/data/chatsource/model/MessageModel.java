package com.tap.chatstory.data.chatsource.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lebron on 2017/9/13.
 */

public class MessageModel implements Parcelable {
    public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
        @Override
        public MessageModel createFromParcel(Parcel source) {
            return new MessageModel(source);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };
    @SerializedName("actor")
    private String actor;
    @SerializedName("picture")
    private String avatar;
    @SerializedName("location")
    private String location;
    @SerializedName("word")
    private String word;
    @SerializedName("id")
    private String id;
    private boolean ended;//当前章节是否阅读到底
    private boolean isLastChapter;//是否还有下一章节
    private ChapterModel nextChapterModel;
    private String fictionName;

    public MessageModel() {
    }

    protected MessageModel(Parcel in) {
        this.actor = in.readString();
        this.avatar = in.readString();
        this.location = in.readString();
        this.word = in.readString();
        this.id = in.readString();
        this.ended = in.readByte() != 0;
        this.isLastChapter = in.readByte() != 0;
        this.nextChapterModel = in.readParcelable(ChapterModel.class.getClassLoader());
        this.fictionName = in.readString();
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public boolean isLastChapter() {
        return isLastChapter;
    }

    public void setLastChapter(boolean lastChapter) {
        isLastChapter = lastChapter;
    }

    public ChapterModel getNextChapterModel() {
        return nextChapterModel;
    }

    public void setNextChapterModel(ChapterModel nextChapterModel) {
        this.nextChapterModel = nextChapterModel;
    }

    public String getFictionName() {
        return fictionName;
    }

    public void setFictionName(String fictionName) {
        this.fictionName = fictionName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.actor);
        dest.writeString(this.avatar);
        dest.writeString(this.location);
        dest.writeString(this.word);
        dest.writeString(this.id);
        dest.writeByte(this.ended ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isLastChapter ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.nextChapterModel, flags);
        dest.writeString(this.fictionName);
    }
}
