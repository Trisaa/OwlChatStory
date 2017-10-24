package com.owl.chatstory.data.chatsource.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lebron on 2017/10/24.
 */

public class ChapterModel {
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
}
