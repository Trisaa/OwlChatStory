package com.owl.chatstory.common.util.network.request;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lebron on 2017/9/17.
 */

public class FictionListRequest implements Serializable {
    private String categoryId;
    private int page;
    private int count;
    private String token;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map getQueryMap() {
        Map map = new HashMap();
        if (!TextUtils.isEmpty(categoryId)) {
            map.put("category", categoryId);
        }
        map.put("page", page);
        map.put("count", count);
        if (!TextUtils.isEmpty(token)) {
            map.put("token", token);
        }
        return map;
    }
}
