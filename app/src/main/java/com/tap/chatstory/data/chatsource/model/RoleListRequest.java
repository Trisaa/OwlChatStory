package com.tap.chatstory.data.chatsource.model;


import java.io.Serializable;
import java.util.List;

/**
 * Created by lebron on 2017/11/21.
 */

public class RoleListRequest implements Serializable {
    private String ifiction_id;
    private List<ActorModel> actors;
    private String language;
    private String token;

    public String getIfiction_id() {
        return ifiction_id;
    }

    public void setIfiction_id(String ifiction_id) {
        this.ifiction_id = ifiction_id;
    }

    public List<ActorModel> getList() {
        return actors;
    }

    public void setList(List<ActorModel> list) {
        this.actors = list;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
