package com.akshar.store.ui.checkoutsection.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.akshar.store.Ced_MageNative_SharedPrefrence.Ced_SessionManagement_login;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.ActivityCustomOrderSummaryBinding;
import com.akshar.store.databinding.BottomAddressSheetSingleItemBinding;
import com.akshar.store.databinding.MagenativeActivityNoModuleBinding;
import com.akshar.store.databinding.MagenativeEmptyCartBinding;
import com.akshar.store.databinding.PriceLayoutForCustomOrderSummaryPageBinding;
import com.akshar.store.rest.ApiResponse;
import com.akshar.store.ui.addresssection.activity.Ced_AddAddress;
import com.akshar.store.ui.addresssection.activity.Ced_Addressbook;
import com.akshar.store.ui.addresssection.viewmodel.AddressViewModel;
import com.akshar.store.ui.cartsection.viewmodel.CartViewModel;
import com.akshar.store.ui.checkoutsection.OrderSummaryAdapter;
import com.akshar.store.ui.checkoutsection.viewmodel.CheckoutViewModel;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.akshar.store.ui.orderssection.activity.Ced_ViewOrder;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonObject;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CustomOrderSummaryActivity extends Ced_NavigationActivity implements PaymentResultWithDataListener {

    @Inject
    ViewModelFactory viewModelFactory;
    CartViewModel cartViewModel;
    CheckoutViewModel checkoutViewModel;
    //TODO if isHavingdownloadable is true then hide the shipping/delivery view
    static boolean isHavingdownloadable = false;
    HashMap<String, HashMap<String, String>> finalconfigdata;
    HashMap<String, ArrayList<String>> bundledata;
    HashMap<String, String> configdata;
    ArrayList<HashMap<String, String>> ProductData;
    ArrayList<String> IDS;
    //for view cart
    public static final String KEY_ID = "product_id";
    public static final String KEY_NAME = "product-name";
    public static final String KEY_Image = "product_image";
    public static final String KEY_Price = "sub-total";
    public static final String Key_Quantity = "quantity";
    public static final String ITEMID = "item_id";
    //for view cart end
    //for address list
    static final String KEY_FIRSTNAME = "firstname";
    static final String KEY_LASTNAME = "lastname";
    static final String KEY_CITY = "city";
    static final String KEY_STATE = "region";
    static final String KEY_PHONE = "phone";
    static final String KEY_STREET = "street";
    static final String KEY_COUNTRY = "country";
    static final String KEY_PINCODE = "pincode";
    static final String KEY_ADDRESS_ID = "address_id";
    //for address list end
    ActivityCustomOrderSummaryBinding binding;
    OrderSummaryAdapter listViewAdapter;
    AddressViewModel addressViewModel;
    JsonObject addressBookParams;
    BottomSheetBehavior<LinearLayout> addressListBehavior;
    HashMap<String, String> addressHashMap, addressMobile;
    String allowed_guest_checkout = "", grandTotalForGetRazorPayOrderId = "";

    //Razor Pay Related Variable
    String email = "";
    String paymentcode = "";
    String order_id = "";
    boolean load = false;
    String grandTotal;
    String grand_total;
    String razorpay_order_id;
    //Razor Pay Related Variable
    JsonObject additionalInfoParam;
    boolean isOldFlow = false;
    Checkout checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackButtonWithTitle(getResources().getString(R.string.order_summary));
        checkout = new Checkout();
        hidebottombar();
        isHavingdownloadable = getIntent().getBooleanExtra("ishavingdownloadableonly", false);
        cartViewModel = new ViewModelProvider(CustomOrderSummaryActivity.this, viewModelFactory).get(CartViewModel.class);
        addressViewModel = new ViewModelProvider(CustomOrderSummaryActivity.this, viewModelFactory).get(AddressViewModel.class);
        checkoutViewModel = new ViewModelProvider(CustomOrderSummaryActivity.this, viewModelFactory).get(CheckoutViewModel.class);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_custom_order_summary, content, true);
        binding.orderdItemRv.setLayoutManager(new LinearLayoutManager(this));
        init();
        onGetAddressBook();

        addressListBehavior = BottomSheetBehavior.from(binding.addressBottomSheetLayout.addressListParent);
        //school list drop down click listener end
        setbottomsheet(addressListBehavior, binding.view);
        addressListBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        //TODO will handle the close button of the school bottom sheet
        binding.addressBottomSheetLayout.closeIv.setOnClickListener(view -> {
            if (addressListBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                addressListBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                addressListBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        binding.MageNativeAddressList.setOnClickListener(view1 -> {
            try {
                binding.addressBottomSheetLayout.heading.setText(R.string.select_shipping_address);
                if (addressHashMap != null) {
                    onCreateAndBindDataToAddressView("shipping");
                    if (addressListBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        addressListBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    } else {
                        addressListBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        binding.addressBottomSheetLayout.addressBtn.setOnClickListener(view -> {
            addressListBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            Intent add_address_intent = new Intent(CustomOrderSummaryActivity.this, Ced_AddAddress.class);
            {
                add_address_intent.putExtra("fromcheckout", true);
            }
            startActivityIfNeeded(add_address_intent, FROM_CUSTOM_ORDER_SUMMARY);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        });

        binding.addNewAddress.setOnClickListener(view -> binding.addressBottomSheetLayout.addressBtn.performClick());

        if (isHavingdownloadable) {
            binding.billingShippingSame.setVisibility(View.GONE);
            binding.shippingAddressDropDown.setVisibility(View.GONE);
            binding.shppingMethodHeaderTV.setVisibility(View.GONE);
        }

        //-------------------------------------------------------- Apply and Remove Coupon starts
        binding.MageNativeApplycoupan.setOnClickListener(view12 -> {
            binding.MageNativeApplycoupantag.requestFocus();
            if (binding.MageNativeApplycoupan.getText().toString().equals(getResources().getString(R.string.apply))) {
                if (binding.MageNativeApplycoupantag.getText().toString().isEmpty()) {
                    binding.MageNativeApplycoupantag.setError(getResources().getString(R.string.empty));
                } else {
                    JsonObject coupon1 = new JsonObject();
                    if (session.isLoggedIn()) {
                        coupon1.addProperty("customer_id", session.getCustomerid());
                        coupon1.addProperty("coupon_code", binding.MageNativeApplycoupantag.getText().toString());
                    } else {
                        coupon1.addProperty("cart_id", cedSessionManagement.getCartId());
                        coupon1.addProperty("coupon_code", binding.MageNativeApplycoupantag.getText().toString());
                    }
                    cartViewModel.applycoupon(CustomOrderSummaryActivity.this, coupon1).observe(CustomOrderSummaryActivity.this, CustomOrderSummaryActivity.this::CouponResponse);
                }

            } else {
                if (binding.MageNativeApplycoupan.getText().toString().equals(getResources().getString(R.string.removecoupon))) {
                    JsonObject coupon1 = new JsonObject();
                    if (session.isLoggedIn()) {
                        coupon1.addProperty("customer_id", session.getCustomerid());
                    } else {
                        coupon1.addProperty("cart_id", cedSessionManagement.getCartId());
                    }
                    coupon1.addProperty("remove", "1");
                    cartViewModel.applycoupon(CustomOrderSummaryActivity.this, coupon1).observe(CustomOrderSummaryActivity.this, CustomOrderSummaryActivity.this::CouponResponse);
                }
            }
        });
        //-------------------------------------------------------- Apply and Remove Coupon end
    }

    private void init() {
        try {
            IDS = new ArrayList<>();
            bundledata = new HashMap<>();
            configdata = new HashMap<>();
            finalconfigdata = new HashMap<>();
            ProductData = new ArrayList<>();
            set_regular_font_fortext(binding.deliveryAddressTV);
            set_regular_font_fortext(binding.deliveryAddressMobileTV);
            set_regular_font_fortext(binding.changeAddressTV);
            set_regular_font_fortext(binding.noShippingMethodMsg);
            set_regular_font_fortext(binding.noPaymentMethodMsg);

            set_bold_font_fortext(binding.deliveryHeaderTV);
            set_bold_font_fortext(binding.totalItemCountTV);
            set_bold_font_fortext(binding.shppingMethodHeaderTV);
            set_bold_font_fortext(binding.deliveryAddressMobileLabelTV);
            set_bold_font_fortext(binding.priceDetailsTV);
            set_bold_font_fortext(binding.total);
            set_bold_font_fortext(binding.grandTotalNew);

            set_regular_font_forRadio(binding.shippingMethod);
            set_regular_font_forRadio(binding.paymentMethod);

            set_regular_font_forButton(binding.MageNativeCheckout);

            set_regular_font_forCheckBox(binding.billingShippingSame);

            set_regular_font_fortext(binding.MageNativeApplycoupan);
            set_regular_font_forEdittext(binding.MageNativeApplycoupantag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO view cart request and response view starts
    public void cartRequest() {
        try {
            JsonObject cartlist = new JsonObject();
            if (session.isLoggedIn()) {
                cartlist.addProperty("customer_id", session.getCustomerid());
                if (cedSessionManagement.getStoreId() != null) {
                    cartlist.addProperty("store_id", cedSessionManagement.getStoreId());
                }
                if (cedSessionManagement.getCartId() != null) {
                    cartlist.addProperty("cart_id", cedSessionManagement.getCartId());
                }
                ProductData = new ArrayList<>();
                // request();
                proceed(cartlist);
            } else {
                if (cedSessionManagement.getCartId() != null) {
                    cartlist.addProperty("cart_id", cedSessionManagement.getCartId());
                    if (cedSessionManagement.getStoreId() != null) {
                        cartlist.addProperty("store_id", cedSessionManagement.getStoreId());
                    }
                    ProductData = new ArrayList<>();
                    // request();
                    proceed(cartlist);
                } else {
                    MagenativeEmptyCartBinding magenativeEmptyCartBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_empty_cart, content, true);
                    magenativeEmptyCartBinding.conti.setOnClickListener(v -> {
                        Intent intent = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("exceptfromhome", "true");
                        startActivity(intent);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        finish();
                    });
                }
            }
        } catch (Exception e) {
            Intent intent = new Intent(getApplicationContext(), Ced_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    private void proceed(JsonObject cartList) {
        cartViewModel.viewCart(CustomOrderSummaryActivity.this, cartList).observe(CustomOrderSummaryActivity.this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(Objects.requireNonNull(apiResponse.data));
                        if (object.has("header") && object.getString("header").equals("false")) {
                            MagenativeActivityNoModuleBinding magenativeActivityNoModuleBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_activity_un_authourized_request_error, content, true);
                            magenativeActivityNoModuleBinding.conti.setOnClickListener(v -> {
                                Intent refresh = new Intent(CustomOrderSummaryActivity.this, CustomOrderSummaryActivity.class);
                                startActivity(refresh);
                                finish();
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            });
                        } else {
                            if (object.has("success")) {
                                String status = object.getString("success");
                                if (status.equals("false")) {
                                    Ced_MainActivity.latestcartcount = "0";
                                    changeCartCount();
                                    MagenativeEmptyCartBinding magenativeEmptyCartBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_empty_cart, content, true);
                                    magenativeEmptyCartBinding.conti.setOnClickListener(v -> {
                                        Intent intent = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                                        intent.putExtra("exceptfromhome", "true");
                                        startActivity(intent);
                                        finishAffinity();
                                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                        finish();
                                    });
                                    if (object.has("message")) {
                                        showmsg(object.getString("message"));
                                    }

                                }
                            } else {
                                applyCartData(apiResponse.data);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case ERROR:
                    Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                    showmsg(getResources().getString(R.string.errorString));
                    break;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void applyCartData(String responseStr) {
        try {
            binding.main.setVisibility(View.VISIBLE);
            JSONObject jsonObj = new JSONObject(responseStr);
            JSONObject data = jsonObj.getJSONObject("data");
            if (data.has("razorpay_api_key")) {
                checkout.setKeyID(data.getString("razorpay_api_key"));
                Checkout.preload(getApplicationContext());
            }else{
                //TODO
            }
            grandTotalForGetRazorPayOrderId = data.has("order_total") ? data.getString("order_total") : "";
            String items_count = String.valueOf(data.getInt("items_count"));
            Ced_MainActivity.latestcartcount = items_count;
            changeCartCount();
            binding.totalItemCountTV.setText(items_count.toUpperCase() + " Items");

            if (data.has("cart_error")) {
                String cart_error = data.getString("cart_error");
                if (!cart_error.equals("") && cart_error != null) {
                    binding.cartErrorTxt.setText(cart_error);
                    binding.errorLayout.setVisibility(View.VISIBLE);
                }
            }
            else {
                binding.errorLayout.setVisibility(View.GONE);
            }

            if (data.has("segments")) {
                JSONArray segments = data.getJSONArray("segments");
                if (binding.priceLayout.getChildCount() > 0)
                    binding.priceLayout.removeAllViews();
                for (int k = 0; k < segments.length(); k++) {
                    PriceLayoutForCustomOrderSummaryPageBinding footerPriceLayoutBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.price_layout_for_custom_order_summary_page, null, false);
                    set_regular_font_fortext(footerPriceLayoutBinding.key);
                    set_regular_font_fortext(footerPriceLayoutBinding.value);
                    JSONObject object = segments.getJSONObject(k);
                    if (object.getString("label").contains("Grand Total") || object.getString("label").equalsIgnoreCase("Grand Total")) {
                        //that is to show the grand total at the bottom of the price sections
                        binding.grandTotalNew.setText(object.getString("value"));
                        binding.total.setText(object.getString("label"));
                    } else {
                        footerPriceLayoutBinding.key.setText(object.getString("label"));
                        footerPriceLayoutBinding.value.setText(object.getString("value"));
                        binding.priceLayout.addView(footerPriceLayoutBinding.getRoot());
                    }
                }
            }

            JSONArray products = data.getJSONArray("products");
            for (int i = 0; i < products.length(); i++) {
                JSONObject c = products.getJSONObject(i);
                String id = c.getString(KEY_ID);
                // IDS.add(id);
                String item_id = c.getString(ITEMID);
                String name = c.getString(KEY_NAME);
                String image = c.getString(KEY_Image);
                String price = c.getString(KEY_Price);
                int Quantity = c.getInt(Key_Quantity);
                String product_type = c.getString("product_type");
                HashMap<String, String> productdata = new HashMap<>();
                productdata.put(KEY_ID, id);
                productdata.put(KEY_NAME, name);
                productdata.put(KEY_Image, image);
                productdata.put(ITEMID, item_id);
                productdata.put(KEY_Price, price);
                productdata.put(Key_Quantity, String.valueOf(Quantity));
                productdata.put("Product_type", product_type);
                if (c.has("item_error")) {
                    JSONArray item_error = c.getJSONArray("item_error");
                    productdata.put("item_error", item_error.toString());
                }
                if (product_type.equals("bundle")) {
                    JSONObject object = c.getJSONObject("bundle_options");
                    JSONArray option_names = object.names();
                    JSONArray option_values = object.toJSONArray(option_names);
                    ArrayList<String> data1 = new ArrayList<>();
                    for (int option = 0; option < Objects.requireNonNull(option_values).length(); option++) {
                        JSONObject jsonObject = option_values.getJSONObject(option);
                        String label = jsonObject.getString("label");
                        JSONArray array = jsonObject.getJSONArray("value");
                        for (int value = 0; value < array.length(); value++) {
                            JSONObject object1 = array.getJSONObject(value);
                            String title = object1.getString("title");
                            String qty = object1.getString("qty");
                            String optionprice = object1.getString("price");
                            String datatoshow = label + "#" + title + "#" + qty + "#" + optionprice;
                            data1.add(datatoshow);
                        }
                    }
                    bundledata.put(item_id, data1);
                }
                if (product_type.equals("configurable")) {
                    JSONArray object = c.getJSONArray("options_selected");
                    HashMap<String, String> config_data = new HashMap<>();
                    for (int option = 0; option < object.length(); option++) {
                        JSONObject jsonObject = object.getJSONObject(option);
                        String label = jsonObject.getString("label");
                        String value = jsonObject.getString("value");
                        config_data.put(value, label);
                    }
                    finalconfigdata.put(item_id, config_data);
                }
                if (product_type.equals("downloadable")) {
                    isHavingdownloadable = true;
                }
                ProductData.add(productdata);
                System.out.println("ProductData:" + ProductData);
            }

            if (finalconfigdata.size() > 0 && bundledata.size() <= 0) {
                Log.i("ProductData", "In");
                Log.i("ProductData", "In" + finalconfigdata);
                listViewAdapter = new OrderSummaryAdapter(this, ProductData, finalconfigdata, false);
            }
            else {
                if (bundledata.size() > 0 && finalconfigdata.size() <= 0) {
                    Log.i("ProductData", "In2");
                    listViewAdapter = new OrderSummaryAdapter(this, ProductData, bundledata, "Bundle", false);
                } else {
                    if (bundledata.size() > 0 && finalconfigdata.size() > 0) {
                        Log.i("ProductData", "In3");
                        listViewAdapter = new OrderSummaryAdapter(this, ProductData, bundledata, finalconfigdata, false);
                    } else {
                        Log.i("ProductData", "In4");
                        listViewAdapter = new OrderSummaryAdapter(this, ProductData, false);
                    }
                }
            }

            binding.orderdItemRv.setAdapter(listViewAdapter);

            String is_discount = jsonObj.getJSONObject("data").getString("is_discount");
            allowed_guest_checkout = jsonObj.getJSONObject("data").getString("allowed_guest_checkout");
            String coupon = "";
            if (jsonObj.getJSONObject("data").has("coupon")) {
                coupon = jsonObj.getJSONObject("data").getString("coupon");
            }

            if (is_discount.equals("true")) {
                binding.MageNativeApplycoupantag.setText(coupon);
                binding.MageNativeApplycoupan.setText(getResources().getString(R.string.removecoupon));
                binding.MageNativeApplycoupantag.setEnabled(false);
            }
            else {
                binding.MageNativeApplycoupantag.setEnabled(true);
                binding.MageNativeApplycoupantag.setText("");
                binding.MageNativeApplycoupan.setHint(getResources().getString(R.string.couponcode));
                binding.MageNativeApplycoupan.setText(getResources().getString(R.string.apply));
            }
        } catch (Exception w) {
            w.printStackTrace();
            Intent intent = new Intent(getApplicationContext(), Ced_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    public void changeCartCount() {
        invalidateOptionsMenu();
    }
    //TODO view cart request and response view end

    //TODO get address req and response start

    private void onGetAddressBook() {
        try {
            addressBookParams = new JsonObject();
            try {
                if (session.isLoggedIn()) {
                    addressBookParams.addProperty("customer_id", session.getCustomerid());
                }
                if (cedSessionManagement.getCartId() != null) {
                    addressBookParams.addProperty("cart_id", cedSessionManagement.getCartId());
                }
                if (cedSessionManagement.getStoreId() != null) {
                    addressBookParams.addProperty("store_id", cedSessionManagement.getStoreId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            addressViewModel.getAddressListData(CustomOrderSummaryActivity.this, addressBookParams, session.getHahkey()).observe(CustomOrderSummaryActivity.this, apiResponse -> {
                switch (apiResponse.status) {
                    case SUCCESS:
                        try {
                            applyAddressData(apiResponse.data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    case ERROR:
                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                        showmsg(getResources().getString(R.string.errorString));
                        break;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyAddressData(String resStr) throws JSONException {
        JSONObject jsonObject = new JSONObject(resStr);
        if (jsonObject.has("header") && jsonObject.getString("header").equals("false")) {
            Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        } else {
            if (jsonObject.getJSONObject("data").getString("status").equals("success")) {
                addressHashMap = new LinkedHashMap<>();
                addressMobile = new LinkedHashMap<>();
                JSONArray user_details = jsonObject.getJSONObject("data").getJSONArray("address");
                if (user_details.length() > 0) {
                    for (int i = 0; i < user_details.length(); i++) {
                        JSONObject c = user_details.getJSONObject(i);
                        String firstname, lastname, city, state, phone, country, street = "", pincode = "", id = null;
                        id = c.getString(KEY_ADDRESS_ID);
                        firstname = c.getString(KEY_FIRSTNAME);
                        lastname = c.getString(KEY_LASTNAME);
                        city = c.getString(KEY_CITY);
                        state = c.getString(KEY_STATE);
                        phone = c.getString(KEY_PHONE);
                        country = c.getString(KEY_COUNTRY);
                        if (c.has(Ced_Addressbook.KEY_STREET)) {
                            if (c.getString(Ced_Addressbook.KEY_STREET).contains("{")) {
                                street = String.valueOf(new JSONObject(c.getString(Ced_Addressbook.KEY_STREET)).get("0"));
                            } else {
                                street = c.getString(Ced_Addressbook.KEY_STREET);
                            }
                        }
                        try {
                            pincode = c.getString(KEY_PINCODE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String address = firstname + " " + lastname + "\n" + street + " " + city + " " + state + " " + country + " " + pincode;
                        String address_map = firstname + " " + lastname + "\n" + street + " " + city + " " + state + "\n" + country + " " + pincode;
                        addressHashMap.put(id, address_map);
                        addressMobile.put(id, phone);
                        if (i == 0) {
                            binding.selectedDeliveryAddressId.setText(id);
                            binding.deliveryAddressTV.setText(address);
                            binding.deliveryAddressMobileTV.setText(phone);
                            binding.deliveryAddressMobileTV.setVisibility(View.VISIBLE);
                            binding.deliveryAddressMobileLabelTV.setVisibility(View.VISIBLE);
                            binding.deliveryAddressTV.setVisibility(View.VISIBLE);
                            //shipping address
                            binding.MageNativeAddressList.setText(address);
                            binding.MageNativeAddressList.setTag(id);
                            binding.billingShippingSame.setChecked(true);
                            binding.shippingAddressDropDown.setVisibility(View.GONE);
                        }

               /*         if ((c.has("default_billing") && c.has("default_shipping")) && (c.getString("default_billing").equalsIgnoreCase("true")
                                && c.getString("default_shipping").equalsIgnoreCase("true"))) {
                            binding.selectedDeliveryAddressId.setText(id);
                            binding.deliveryAddressTV.setText(address);
                            binding.deliveryAddressMobileTV.setText(phone);
                            binding.deliveryAddressMobileTV.setVisibility(View.VISIBLE);
                            binding.deliveryAddressMobileLabelTV.setVisibility(View.VISIBLE);
                            binding.deliveryAddressTV.setVisibility(View.VISIBLE);
                            //shipping address
                            binding.MageNativeAddressList.setText(address);
                            binding.MageNativeAddressList.setTag(id);
                        }
                        else if (c.has("default_billing") && c.getString("default_billing").equalsIgnoreCase("true")) {
                            binding.selectedDeliveryAddressId.setText(id);
                            binding.deliveryAddressTV.setText(address);
                            binding.deliveryAddressMobileTV.setText(phone);
                            binding.deliveryAddressMobileTV.setVisibility(View.VISIBLE);
                            binding.deliveryAddressMobileLabelTV.setVisibility(View.VISIBLE);
                            binding.deliveryAddressTV.setVisibility(View.VISIBLE);
                        } else if (c.has("default_shipping") && c.getString("default_shipping").equalsIgnoreCase("true")) {
                            //shipping address
                            binding.MageNativeAddressList.setText(address);
                            binding.MageNativeAddressList.setTag(id);
                        }else{
                            binding.selectedDeliveryAddressId.setText(id);
                            binding.deliveryAddressTV.setText(address);
                            binding.deliveryAddressMobileTV.setText(phone);
                            binding.deliveryAddressMobileTV.setVisibility(View.VISIBLE);
                            binding.deliveryAddressMobileLabelTV.setVisibility(View.VISIBLE);
                            binding.deliveryAddressTV.setVisibility(View.VISIBLE);
                            binding.MageNativeAddressList.setText(address);
                            binding.MageNativeAddressList.setTag(id);
                        }*/
                    }

                    binding.noShippingMethodMsg.setVisibility(View.GONE);
                    binding.noPaymentMethodMsg.setVisibility(View.GONE);
                    binding.noDeliveryAddressParentView.setVisibility(View.GONE);
                    binding.deliveryAddressMobileTV.setVisibility(View.VISIBLE);
                    binding.deliveryAddressMobileLabelTV.setVisibility(View.VISIBLE);
                    binding.deliveryAddressTV.setVisibility(View.VISIBLE);
                    binding.deliveryHeaderTV.setVisibility(View.VISIBLE);
                    binding.changeAddressTV.setVisibility(View.VISIBLE);
                    onSaveBillingAndShippingAddress();
                }
            } else if (jsonObject.getJSONObject("data").getString("status").equals("no_address")) {
                cartRequest();
                //handle change address or add new address
                binding.deliveryAddressMobileTV.setVisibility(View.GONE);
                binding.deliveryAddressMobileLabelTV.setVisibility(View.GONE);
                binding.deliveryAddressTV.setVisibility(View.GONE);
                binding.deliveryHeaderTV.setVisibility(View.GONE);
                binding.changeAddressTV.setVisibility(View.GONE);
                binding.noShippingMethodMsg.setVisibility(View.VISIBLE);
                binding.noPaymentMethodMsg.setVisibility(View.VISIBLE);
                binding.noDeliveryAddressParentView.setVisibility(View.VISIBLE);
            }
        }
    }
    //TODO get address req and response end

    public void onChangedAddressClicked(View view) {
        try {
            try {
                binding.addressBottomSheetLayout.heading.setText(R.string.selecte_delivery_address);
                if (addressHashMap != null) {
                    onCreateAndBindDataToAddressView("delivery");
                    if (addressListBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        addressListBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    } else {
                        addressListBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onChangeShippingAddress(View view) {
        try {
            if (binding.billingShippingSame.isChecked()) {
                binding.shippingAddressDropDown.setVisibility(View.GONE);
            } else {
                binding.shippingAddressDropDown.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onCreateAndBindDataToAddressView(String from) {
        binding.addressBottomSheetLayout.parentViewForAddress.removeAllViews();
        for (Map.Entry<String, String> entry : addressHashMap.entrySet()) {
            BottomAddressSheetSingleItemBinding bottomSheetAddressLayoutBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.bottom_address_sheet_single_item, null, false);
            set_regular_font_fortext(bottomSheetAddressLayoutBinding.deliveryAddressTV);
            set_regular_font_fortext(bottomSheetAddressLayoutBinding.deliveryAddressMobileTV);
            if (from.equalsIgnoreCase("shipping")) {
                if (binding.MageNativeAddressList.getTag().toString().trim().equalsIgnoreCase(entry.getKey()))
                    bottomSheetAddressLayoutBinding.addressRadioBtn.setChecked(true);
            } else if (from.equalsIgnoreCase("delivery")) {
                if (binding.selectedDeliveryAddressId.getText().toString().trim().equalsIgnoreCase(entry.getKey()))
                    bottomSheetAddressLayoutBinding.addressRadioBtn.setChecked(true);
            }
            if (addressMobile.containsKey(entry.getKey()))
                bottomSheetAddressLayoutBinding.deliveryAddressMobileTV.setText(addressMobile.get(entry.getKey()));

            bottomSheetAddressLayoutBinding.addressRadioBtn.setTag(entry.getKey());

            bottomSheetAddressLayoutBinding.deliveryAddressTV.setText(entry.getValue());
            bottomSheetAddressLayoutBinding.deliveryAddressTV.setTag(entry.getKey());

            bottomSheetAddressLayoutBinding.deliveryAddressMobileTV.setVisibility(View.VISIBLE);
            bottomSheetAddressLayoutBinding.deliveryAddressMobileLabelTV.setVisibility(View.VISIBLE);

            bottomSheetAddressLayoutBinding.addressRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
                addressListBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (b) {
                    AppCompatRadioButton appCompatRadioButton = (AppCompatRadioButton) compoundButton;
                    String key = appCompatRadioButton.getTag().toString();
                    addressListBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    if (from.equalsIgnoreCase("shipping")) {
                        //shipping address
                        if (binding.selectedDeliveryAddressId.getText().toString().trim().equalsIgnoreCase(key)) {
                            binding.billingShippingSame.setChecked(true);
                            binding.shippingAddressDropDown.setVisibility(View.GONE);
                        }
                        binding.MageNativeAddressList.setText(addressHashMap.get(key));
                        binding.MageNativeAddressList.setTag(key);
                    } else if (from.equalsIgnoreCase("delivery")) {
                        //delivery address
                        binding.selectedDeliveryAddressId.setText(key);
                        binding.deliveryAddressTV.setText(addressHashMap.get(key));
                        binding.deliveryAddressMobileTV.setText(addressMobile.get(key));
                        onSaveBillingAndShippingAddress();
                    }
                }
            });

            binding.addressBottomSheetLayout.parentViewForAddress.addView(bottomSheetAddressLayoutBinding.getRoot());
        }
    }

    private void onSaveBillingAndShippingAddress() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Role", "USER");
        jsonObject.addProperty("cart_id", cedSessionManagement.getCartId());
        jsonObject.addProperty("customer_id", session.getCustomerid());
        JsonObject extension_attributes = new JsonObject();
        if (binding.billingShippingSame.isChecked()) {
            extension_attributes.addProperty("shipping_address_id", binding.selectedDeliveryAddressId.getText().toString().trim());
            extension_attributes.addProperty("same", true);
        } else {
            extension_attributes.addProperty("billing_address_id", binding.selectedDeliveryAddressId.getText().toString().trim());
            extension_attributes.addProperty("shipping_address_id", binding.MageNativeAddressList.getTag().toString().trim());
            extension_attributes.addProperty("same", false);
        }
        jsonObject.add("extension_attributes", extension_attributes);
        Log.e(Urls.TAG, "onSaveBillingAndShippingAddress: " + jsonObject);

        try {
            checkoutViewModel.saveBillingAddress(CustomOrderSummaryActivity.this, jsonObject).observe(CustomOrderSummaryActivity.this, apiResponse -> {
                switch (apiResponse.status) {
                    case SUCCESS:
                        onManageSaveBillingShippingApiResponse(apiResponse.data);
                        break;

                    case ERROR:
                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                        showmsg(getResources().getString(R.string.errorString));
                        break;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onManageSaveBillingShippingApiResponse(String s) {
        try {
            JSONObject object = new JSONObject(s);
            String success = object.getString("success");
            if (success.equals("true")) {
                //TODO call getShipPaymentAPI
                onCallShippingPaymentAPI();
            } else {
                if (object.has("message")) {
                    showmsg(object.getString("message"));
                } else {
                    showmsg(getResources().getString(R.string.somethingbadhappended));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onCallShippingPaymentAPI() {
        try {
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("Role", "USER");
            if (cedSessionManagement.getCartId() != null) {
                jsonObject.addProperty("cart_id", cedSessionManagement.getCartId());
            }
            if (session.isLoggedIn()) {
                jsonObject.addProperty("customer_id", session.getCustomerid());
            }

            checkoutViewModel.getShippingPaymentMethods(CustomOrderSummaryActivity.this, jsonObject).observe(CustomOrderSummaryActivity.this, apiResponse -> {
                switch (apiResponse.status) {
                    case SUCCESS:
                        onManageShippingPaymentAPIResponse(apiResponse.data);
                        break;

                    case ERROR:
                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                        showmsg(getResources().getString(R.string.errorString));
                        break;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onManageShippingPaymentAPIResponse(String data) {
        try {
            JSONObject object = new JSONObject(data);
            String success = object.getString("success");
            if (success.equals("true")) {
                if (object.get("shipping").equals("No Quotes Availabile.")) {
                    showmsg(getString(R.string.no_quote_available));
                } else {
                    JSONObject shipping = object.getJSONObject("shipping");
                    JSONArray methods = shipping.getJSONArray("methods");
                    JSONObject shipping_method_json_obj = methods.getJSONObject(0);
                    binding.shippingMethod.setVisibility(View.VISIBLE);
                    binding.paymentMethod.setVisibility(View.VISIBLE);
                    binding.shippingMethod.setText(shipping_method_json_obj.getString("label"));
                    binding.shippingMethod.setTag(shipping_method_json_obj.getString("value"));
                    binding.shippingMethodPriceTV.setText(shipping_method_json_obj.getString("price"));

                    onSavePaymentAndShipmentMethod();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onSavePaymentAndShipmentMethod() {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("Role", "USER");
            json.addProperty("cart_id", cedSessionManagement.getCartId());
            json.addProperty("customer_id", session.getCustomerid());
            json.addProperty("payment_method", "RazorPay");
            json.addProperty("shipping_method", binding.shippingMethod.getTag().toString().trim());
            checkoutViewModel.saveShippingPaymentMethods(CustomOrderSummaryActivity.this, json).observe(CustomOrderSummaryActivity.this, apiResponse -> {
                switch (apiResponse.status) {
                    case SUCCESS:
                        onManageSaveShipmentAndPaymentMethodAPIResponse(apiResponse.data);
                        break;

                    case ERROR:
                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                        showmsg(getResources().getString(R.string.errorString));
                        break;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onManageSaveShipmentAndPaymentMethodAPIResponse(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.getString("success").equals("true")) {
                cartRequest();
            } else {
                showmsg(getResources().getString(R.string.errorString));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onPayButtonClicked(View view) {
        if (binding.selectedDeliveryAddressId.getText().toString().trim().isEmpty()) {
            showmsg(getResources().getString(R.string._please_select_delivery_address));
        } else if (binding.shippingMethod.getText().toString().trim().isEmpty()) {
            showmsg(getString(R.string.no_quote_available));
        } else if (grandTotalForGetRazorPayOrderId.length() == 0) {
            showmsg(getString(R.string.somethingbadhappended));
        } else {
/*            Intent intent = new Intent(CustomOrderSummaryActivity.this, PlacedOrderRazorPay.class);
            intent.putExtra("email", session.getUserDetails().get("Email"));
            intent.putExtra("paymentcode", "apppayment");
            intent.putExtra("grand_total", grandTotalForGetRazorPayOrderId);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);*/
// TODO           binding.MageNativeCheckout.setEnabled(false);
            razorPayOrderId(grandTotalForGetRazorPayOrderId);
        }
    }

    private void CouponResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                try {
                    JSONObject object = new JSONObject(Objects.requireNonNull(apiResponse.data));
                    if (object.getJSONObject("cart_id").getString("success").equals("true")) {
                        showmsg(object.getJSONObject("cart_id").getString("message"));
                        cartRequest();
                    } else {
                        if (object.getJSONObject("cart_id").getString("success").equals("false")) {
                            showmsg(object.getJSONObject("cart_id").getString("message"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case ERROR:
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FROM_CUSTOM_ORDER_SUMMARY) {
            try {
                if (Objects.requireNonNull(data).hasExtra("MESSAGE")) {
                    showmsg(data.getStringExtra("MESSAGE"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            onGetAddressBook();
        }
    }

    //Razor Pay Code Start
    private void razorPayOrderId(String grandTotal) {
        JsonObject object = new JsonObject();
        try {
            if (cedSessionManagement.getCartId() != null) {
                object.addProperty("cart_id", cedSessionManagement.getCartId());
            }
            object.addProperty("customer_id", session.getCustomerid());
            if (null != cedSessionManagement.getStoreId()) {
                object.addProperty("store_id", cedSessionManagement.getStoreId());
            }
            if (!grandTotal.contains(","))
                object.addProperty("amount", Double.valueOf(Double.parseDouble(grandTotal) * 100).intValue());
            else
                object.addProperty("amount", Double.valueOf(Double.parseDouble(grandTotal.replace(",", "")) * 100).intValue());

            checkoutViewModel.getRazorPayOrderId(CustomOrderSummaryActivity.this, object).observe(CustomOrderSummaryActivity.this, apiResponse -> {
                switch (apiResponse.status) {
                    case SUCCESS:
                        try {
                            JSONObject response = new JSONObject(Objects.requireNonNull(apiResponse.data));
                            if (response.getBoolean("success")) {
                                razorpay_order_id = response.getString("razorpay_order_id");
                                String amount_paid = response.getString("amount_paid");
                                String currency = response.getString("currency");
                                if (isOldFlow) {
                                    saveOrder();
                                } else
                                    startPayment(cedSessionManagement.getcurrency(), amount_paid, razorpay_order_id);
                            } else {
                                binding.MageNativeCheckout.setEnabled(true);
                                showmsg(response.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    case ERROR:
                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                        binding.MageNativeCheckout.setEnabled(true);
                        showmsg(getResources().getString(R.string.errorString));
                        break;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveOrder() {
        JsonObject order_post_data = new JsonObject();
        order_post_data.addProperty("email", email);
        if (cedSessionManagement.getCartId() != null) {
            order_post_data.addProperty("cart_id", cedSessionManagement.getCartId());
        }
        if (session.isLoggedIn()) {
            order_post_data.addProperty("customer_id", session.getCustomerid());
        }
        if (cedSessionManagement.getStoreId() != null) {
            order_post_data.addProperty("store_id", cedSessionManagement.getStoreId());
        }
        JsonObject extension_attributes = new JsonObject();
        extension_attributes.addProperty("device_type", "app");
        order_post_data.add("extension_attributes", extension_attributes);
        try {
            checkoutViewModel.saveOrder(CustomOrderSummaryActivity.this, order_post_data).observe(CustomOrderSummaryActivity.this, apiResponse -> {
                switch (apiResponse.status) {
                    case SUCCESS:
                        try {
                            processOrderData(apiResponse.data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    case ERROR:
                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                        binding.MageNativeCheckout.setEnabled(true);
                        showmsg(getResources().getString(R.string.errorString));
                        break;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processOrderData(String s) throws JSONException {
        JSONObject object = new JSONObject(s);
        JSONObject jsonObject = object;
        if (jsonObject.getString("success").equals("true")) {
            cedSessionManagement.clearcartId();
            load = true;
            order_id = jsonObject.getString("order_id");
            grandTotal = jsonObject.getString("grandtotal");
            String currency_code = jsonObject.getString("currency_code");
            if (isOldFlow)
                startPayment(currency_code, grandTotal, razorpay_order_id);
            else {
                additionalInfoParam.addProperty("order_id", order_id);
                setFinalOrderCheck(additionalInfoParam);
            }
        } else {
            binding.MageNativeCheckout.setEnabled(true);
        }
    }

    public void startPayment(String currencyCode, String amount, String orderId) {
//        orderId = cedSessionManagement.getCartId();
        final Activity activity = this;
        checkout.setImage(R.mipmap.ic_launcher);
        try {
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.app_name));
            options.put("description", "Your Total Amount is:");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", R.mipmap.ic_launcher);
            options.put("currency", currencyCode);
            options.put("order_id", orderId);
            options.put("amount", amount);
            JSONObject preFill = new JSONObject();
            preFill.put("email", session.getUserDetails().get(Ced_SessionManagement_login.Key_Email));
            preFill.put("contact", session.getUserMobile());
            options.put("prefill", preFill);
            options.put("theme", new JSONObject("{color: '#30B7C5'}"));
            Log.e(Urls.TAG, "RAZOR_PAY_PARAMS " + options);
            checkout.open(activity, options);
        } catch (Exception e) {
            showmsg("Error in payment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setFinalOrderCheck(JsonObject object) {
        try {
            drawerViewModel.getAdditionalInfo(CustomOrderSummaryActivity.this, object).observe(CustomOrderSummaryActivity.this, apiResponse -> {
                switch (apiResponse.status) {
                    case SUCCESS:
                        try {
                            JSONObject jsonObject1 = new JSONObject(Objects.requireNonNull(apiResponse.data));
                            String success = jsonObject1.getString("success");
                            if (success.equals("true")) {
                                Ced_MainActivity.latestcartcount = "0";
                                cedSessionManagement.clearcartId();
                                invalidateOptionsMenu();
                                Intent intent = new Intent(CustomOrderSummaryActivity.this, Ced_ViewOrder.class);
                                intent.putExtra("order_id", String.valueOf(object.get("order_id")));
                                startActivity(intent);
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            } else {
                                Ced_MainActivity.latestcartcount = "0";
                                cedSessionManagement.clearcartId();
                                invalidateOptionsMenu();
                                Intent intent1 = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                                startActivity(intent1);
                                finishAffinity();
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case ERROR:
                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                        showmsg(getResources().getString(R.string.errorString));
                        break;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Razor Pay Code End

    @Override
    public void onBackPressed() {
        if (load) {
            try {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("order_id", order_id);
                jsonObject.addProperty("failure", "true");
//                setFinalOrderCheck(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            super.onBackPressed();
            Intent intent = new Intent();
            intent.putExtra("data", "from");
            setResult(2777);
            finish();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID, PaymentData paymentData) {
        Log.e("RAZOR_PAY_SUCCESS", "onPaymentSuccess: " + paymentData);
        try {
            showmsg("Payment Successful. Processing your order ...");
            if (isOldFlow) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("additional_info", razorpayPaymentID);
                jsonObject.addProperty("order_id", order_id);
                jsonObject.addProperty("failure", "false");
                setFinalOrderCheck(jsonObject);
            } else {
                additionalInfoParam = new JsonObject();
                additionalInfoParam.addProperty("additional_info", razorpayPaymentID);
                additionalInfoParam.addProperty("failure", "false");
                saveOrder();
            }
        } catch (Exception e) {
            Log.e("Exception", "Exception in onPaymentSuccess :", e);
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Log.e("RAZOR_PAY_ERROR", "onPaymentError: " + s);
        Log.e("RAZOR_PAY_ERROR", "onPaymentErrorCODE: " + i);
        try {
            if (isOldFlow) {
                showmsg("Payment failed::Please try again Thank you.");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("failure", "true");
                jsonObject.addProperty("order_id", order_id);
                setFinalOrderCheck(jsonObject);
            } else {
                if (Checkout.PAYMENT_CANCELED == i) {
                    showmsg("Payment Closed.");
                } else if (Checkout.INVALID_OPTIONS == i) {
                    Log.e(Urls.TAG, "onPaymentError: " + s);
                } else if (Checkout.NETWORK_ERROR == i) {
                    showmsg("There was a network error. For example, loss of internet connectivity.");
                } else if (Checkout.TLS_ERROR == 1) {
                    showmsg("The device does not support TLS v1.1 or TLS v1.2.");
                } else {
                    showmsg("Payment failed::Please try again Thank you.");
                    additionalInfoParam = new JsonObject();
                    additionalInfoParam.addProperty("failure", "true");
                    saveOrder();
                }
            }
        } catch (Exception e) {
            Log.e("Exception", "Exception in onPaymentSuccess :", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Checkout.clearUserData(this);
    }

}