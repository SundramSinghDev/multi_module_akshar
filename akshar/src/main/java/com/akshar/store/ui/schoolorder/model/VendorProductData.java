package com.akshar.store.ui.schoolorder.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VendorProductData {

    @SerializedName("products")
    @Expose
    private List<ProductDataModel> products = null;

    public List<ProductDataModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDataModel> products) {
        this.products = products;
    }
}
