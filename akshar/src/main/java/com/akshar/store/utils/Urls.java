package com.akshar.store.utils;

public class Urls {
    //put your base url here
    /* public static final String BASE_URL = "http://demo.cedcommerce.com/magento4/vrushank/";*/
    /*  public static final String BASE_URL = "http://demo.cedcommerce.com/magento2/mage-native/";*/
//    public static final String BASE_URL = "https://myakshar.in/";
//    public static final String BASE_URL = "https://staging.myakshar.com/";
//    public static final String BASE_URL = "https://myakshar.com/";
    public static final String BASE_URL = "https://pronted.in/";
    public static final String TAG = "REpo";

    //Constants
    public static final String PHONE_PATTERN = "^[1-9]\\d{9}$";

    //MainActivity
    public static final String MODULE_LIST = "rest/V1/mobiconnectadvcart/getmodulelist";
    public static final String CART_COUNT = "rest/V1/mobiconnect/checkout/getcartcount";
    public static final String ALl_CATEGORIES = "rest/V1/mobiconnectadvcart/category/getallcategories/";

    //Login & RegistrationSection
    public static final String LOGIN = "rest/V1/mobiconnect/customer/login";
    public static final String SOCIAL_LOGIN = "rest/V1/mobiconnect/sociallogin/create";
    public static final String FORGOT_PASSWORD = "rest/V1/mobiconnect/customer/forgotpassword/";
    public static final String REGISTER = "rest/V1/mobiconnect/customer/register";
    public static final String REGISTER_FIELDS = "rest/V1/mobiconnect/customer/getRequiredFields";

    //HomeSection
    public static final String HOMEPAGE = "rest/V1/mobiconnect/module/gethomepage/1/";
    public static final String DEALS = "rest/V1/mobiconnectdeals/getdealgroup/";
    public static final String FEATURED = "rest/V1/mobiconnect/home/featured/page/";
    public static final String GET_CATEGORIES = "rest/V1/mobiconnect/catalog/subcategories";
    public static final String HOME_NEWTHEME = "rest//V1/getNewHomepage";

    //UserProfileSection
    public static final String UPDATE_PROFILE = "rest/V1/mobiconnect/customer/update";
    public static final String GET_DOWNLOADED = "rest/V1/mobiconnectadvcart/customer/download";

    //AddressSection
    public static final String GET_COUNTRIES = "rest/V1/mobiconnect/module/getcountry/";
    public static final String SAVE_ADDRESS = "rest/V1/mobiconnect/customer/saveaddress";
    public static final String GET_ADDRESS = "rest/V1/mobiconnect/customer/address";
    public static final String DELETE_ADDRESS = "rest/V1/mobiconnect/customer/deleteaddress/";
    public static final String GETREQUIREDFIELDS = "rest/V1/mobiconnect/customer/getRequiredFields/";

    //WishListSection
    public static final String GET_WISHLIST = "rest/V1/mobiconnect/wishlist/getwishlist/";
    public static final String CLEAR_WISHLIST = "rest/V1/mobiconnect/wishlist/clear";
    public static final String REMOVE_WISHLIST = "rest/V1/mobiconnect/wishlist/remove";
    public static final String ADD_WISHLIST = "rest/V1/mobiconnect/wishlist/add/";

    //ProductSection
    public static final String VIEW_PRODUCT = "rest/V1/mobiconnect/catalog/view/";
    public static final String PRODUCT_LIST = "rest/V1/mobiconnect/catalog/products/";
    public static final String ADD_TO_CART = "rest/V1/mobiconnect/checkout/add/";
    public static final String VIEWIMAGE = "rest/V1/mobiconnect/catalog/viewimage/";

    //Productreview
    public static final String PRODUCTREVIEW_LISTING = "rest/V1/mobiconnect/review/product";
    public static final String PRODUCTREVIEW_ADD = "rest/V1/mobiconnect/review/add";
    public static final String GETPRODUCT_RATINGOPTION = "rest/V1/mobiconnect/review/ratingoption";

