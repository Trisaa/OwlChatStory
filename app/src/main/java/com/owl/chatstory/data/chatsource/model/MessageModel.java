package com.owl.chatstory.data.chatsource.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lebron on 2017/9/13.
 */

public class MessageModel {
    @SerializedName("actor")
    private String actor;
    @SerializedName("picture")
    private String avatar;
    @SerializedName("location")
    private String location;
    @SerializedName("word")
    private String word;

    private boolean ended;//当前章节是否阅读到底
    private boolean isLastChapter;//是否还有下一章节
    private ChapterModel nextChapterModel;
    private String fictionName;

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
}
