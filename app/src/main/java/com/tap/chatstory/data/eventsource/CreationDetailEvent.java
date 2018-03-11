package com.tap.chatstory.data.eventsource;

/**
 * Created by lebron on 2017/11/23.
 */

public class CreationDetailEvent {
    private boolean reload;


    public CreationDetailEvent(boolean reload) {
        this.reload = reload;
    }

    public boolean isReload() {
        return reload;
    }

    public void setReload(boolean reload) {
        this.reload = reload;
    }
}
