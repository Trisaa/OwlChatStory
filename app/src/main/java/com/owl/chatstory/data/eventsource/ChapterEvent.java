package com.owl.chatstory.data.eventsource;

/**
 * Created by lebron on 2017/10/30.
 */

public class ChapterEvent {
    private String chapterId;
    private String progress;

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }
}
