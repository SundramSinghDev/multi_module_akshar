package com.akshar.store.ui.schoolorder.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDataModel {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("image_name")
    @Expose
    private String imageName;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("Inwishlist")
    @Expose
    private String inwishlist;
    @SerializedName("wishlist_item_id")
    @Expose
    private String wishlistItemId;
    @SerializedName("class_name")
    @Expose
    private String className;
    @SerializedName("review")
    @Expose
    private Integer review;
    @SerializedName("special_price")
    @Expose
    private String specialPrice;
    @SerializedName("regular_price")
    @Expose
    private String regularPrice;

    @SerializedName("from_price")
    @Expose
    private String from_price="";
    @SerializedName("to_price")
    @Expose
    private String to_price="";

    @SerializedName("is_restricted_product")
    @Expose
    private String isRestrictedProduct;
    @SerializedName("offer")
    @Expose
    private Integer offer;


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInwishlist() {
        return inwishlist;
    }

    public void setInwishlist(String inwishlist) {
        this.inwishlist = inwishlist;
    }

    public String getWishlistItemId() {
        return wishlistItemId;
    }

    public void setWishlistItemId(String wishlistItemId) {
        this.wishlistItemId = wishlistItemId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getReview() {
        return review;
    }

    public void setReview(Integer review) {
        this.review = review;
    }

    public String getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(String specialPrice) {
        this.specialPrice = specialPrice;
    }

    public String getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(String regularPrice) {
        this.regularPrice = regularPrice;
    }

    public String getIsRestrictedProduct() {
        return isRestrictedProduct;
    }

    public void setIsRestrictedProduct(String isRestrictedProduct) {
        this.isRestrictedProduct = isRestrictedProduct;
    }

    public Integer getOffer() {
        return offer;
    }

    public void setOffer(Integer offer) {
        this.offer = offer;
    }

    public String getFrom_price() {
        return from_price;
    }

    public void setFrom_price(String from_price) {
        this.from_price = from_price;
    }

    public String getTo_price() {
        return to_price;
    }

    public void setTo_price(String to_price) {
        this.to_price = to_price;
    }
}
