package com.akshar.store.ui.orderssection.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order_Item_Model {
    @SerializedName("order_id")
    @Expose
    private String order_id;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("total_amount")
    @Expose
    private String total_amount;

    @SerializedName("ship_to")
    @Expose
    private String ship_to;

    @SerializedName("order_status")
    @Expose
    private String order_status;

    @SerializedName("number")
    @Expose
    private String number;

    @SerializedName("ordered_items")
    @Expose
    private List<OrderedItem> orderedItems = null;

    public String getNumber() {
        return number;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getDate() {
        return date;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public String getShip_to() {
        return ship_to;
    }

    public String getOrder_status() {
        return order_status;
    }

    public List<OrderedItem> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<OrderedItem> orderedItems) {
        this.orderedItems = orderedItems;
    }
}
