package com.akshar.store.ui.addresssection.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressItem {
    @SerializedName("firstname")
    @Expose
    String firstname= null;
    @SerializedName("lastname")
    @Expose
    String lastname= null;
    @SerializedName("street")
    @Expose
    String street= null;
    @SerializedName("date")
    @Expose
    String date= null;
    @SerializedName("city")
    @Expose
    String city= null;
    @SerializedName("region_id")
    @Expose
    String region_id= null;
    @SerializedName("region")
    @Expose
    String region= null;

    @SerializedName("country")
    @Expose
    String country= null;
    @SerializedName("pincode")
    @Expose
    String pincode= null;
    @SerializedName("phone")
    @Expose
    String phone= null;
    @SerializedName("address_id")
    @Expose
    String address_id= null;
}
