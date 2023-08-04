package com.akshar.store.ui.schoolorder.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SchoolDetailsData {
    @SerializedName("school_view")
    @Expose
    private List<SchoolDetailsDataModel> schoolData = null;
    @SerializedName("Categories")
    @Expose
    private List<CategoriesDataModel> categories = null;

    public List<SchoolDetailsDataModel> getSchoolData() {
        return schoolData;
    }

    public void setSchoolData(List<SchoolDetailsDataModel> schoolData) {
        this.schoolData = schoolData;
    }

    public List<CategoriesDataModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoriesDataModel> categories) {
        this.categories = categories;
    }
}
