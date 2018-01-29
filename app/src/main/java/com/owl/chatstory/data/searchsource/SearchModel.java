package com.owl.chatstory.data.searchsource;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lebron on 2018/1/29.
 */

public class SearchModel {
    @SerializedName("numFound")
    private int found;
    @SerializedName("start")
    private int start;
    @SerializedName("docs")
    private List<SearchDetailModel> list;

    public int getFound() {
        return found;
    }

    public void setFound(int found) {
        this.found = found;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public List<SearchDetailModel> getList() {
        return list;
    }

    public void setList(List<SearchDetailModel> list) {
        this.list = list;
    }
}
