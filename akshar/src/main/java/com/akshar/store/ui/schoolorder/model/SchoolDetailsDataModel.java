package com.akshar.store.ui.schoolorder.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SchoolDetailsDataModel {
    @SerializedName("shop_title")
    @Expose
    private String shopTitle;
    @SerializedName("banner_pic")
    @Expose
    private String bannerPic;
    @SerializedName("company_locality")
    @Expose
    private String companyLocality;
    @SerializedName("logo_pic")
    @Expose
    private String logo_pic;
    @SerializedName("about_us")
    @Expose
    private List<String> aboutUs = null;
    @SerializedName("seller_id")
    @Expose
    private String seller_id;

    @SerializedName("sub_seller_id")
    @Expose
    private String sub_seller_id;

    @SerializedName("is_seller")
    @Expose
    private String is_seller;

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
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

    public String getShopTitle() {
        return shopTitle;
    }

    public void setShopTitle(String shopTitle) {
        this.shopTitle = shopTitle;
    }

    public String getBannerPic() {
        return bannerPic;
    }

    public void setBannerPic(String bannerPic) {
        this.bannerPic = bannerPic;
    }

    public String getCompanyLocality() {
        return companyLocality;
    }

    public void setCompanyLocality(String companyLocality) {
        this.companyLocality = companyLocality;
    }

    public String getLogo() {
        return logo_pic;
    }

    public void setLogo(String logo) {
        this.logo_pic = logo;
    }

    public List<String> getAboutUs() {
        return aboutUs;
    }

    public void setAboutUs(List<String> aboutUs) {
        this.aboutUs = aboutUs;
    }
}
