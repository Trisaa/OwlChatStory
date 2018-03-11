package com.tap.chatstory.data.usersource.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lebron on 2017/9/20.
 */

public class UserResponse {
    @SerializedName("token")
    private String token;
    @SerializedName("userinfo")
    private UserModel userModel;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}
