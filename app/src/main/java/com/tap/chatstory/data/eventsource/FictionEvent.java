package com.tap.chatstory.data.eventsource;

import com.tap.chatstory.data.chatsource.model.FictionStatusResponse;

/**
 * Created by lebron on 2018/3/4.
 */

public class FictionEvent {
    private FictionStatusResponse response;
    private int likes;
    private int stars;

    public FictionEvent(int likes, int stars, FictionStatusResponse response) {
        this.likes = likes;
        this.stars = stars;
        this.response = response;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public FictionStatusResponse getResponse() {
        return response;
    }

    public void setResponse(FictionStatusResponse response) {
        this.response = response;
    }
}
