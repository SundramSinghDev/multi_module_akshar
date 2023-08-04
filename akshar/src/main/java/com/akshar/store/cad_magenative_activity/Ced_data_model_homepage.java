package com.akshar.store.cad_magenative_activity;

/**
 * Created by cedcoss on 24/1/17.
 */
public class Ced_data_model_homepage {
    String type;
    String product_image;
    String product_id;
    String product_name;
    String special_price;
    String regular_price;
    String Inwishlist;
    String wishlist_item_id;
    String offer;
    String review;

    public Ced_data_model_homepage(String type, String product_image, String product_id, String product_name, String special_price, String regular_price, String Inwishlist, String wishlist_item_id, String offer, String review) {

        this.type = type;

        this.product_image = product_image;
        this.product_id = product_id;
        this.product_name = product_name;
        this.special_price = special_price;
        this.regular_price = regular_price;
        this.Inwishlist = Inwishlist;
        this.wishlist_item_id = wishlist_item_id;
        this.offer = offer;
        this.review = review;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getSpecial_price() {
        return special_price;
    }

    public void setSpecial_price(String special_price) {
        this.special_price = special_price;
    }

    public String getRegular_price() {
        return regular_price;
    }

    public void setRegular_price(String regular_price) {
        this.regular_price = regular_price;
    }

    public String getInwishlist() {
        return Inwishlist;
    }

    public void setInwishlist(String inwishlist) {
        Inwishlist = inwishlist;
    }

    public String getWishlist_item_id() {
        return wishlist_item_id;
    }

    public void setWishlist_item_id(String wishlist_item_id) {
        this.wishlist_item_id = wishlist_item_id;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }


}
