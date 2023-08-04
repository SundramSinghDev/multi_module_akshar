package com.akshar.store.ui.productsection.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("special_price")
    @Expose
    private String specialPrice;
    @SerializedName("regular_price")
    @Expose
    private String regularPrice;
    @SerializedName("Inwishlist")
    @Expose
    private String inwishlist;
    @SerializedName("wishlist_item_id")
    @Expose
    private Integer wishlistItemId;
    @SerializedName("stock_status")
    @Expose
    private Boolean stockStatus;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("review")
    @Expose
    private Double review;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
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

    public String getInwishlist() {
        return inwishlist;
    }

    public void setInwishlist(String inwishlist) {
        this.inwishlist = inwishlist;
    }

    public Integer getWishlistItemId() {
        return wishlistItemId;
    }

    public void setWishlistItemId(Integer wishlistItemId) {
        this.wishlistItemId = wishlistItemId;
    }

    public Boolean getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(Boolean stockStatus) {
        this.stockStatus = stockStatus;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Double getReview() {
        return review;
    }

    public void setReview(Double review) {
        this.review = review;
    }

    public Integer getOffer() {
        return offer;
    }

    public void setOffer(Integer offer) {
        this.offer = offer;
    }
}
