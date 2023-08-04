package com.akshar.store.rest;

import com.google.gson.JsonObject;
import com.akshar.store.utils.Urls;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiInterface {

    @GET
    Call<Object> checkValidity(@Url String url);

    @POST(Urls.MODULE_LIST)
    Call<Object> getModuleList(@Body JsonObject postData);

    @POST(Urls.CART_COUNT)
    Call<Object> getCartCount(@Body JsonObject postData);

    @POST(Urls.ALl_CATEGORIES)
    Call<Object> getAllCategories(@Body JsonObject postData);

    @POST(Urls.LOGIN)
    Call<Object> getLoginData(@Body JsonObject postData);

    @POST(Urls.COMPARE_REMOVE)
    Call<Object> removefrom_Compare(@Header("hashkey") String hashkey,
                                    @Body JsonObject postData);

    @POST(Urls.SOCIAL_LOGIN)
    Call<Object> getSocialLoginData(@Body JsonObject postData);

    @POST(Urls.FORGOT_PASSWORD)
    Call<Object> getForgotPassData(@Body JsonObject postData);

    @POST(Urls.REGISTER)
    Call<Object> getRegisterData(@Body JsonObject postData);

    @GET(Urls.REGISTER_FIELDS)
    Call<Object> getRegisterFields();

    /*@GET
    Call<Object> getProfileFields(@Url String url);*/

    @POST(Urls.UPDATE_PROFILE)
    Call<Object> getProfileUpdate(@Header("hashkey") String hashkey,
                                  @Body JsonObject postData);

    @POST(Urls.HOME_NEWTHEME)
    Call<Object> getnewthemehomepage(@Body JsonObject postData);

    @POST(Urls.COMPARE_URL)
    Call<Object> getcomparelist(@Header("hashkey") String hashkey,
                                @Body JsonObject postData);

    @GET("rest/V1/mobiconnect/module/gethomepage/1/{store_id}")
    Call<Object> getBanners(@Path("store_id") String store_id);

    @GET("rest/V1/mobiconnectdeals/getdealgroup/{store_id}")
    Call<Object> getDeals(@Path("store_id") String store_id);


    @GET("rest/V1/mobiconnect/home/featured/page/{page}/store/{store_id}")
    Call<Object> getFeatured(@Path("page") String page,
                             @Path("store_id") String store_id);


    @GET(Urls.GET_COUNTRIES + "{countryid}")
    Call<Object> getState(@Path("countryid") String countryid);

    @GET(Urls.GET_COUNTRIES)
    Call<Object> getcountry();

    @GET(Urls.GET__SHOP_RATINGOPTION)
    Call<Object> getratingoption();

    @GET(Urls.GETPRODUCT_RATINGOPTION)
    Call<Object> getproductratingoption();

    @POST(Urls.GET_CATEGORIES)
    Call<Object> getCategories(@Body JsonObject postData);

    /*@GET
    Call<Object> getWebCheckout(@Url String url);*/

    @POST(Urls.ADD_TO_CART)
    Call<Object> addToCart(@Body JsonObject postData);

    @POST(Urls.PRODUCTREVIEW_ADD)
    Call<Object> addproductreview(@Body JsonObject postData);

    @POST(Urls.PRODUCTREVIEW_LISTING)
    Call<Object> productreviewlisting(@Body JsonObject postData);

    @POST(Urls.ADD_TO_COMPARE)
    Call<Object> addToCompare(@Header("hashkey") String hashkey,
                              @Body JsonObject postData);

    @POST(Urls.VIEWIMAGE)
    Call<Object> viewimage(@Body JsonObject postData);

    @POST(Urls.VIEW_CART)
    Call<Object> getViewCart(@Body JsonObject postData);

    @POST(Urls.DEVICE_REGISTER)
    Call<Object> registerdevicetoserver(@Body JsonObject postData);

    @POST(Urls.EMPTY_CART)
    Call<Object> emptyCart(@Body JsonObject postData);

    @POST(Urls.DELETE_CART)
    Call<Object> deleteFromCart(@Body JsonObject postData);

    @POST(Urls.APPLY_COUPON)
    Call<Object> applycoupon(@Body JsonObject postData);

    @POST(Urls.UPDATE_CART)
    Call<Object> updateCart(@Body JsonObject postData);

    @POST(Urls.SAVECODE)
    Call<Object> saveshoolcode(@Body JsonObject postData);

    @POST(Urls.VENDOR_LIST)
    Call<Object> vendorList(@Body JsonObject postData);

    @POST(Urls.SCHOOL_VIEW_OR_SCHOOL_DETAILS)
    Call<Object> schoolDetailsOrView(@Body JsonObject postData);

    @POST(Urls.VENDOR_PRODUCT_LIST)
    Call<Object> getVendorProducts(@Body JsonObject postData);

    @GET(Urls.SCHOOL_CONTACT_US_FORM_FIELD)
    Call<Object> getSchoolContactFormField();

    @POST(Urls.SAVE_ADDRESS)
    Call<Object> saveAddress(@Header("hashkey") String hashkey,
                             @Body JsonObject postData);

    @POST(Urls.DELETE_ADDRESS)
    Call<Object> deleteAddress(@Header("hashkey") String hashkey,
                               @Body JsonObject postData);

    @POST(Urls.GET_ADDRESS)
    Call<Object> getAddressList(@Header("hashkey") String hashkey,
                                @Body JsonObject postData);

    @POST(Urls.GET_WISHLIST)
    Call<Object> getWishList(@Header("hashkey") String hashkey,
                             @Body JsonObject postData);

    @POST(Urls.CLEAR_WISHLIST)
    Call<Object> clearWishList(@Header("hashkey") String hashkey,
                               @Body JsonObject postData);

    @POST(Urls.REMOVE_WISHLIST)
    Call<Object> removeWishList(@Header("hashkey") String hashkey,
                                @Body JsonObject postData);

    @POST(Urls.NOTIFICATION_LIST)
    Call<Object> getnotificationlist(@Header("hashkey") String hashkey,
                                     @Body JsonObject postData);

    @POST(Urls.ADD_WISHLIST)
    Call<Object> addToWishList(@Header("hashkey") String hashkey,
                               @Body JsonObject postData);

    @POST(Urls.GET_ORDERS)
    Call<Object> getOrders(@Header("hashkey") String hashkey,
                           @Body JsonObject postData);

    @POST(Urls.GET_ORDERVIEW)
    Call<Object> getOrderView(@Header("hashkey") String hashkey,
                              @Body JsonObject postData);

    @POST(Urls.REORDER)
    Call<Object> getReorder(@Header("hashkey") String hashkey,
                            @Body JsonObject postData);

    @POST(Urls.GET_AUTOCOMPLETE)
    Call<Object> getAutoComplete(@Body JsonObject postData);

    @POST(Urls.SEARCH)
    Call<Object> getSearchList(@Body JsonObject postData);

    @POST(Urls.GET_DOWNLOADED)
    Call<Object> getDownloadProducts(@Header("hashkey") String hashkey,
                                     @Body JsonObject postData);

    @POST(Urls.VIEW_PRODUCT)
    Call<Object> getProductView(@Body JsonObject postData);

    @POST(Urls.PRODUCT_LIST)
    Call<Object> getProductList(@Body JsonObject postData);

    @POST(Urls.GET_SELLERS_LIST)
    Call<Object> getsellersList(@Body JsonObject postData);

    @POST(Urls.SUBMIT_VENDORREVIEW)
    Call<Object> submitvendorreviewdata(@Body JsonObject postData);

    @POST(Urls.GET_VENDORREVIEW)
    Call<Object> getVendorReview(@Body JsonObject postData);

    @POST(Urls.GET_SELLERS_SHOPS)
    Call<Object> getsellersShops(@Body JsonObject postData);

    @POST(Urls.SAVE_BILLING_ADDRESS)
    Call<Object> saveBillingShipping(@Body JsonObject postData);

    @POST(Urls.GET_METHODS)
    Call<Object> getShippingPayment(@Body JsonObject postData);

    @POST(Urls.SAVE_METHODS)
    Call<Object> saveShippingPayment(@Body JsonObject postData);

    @POST(Urls.SAVE_ORDER)
    Call<Object> saveOrder(@Body JsonObject postData);

    @POST(Urls.GET_RAZOR_PAY_ORDER_ID)
    Call<Object> getRazorPayOrderId(@Body JsonObject postData);

    @POST(Urls.ADDITIONAL_INFO)
    Call<Object> additionalInfo(@Body JsonObject postData);

    @GET
    Call<Object> getDataFromUrl(@Url String url);

    @GET(Urls.GETREQUIREDFIELDS)
    Call<Object> getrequiredfields();

    @GET(Urls.SET_STORE + "{store_id}")
    Call<Object> setstore(@Path("store_id") String store_id);

    //    mobile login
    @POST(Urls.VALIDATE_MOBILE_NUMBER)
    Call<Object> onValidateNumber(@Body JsonObject postData);

    @POST(Urls.SEND_OTP)
    Call<Object> onSendOTP(@Body JsonObject postData);

    @POST(Urls.VERIFY_OTP)
    Call<Object> onVerifyOTP(@Body JsonObject postData);
}
