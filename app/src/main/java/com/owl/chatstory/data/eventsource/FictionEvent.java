package com.owl.chatstory.data.eventsource;

/**
 * Created by lebron on 2018/3/4.
 */

public class FictionEvent {

    private int likes;
    private int stars;

    public FictionEvent(int likes, int stars) {
        this.likes = likes;
        this.stars = stars;
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
}
