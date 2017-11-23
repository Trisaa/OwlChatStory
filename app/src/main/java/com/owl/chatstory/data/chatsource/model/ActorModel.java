package com.owl.chatstory.data.chatsource.model;

import com.google.gson.annotations.SerializedName;


/**
 * Created by lebron on 2017/11/22.
 */

public class ActorModel {
    public static final int ROLE_FIRST = 0;
    public static final int ROLE_SECOND = 1;
    public static final int ROLE_ASIDE = 2;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("role_type")
    private int role_type;
    @SerializedName("picture")
    private String picture;

    public ActorModel(int role, String name, String icon) {
        this.role_type = role;
        this.name = name;
        this.picture = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRole_type() {
        return role_type;
    }

    public String getRoleTypeStr() {
        switch (role_type) {
            case ROLE_FIRST:
                return "right";
            case ROLE_SECOND:
                return "left";
            case ROLE_ASIDE:
                return "center";
        }
        return "center";
    }

    public void setRole_type(int role_type) {
        this.role_type = role_type;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
