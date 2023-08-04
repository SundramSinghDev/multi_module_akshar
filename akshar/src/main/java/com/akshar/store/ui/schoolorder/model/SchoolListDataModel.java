package com.akshar.store.ui.schoolorder.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SchoolListDataModel {

    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName(("name"))
    private String name;

    @Expose
    @SerializedName("location")
    private String location;

    public SchoolListDataModel(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
