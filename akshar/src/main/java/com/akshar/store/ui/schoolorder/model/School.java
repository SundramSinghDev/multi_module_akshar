package com.akshar.store.ui.schoolorder.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class School {
    @SerializedName("shop_title")
    @Expose
    private String shopTitle;
    @SerializedName("logo_pic")
    @Expose
    private String logoPic;
    @SerializedName("company_locality")
    @Expose
    private String companyLocality;

    @SerializedName("seller_id")
    @Expose
    private String sellerId;

    @SerializedName("banner_pic")
    @Expose
    private String bannerPic;

    @SerializedName("sub_seller_id")
    @Expose
    private String sub_seller_id;

    @SerializedName("is_seller")
    @Expose
    private String is_seller;

    public String getShopTitle() {
        return shopTitle;
    }

    public void setShopTitle(String shopTitle) {
        this.shopTitle = shopTitle;
    }

    public String getLogoPic() {
        return logoPic;
    }

    public void setLogoPic(String logoPic) {
        this.logoPic = logoPic;
    }

    public String getCompanyLocality() {
        return companyLocality;
    }

    public void setCompanyLocality(String companyLocality) {
        this.companyLocality = companyLocality;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getBannerPic() {
        return bannerPic;
    }

    public void setBannerPic(String bannerPic) {
        this.bannerPic = bannerPic;
    }

    public String getSub_seller_id() {
        return sub_seller_id;
    }

    public void setSub_seller_id(String sub_seller_id) {
        this.sub_seller_id = sub_seller_id;
    }

    public String getIs_seller() {
        return is_seller;
    }

    public void setIs_seller(String is_seller) {
        this.is_seller = is_seller;
    }
}
