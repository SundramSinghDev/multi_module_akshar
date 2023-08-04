package com.akshar.store.ui.orderssection.datamodel;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderedItem {
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("shop_title")
    @Expose
    private String shopTitle;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("quantity")
    @Expose
    private String quantity;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShopTitle() {
        return shopTitle;
    }

    public void setShopTitle(String shopTitle) {
        this.shopTitle = shopTitle;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getQuantity() {
        try {
            Integer qty = new GsonBuilder().create().fromJson(quantity, Integer.class);
            quantity = "" + qty;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
