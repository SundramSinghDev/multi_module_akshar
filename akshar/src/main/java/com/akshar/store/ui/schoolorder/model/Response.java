package com.akshar.store.ui.schoolorder.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {
    @SerializedName("school_list")
    @Expose
    private List<School> schoolList = null;
    @SerializedName("school_list_count")
    @Expose
    private String schoolListCount;
    @SerializedName("filter")
    @Expose
    private List<Filter> filter = null;

    public String getSchoolListCount() {
        return schoolListCount;
    }

    public void setSchoolListCount(String schoolListCount) {
        this.schoolListCount = schoolListCount;
    }

    public List<School> getSchoolList() {
        return schoolList;
    }

    public void setSchoolList(List<School> schoolList) {
        this.schoolList = schoolList;
    }

    public List<Filter> getFilter() {
        return filter;
    }

    public void setFilter(List<Filter> filter) {
        this.filter = filter;
    }
}
