package com.owl.chatstory.data.chatsource.model;

import com.owl.chatstory.data.usersource.model.UserModel;

import java.util.List;

/**
 * Created by lebron on 2017/11/21.
 */

public class RoleListRequest {
    private String ifiction_id;
    private List<UserModel> list;
    private String type;

    public String getIfiction_id() {
        return ifiction_id;
    }

    public void setIfiction_id(String ifiction_id) {
        this.ifiction_id = ifiction_id;
    }

    public List<UserModel> getList() {
        return list;
    }

    public void setList(List<UserModel> list) {
        this.list = list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
