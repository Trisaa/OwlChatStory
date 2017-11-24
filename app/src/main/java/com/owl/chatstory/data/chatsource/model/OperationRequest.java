package com.owl.chatstory.data.chatsource.model;

/**
 * Created by lebron on 2017/11/23.
 */

public class OperationRequest {
    private String token;
    private String ifiction_id;
    private String chapter_id;
    private String language;
    private String op;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIfiction_id() {
        return ifiction_id;
    }

    public void setIfiction_id(String ifiction_id) {
        this.ifiction_id = ifiction_id;
    }

    public String getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(String chapter_id) {
        this.chapter_id = chapter_id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }
}