    //Compare Section
    public static final String ADD_TO_COMPARE = "rest/V1/mobiconnect/addtocompare";
    public static final String COMPARE_URL = "rest/V1/mobiconnect/listcompare";
    public static final String COMPARE_REMOVE = "rest/V1/mobiconnect/remove";
    public static final String COMPARE_ADD = "rest/V1/mobiconnect/checkout/add/";

    //Notification Section
    public static final String NOTIFICATION_LIST = "rest/V1/mobinotifications/listNotification";
    public static final String DEVICE_REGISTER = "rest/V1/mobinotifications/setdevice";

    //OrderSection
    public static final String GET_ORDERS = "rest/V1/mobiconnect/customer/order/";
    public static final String GET_ORDERVIEW = "rest/V1/mobiconnect/customer/orderview/";
    public static final String REORDER = "rest/V1/mobiconnect/customer/reorder";

    //SearchSection
    public static final String GET_AUTOCOMPLETE = "rest/V1/mobiconnectadvcart/search/autocomplete";
    public static final String SEARCH = "rest/V1/mobiconnect/catalog/search";

    //StoreSection
    public static final String GET_STORES = "rest/V1/mobiconnectstore/getlist";
    public static final String SET_STORE = "rest/V1/mobiconnectstore/setstore/";

    //CartSection
    public static final String VIEW_CART = "rest/V1/mobiconnect/checkout/viewcart";
    public static final String EMPTY_CART = "rest/V1/mobiconnect/checkout/emptycart";
    public static final String DELETE_CART = "rest/V1/mobiconnect/checkout/delete";
    public static final String UPDATE_CART = "rest/V1/mobiconnect/checkout/updateqty";
    public static final String APPLY_COUPON = "rest/V1/mobiconnect/checkout/coupon";

    //CheckoutSection
    public static final String SAVE_BILLING_ADDRESS = "rest/V1/mobiconnect/checkout/savebillingshipping";
    public static final String GET_METHODS = "rest/V1/mobiconnect/checkout/getshippingpayament";
    public static final String SAVE_METHODS = "rest/V1/mobiconnect/checkout/saveshippingpayament";
    public static final String SAVE_ORDER = "rest/V1/mobiconnect/checkout/saveorder";
    public static final String ADDITIONAL_INFO = "rest/V1/mobiconnect/checkout/additionalinfo";
    public static final String WEBCHECKOUTURL = "mobiconnectcheckout/onepage/index/customer_id/";
    public static final String WEBCHECKOUT_GUESTURL = "mobiconnectcheckout/onepage/index/method/guest/";

    //SellerSection
    public static final String GET_SELLERS_LIST = "rest/V1/vendorapi/index/item";
    public static final String GET_SELLERS_SHOPS = "rest//V1/vendorapi/vproducts/vshops";
    public static final String GET__SHOP_RATINGOPTION = "rest//V1/vreviewapi/getRatingOption";
    public static final String SUBMIT_VENDORREVIEW = "rest//V1/vreviewapi/addReview";
    public static final String GET_VENDORREVIEW = " rest//V1/vreviewapi/getVendorReview";

    //Custom Work
    public static final String SAVECODE = "rest/V1/custom/saveCode";
    public static final String VENDOR_LIST = "rest/V1/custom/vendorList";
    public static final String SCHOOL_VIEW_OR_SCHOOL_DETAILS = "rest/V1/custom/vendorView";
    public static final String SCHOOL_CONTACT_US_FORM_FIELD = "rest/V1/custom/contactUsForm";
    public static final String VENDOR_PRODUCT_LIST = "rest/V1/custom/productList";

    //mobile login
    public static final String VALIDATE_MOBILE_NUMBER = "rest/V1/mobiconnect/customer/validateNumber";
    public static final String SEND_OTP = "rest/V1/mobiconnect/customer/sendOtp";
    public static final String VERIFY_OTP = "rest/V1/mobiconnect/customer/verifyOtp";

    //for razor pay
    public static final String GET_RAZOR_PAY_ORDER_ID = "rest/V1/razorpay/getrazorpayorderid";
}
