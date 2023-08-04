/*
 * Copyright/**
 *          * CedCommerce
 *           *
 *           * NOTICE OF LICENSE
 *           *
 *           * This source file is subject to the End User License Agreement (EULA)
 *           * that is bundled with this package in the file LICENSE.txt.
 *           * It is also available through the world-wide-web at this URL:
 *           * http://cedcommerce.com/license-agreement.txt
 *           *
 *           * @category  Ced
 *           * @package   MageNative
 *           * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *           * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *           * @license   http://cedcommerce.com/license-agreement.txt
 *
 */
package com.akshar.store.ui.cartsection.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshar.store.databinding.NewFooterPriceLayoutBinding;
import com.akshar.store.databinding.NewMagenativeActivityCartListingBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonObject;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import com.akshar.store.databinding.FooterpricelayoutBinding;
import com.akshar.store.databinding.MagenativeActivityCartListing2Binding;
import com.akshar.store.databinding.MagenativeActivityCartListingBinding;
import com.akshar.store.databinding.MagenativeActivityNoModuleBinding;
import com.akshar.store.databinding.MagenativeCustomOptionsBinding;
import com.akshar.store.databinding.MagenativeEmptyCartBinding;
import com.akshar.store.rest.ApiResponse;
import com.akshar.store.ui.cartsection.adapter.Ced_CartListAdapter;
import com.akshar.store.ui.cartsection.viewmodel.CartViewModel;
import com.akshar.store.ui.loginsection.activity.Ced_Login;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ced_CartListing extends Ced_NavigationActivity {
    //Variable and Object Initializations
    public static HashMap<String, String> updatedquantitywithid = new HashMap<String, String>(); //update cart listing ...
    public static final String KEY_ITEM = "data";
    public static final String KEY_SUB_ITEM = "products";
    public static final String KEY_ID = "product_id";
    public static final String KEY_NAME = "product-name";
    public static final String KEY_Image = "product_image";
    public static final String KEY_Price = "sub-total";
    public static final String Key_Quantity = "quantity";
    public static final String ITEMID = "item_id";
    static boolean isHavingdownloadable = false;
    //    MagenativeActivityCartListingBinding magenativeActivityCartListingBinding;
    NewMagenativeActivityCartListingBinding magenativeActivityCartListingBinding;
    MagenativeActivityCartListing2Binding cartListing2Binding;
    @Inject
    ViewModelFactory viewModelFactory;
    CartViewModel cartViewModel;

    HashMap<String, HashMap<String, String>> finalconfigdata;
    HashMap<String, ArrayList<String>> bundledata;
    HashMap<String, String> configdata;
    ArrayList<HashMap<String, String>> ProductData;
    ConstraintLayout main;
    LinearLayoutManager linearLayoutManager;

    //    ListView list;
    String ids;
    String URL;
    String product_type;
    String items_count;
    JSONArray products = null;
    JSONArray amount = null;
    ArrayList<String> IDS;
    String allowed_guest_checkout = "";
    String couponcodedata = "";
    JsonObject cartlist;
    TextView conti;
    private Ced_CartListAdapter listViewAdapter;
    String cart_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            showBackButtonWithTitle("My Cart");
            hidebottombar();
            cartViewModel = new ViewModelProvider(Ced_CartListing.this, viewModelFactory).get(CartViewModel.class);
            cartListing2Binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_activity_cart_listing2, content, true);
            updatedquantitywithid = new HashMap<String, String>();
            selectcarttab();
            IDS = new ArrayList<>();
            bundledata = new HashMap<>();
            configdata = new HashMap<>();
            finalconfigdata = new HashMap<>();
            cartlist = new JsonObject();
            // Data to send on server
            if (getIntent().getExtras() != null) {
                couponcodedata = getIntent().getStringExtra("couponcode");
            }
            request();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void request() {
        try {

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

    private void proceed(JsonObject cartlist) {

        cartViewModel.viewCart(Ced_CartListing.this, this.cartlist).observe(Ced_CartListing.this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(apiResponse.data);
                        if (object.has("header") && object.getString("header").equals("false")) {
                            MagenativeActivityNoModuleBinding magenativeActivityNoModuleBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_activity_no_module, content, true);
                            magenativeActivityNoModuleBinding.conti.setVisibility(View.VISIBLE);
                            magenativeActivityNoModuleBinding.conti.setOnClickListener(v -> request());
                        } else {
                            if (object.has("success")) {
                                String status = object.getString("success");
                                if (status.equals("false")) {
                                    Ced_MainActivity.latestcartcount = "0";
                                    changecartcount();
                                    emptyCartView();
                                       /* if(object.has("message"))
                                        {
                                            showmsg(object.getString("message"));
                                        }*/

                                }
                            } else {
                                applydata2(apiResponse.data);
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

    private void emptyCartView() {
        MagenativeEmptyCartBinding magenativeEmptyCartBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_empty_cart, content, true);
        magenativeEmptyCartBinding.conti.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
            intent.putExtra("exceptfromhome", "true");
            startActivity(intent);
            finishAffinity();
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            finish();
        });
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    public void applydata2(String Jstring) {
        try {
            //--------------------------------
            cartListing2Binding.parentlayout.removeAllViews();
            //--------------------------------------------------------
//            magenativeActivityCartListingBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_activity_cart_listing, null, false);
            magenativeActivityCartListingBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.new_magenative_activity_cart_listing, null, false);
            linearLayoutManager = new LinearLayoutManager(Ced_CartListing.this, RecyclerView.VERTICAL, false);
            magenativeActivityCartListingBinding.cartRecycler.setLayoutManager(linearLayoutManager);
            magenativeActivityCartListingBinding.cartRecycler.setNestedScrollingEnabled(false);
            //--------------------------------------------------------
            IDS = new ArrayList<>();
            bundledata = new HashMap<>();
            configdata = new HashMap<>();
            finalconfigdata = new HashMap<>();
            cartlist = new JsonObject();
            ProductData = new ArrayList<>();
            set_regular_font_fortext(magenativeActivityCartListingBinding.couponcodetext);
            set_bold_font_fortext(magenativeActivityCartListingBinding.MageNativeCount);
            set_regular_font_fortext(magenativeActivityCartListingBinding.MageNativeApplycoupan);
            set_regular_font_forEdittext(magenativeActivityCartListingBinding.MageNativeApplycoupantag);
            set_regular_font_forButton(magenativeActivityCartListingBinding.MageNativeCheckout);
            //--------------------------------------------------------
            magenativeActivityCartListingBinding.MageNativeClearcartsection.setOnClickListener(v -> {
                try {
                    new MaterialAlertDialogBuilder(Ced_CartListing.this, R.style.MaterialDialog)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setIcon(R.mipmap.ic_launcher)
                            .setMessage(getResources().getString(R.string.confirm_alldelete))
                            .setNegativeButton("Cancel", (dialog, which) -> {
                            })
                            .setPositiveButton("Confirm", (dialog, which) -> {
                                clearallcart();
                            })
                            .show();


                } catch (Exception e) {
                    Log.i("Clearcart", "In Exception");
                    e.printStackTrace();
                    Intent intent = new Intent(Ced_CartListing.this, Ced_MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
            });
            //--------------------------------------------------------
            magenativeActivityCartListingBinding.MageNativeApplycoupan.setOnClickListener(view12 -> {
                magenativeActivityCartListingBinding.MageNativeApplycoupantag.requestFocus();
                if (magenativeActivityCartListingBinding.MageNativeApplycoupan.getText().toString().equals(getResources().getString(R.string.apply))) {
                    if (magenativeActivityCartListingBinding.MageNativeApplycoupantag.getText().toString().isEmpty()) {
                        magenativeActivityCartListingBinding.MageNativeApplycoupantag.setError(getResources().getString(R.string.empty));
                    } else {
                        JsonObject coupon1 = new JsonObject();
                        if (session.isLoggedIn()) {
                            coupon1.addProperty("customer_id", session.getCustomerid());
                            coupon1.addProperty("coupon_code", magenativeActivityCartListingBinding.MageNativeApplycoupantag.getText().toString());
                        } else {
                            coupon1.addProperty("cart_id", cedSessionManagement.getCartId());
                            coupon1.addProperty("coupon_code", magenativeActivityCartListingBinding.MageNativeApplycoupantag.getText().toString());
                        }
                        cartViewModel.applycoupon(Ced_CartListing.this, coupon1).observe(Ced_CartListing.this, Ced_CartListing.this::CouponResponse);
                    }

                        /*//TODO
                        Ced_ClientRequestResponseRest crr = new Ced_ClientRequestResponseRest(output -> {
                            String couponapplied = output.toString();
                                JSONObject object = new JSONObject(couponapplied);
                                if (object.getJSONObject("cart_id").getString("success").equals("true")) {
                                    showmsg(object.getJSONObject("cart_id").getString("message"));
                                    Intent intent = new Intent(getApplicationContext(), Ced_CartListing.class);
                                    intent.putExtra("couponcode",  magenativeActivityCartListingBinding.MageNativeApplycoupantag.getText().toString());
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                } else {
                                    if (object.getJSONObject("cart_id").getString("success").equals("false")) {
                                        showmsg(object.getJSONObject("cart_id").getString("message"));
                                    }
                                }

                        }, Ced_CartListing.this, "POST", coupon1.toString());
                        crr.execute(appycouponurl);}*/
                } else {
                    if (magenativeActivityCartListingBinding.MageNativeApplycoupan.getText().toString().equals(getResources().getString(R.string.removecoupon))) {
                        JsonObject coupon1 = new JsonObject();
                        if (session.isLoggedIn()) {
                            coupon1.addProperty("customer_id", session.getCustomerid());
                            coupon1.addProperty("remove", "1");
                        } else {
                            coupon1.addProperty("cart_id", cedSessionManagement.getCartId());
                            coupon1.addProperty("remove", "1");
                        }
                        cartViewModel.applycoupon(Ced_CartListing.this, coupon1).observe(Ced_CartListing.this, Ced_CartListing.this::CouponResponse);
                    }
                    //TODO
                       /* Ced_ClientRequestResponseRest crr = new Ced_ClientRequestResponseRest(output -> {
                            String couponapplied = output.toString();
                                JSONObject object = new JSONObject(couponapplied);
                                if (object.getJSONObject("cart_id").getString("success").equals("true")) {
                                    showmsg(object.getJSONObject("cart_id").getString("message"));
                                    Intent intent = new Intent(getApplicationContext(), Ced_CartListing.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                } else {
                                    if (object.getJSONObject("cart_id").getString("success").equals("false")) {
                                        showmsg(object.getJSONObject("cart_id").getString("message"));
                                    }
                                }

                        }, Ced_CartListing.this, "POST", coupon1.toString());
                        crr.execute(appycouponurl);}*/
                }
            });
            //--------------------------------
            magenativeActivityCartListingBinding.main.setVisibility(View.VISIBLE);
            JSONObject jsonObj = new JSONObject(Jstring);
            if (jsonObj.getJSONObject(KEY_ITEM).has("cart_error")) {
                cart_error = jsonObj.getJSONObject(KEY_ITEM).getString("cart_error");
                if (!cart_error.equals("") && cart_error != null) {
                    cartListing2Binding.cartErrorTxt.setText(cart_error);
                    cartListing2Binding.errorLayout.setVisibility(View.VISIBLE);
                }
            } else {
                cartListing2Binding.errorLayout.setVisibility(View.GONE);
            }

            products = jsonObj.getJSONObject(KEY_ITEM).getJSONArray(KEY_SUB_ITEM);
            items_count = String.valueOf(jsonObj.getJSONObject(KEY_ITEM).getInt("items_count"));
            String is_discount = jsonObj.getJSONObject(KEY_ITEM).getString("is_discount");
            allowed_guest_checkout = jsonObj.getJSONObject(KEY_ITEM).getString("allowed_guest_checkout");
            String coupon = "";
            if (jsonObj.getJSONObject(KEY_ITEM).has("coupon")) {
                coupon = jsonObj.getJSONObject(KEY_ITEM).getString("coupon");
            }
            Ced_MainActivity.latestcartcount = items_count;
            changecartcount();
            magenativeActivityCartListingBinding.MageNativeCount.setText(items_count.toUpperCase() + "  " + getResources().getString(R.string.itemcounttext));
            //that line on code will show the total item in the price layout
            magenativeActivityCartListingBinding.MageNativeCountForPrice.setText(getString(R.string.product_details_) + items_count.toUpperCase() + getString(R.string.items_s_));

            for (int i = 0; i < products.length(); i++) {
                JSONObject c = null;
                c = products.getJSONObject(i);
                String id = c.getString(KEY_ID);
                String item_id = c.getString(ITEMID);
                String name = c.getString(KEY_NAME);
                String image = c.getString(KEY_Image);
                String price = c.getString(KEY_Price);
                int Quantity = c.getInt(Key_Quantity);
                if (c.has("sub_seller_id"))
                    cedSessionManagement.saveSubSellerIdFromCart(c.getString("sub_seller_id"));
                product_type = c.getString("product_type");
                HashMap<String, String> productdata = new HashMap<>();
                productdata.put(KEY_ID, id);
                productdata.put(KEY_NAME, name);
                productdata.put(KEY_Image, image);
                productdata.put(ITEMID, item_id);
                productdata.put(KEY_Price, price);
                productdata.put(Key_Quantity, String.valueOf(Quantity));
                productdata.put("Product_type", product_type);
                if (product_type.equals("bundle")) {
                    JSONObject object = c.getJSONObject("bundle_options");
                    JSONArray option_names = object.names();
                    JSONArray option_values = object.toJSONArray(option_names);
                    ArrayList<String> data = new ArrayList<>();
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
                            data.add(datatoshow);
                        }
                    }
                    bundledata.put(item_id, data);
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
            }
            if (finalconfigdata.size() > 0 && bundledata.size() <= 0) {
                Log.i("ProductData", "In");
                Log.i("ProductData", "In" + finalconfigdata);
                listViewAdapter = new Ced_CartListAdapter(this, ProductData, finalconfigdata, true);
            } else {
                if (bundledata.size() > 0 && finalconfigdata.size() <= 0) {
                    Log.i("ProductData", "In2");
                    listViewAdapter = new Ced_CartListAdapter(this, ProductData, bundledata, "Bundle", true);
                } else {
                    if (bundledata.size() > 0 && finalconfigdata.size() > 0) {
                        Log.i("ProductData", "In3");
                        listViewAdapter = new Ced_CartListAdapter(this, ProductData, bundledata, finalconfigdata, true);
                    } else {
                        Log.i("ProductData", "In4");
                        listViewAdapter = new Ced_CartListAdapter(this, ProductData, true);
                    }
                }
            }
            magenativeActivityCartListingBinding.cartRecycler.setAdapter(listViewAdapter);
            magenativeActivityCartListingBinding.cartRecycler.setItemViewCacheSize(ProductData.size());
            listViewAdapter.notifyDataSetChanged();
            //MageNative_checkout
            magenativeActivityCartListingBinding.MageNativeCheckout.setOnClickListener(v -> {
                if (cartListing2Binding.errorLayout.getVisibility() == View.GONE) {
                    if (session.isLoggedIn()) {
                        cedhandlecheckout();
                    } else {
                        if (allowed_guest_checkout.equals("1") && !isHavingdownloadable) {
                            final Dialog listDialog = new Dialog(Ced_CartListing.this, R.style.PauseDialog);
                            ((ViewGroup) ((ViewGroup) Objects.requireNonNull(listDialog.getWindow()).getDecorView()).getChildAt(0))
                                    .getChildAt(1)
                                    .setBackgroundColor(Ced_CartListing.this.getResources().getColor(R.color.lighter_gray));
                            listDialog.setTitle(Html.fromHtml(getResources().getString(R.string.logintype)));
                            LayoutInflater li = (LayoutInflater) Ced_CartListing.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            MagenativeCustomOptionsBinding magenativeCustomOptionsBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_custom_options, null, false);
                            RadioButton Guest = magenativeCustomOptionsBinding.Guest;
                            RadioButton User = magenativeCustomOptionsBinding.User;
                            Guest.setButtonDrawable(checkbox_visibility);
                            User.setButtonDrawable(checkbox_visibility);
                            Guest.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                if (isChecked) {
                                    listDialog.dismiss();
                                    cedhandlecheckout();
                                }
                            });
                            User.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                if (isChecked) {
                                    listDialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), Ced_Login.class);
                                    intent.putExtra("isHavingdownloadable", true);
                                    intent.putExtra("Checkout", "CheckoutModule");
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                }
                            });
                            listDialog.setContentView(magenativeCustomOptionsBinding.getRoot());
                            listDialog.setCancelable(true);
                            listDialog.show();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), Ced_Login.class);
                            intent.putExtra("isHavingdownloadable", isHavingdownloadable);
                            intent.putExtra("Checkout", "CheckoutModule");
                            startActivity(intent);
                            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        }
                    }
                } else {
                    final Animation animShake = AnimationUtils.loadAnimation(Ced_CartListing.this, R.anim.shake2);
                    cartListing2Binding.errorLayout.startAnimation(animShake);
                }
            });

            if (!(couponcodedata.isEmpty())) {
                magenativeActivityCartListingBinding.MageNativeApplycoupantag.setText(couponcodedata);
                magenativeActivityCartListingBinding.MageNativeApplycoupan.setText(getResources().getString(R.string.removecoupon));
            }

            if (is_discount.equals("true")) {
                magenativeActivityCartListingBinding.MageNativeApplycoupantag.setText(coupon);
                magenativeActivityCartListingBinding.MageNativeApplycoupan.setText(getResources().getString(R.string.removecoupon));
                magenativeActivityCartListingBinding.couponheader.setVisibility(View.VISIBLE);
            }


            if (jsonObj.getJSONObject(KEY_ITEM).has("segments")) {
                JSONArray segments = jsonObj.getJSONObject(KEY_ITEM).getJSONArray("segments");
                for (int k = 0; k < segments.length(); k++) {
//                    FooterpricelayoutBinding footerPriceLayoutBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.footerpricelayout, null, false);
                    NewFooterPriceLayoutBinding footerPriceLayoutBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.new_footer_price_layout, null, false);
                    set_regular_font_fortext(footerPriceLayoutBinding.amount);
                    JSONObject object = segments.getJSONObject(k);

                    if (object.getString("label").contains("Grand Total") || object.getString("label").equalsIgnoreCase("Grand Total")) {
                        //that is to show the grand total at the bottom of the price sections
                        magenativeActivityCartListingBinding.grandTotalNew.setText(object.getString("value"));
                        magenativeActivityCartListingBinding.total.setText(object.getString("label"));
                        //that is to show the grand total at the bottom of the price sections end
                        //this is for the bottom view
                        magenativeActivityCartListingBinding.grandamount.setText(object.getString("value"));
                    } else {
                        footerPriceLayoutBinding.label.setText(object.getString("label"));
                        footerPriceLayoutBinding.amount.setText(object.getString("value"));
                        magenativeActivityCartListingBinding.allprices.addView(footerPriceLayoutBinding.getRoot());
                    }
                }
            }

            cartListing2Binding.parentlayout.addView(magenativeActivityCartListingBinding.getRoot());
        } catch (Exception w) {
            w.printStackTrace();
            Intent intent = new Intent(getApplicationContext(), Ced_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    private void clearallcart() {
        JsonObject deletefromcart = new JsonObject();
        if (session.isLoggedIn()) {
            deletefromcart.addProperty("customer_id", session.getCustomerid());
        } else {
            deletefromcart.addProperty("cart_id", cedSessionManagement.getCartId());
        }
        cartViewModel.emptyCart(Ced_CartListing.this, deletefromcart).observe(Ced_CartListing.this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                    try {
                        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(apiResponse.data));
                        if (jsonObject.has("header") && jsonObject.getString("header").equals("false")) {
                            Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        } else {
                            if (jsonObject.getBoolean("success")) {
                                Ced_MainActivity.latestcartcount = "0";
                                changecartcount();
                                //showmsg(jsonObject.getString("message"));
                                cedSessionManagement.clearcartId();
                                request();
                            } else {
                                if (jsonObject.has("message"))
                                    showmsg(jsonObject.getString("message"));
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

    private void CouponResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                try {
                    JSONObject object = new JSONObject(Objects.requireNonNull(apiResponse.data));
                    if (object.getJSONObject("cart_id").getString("success").equals("true")) {
                        showmsg(object.getJSONObject("cart_id").getString("message"));
                        request();
//                        Intent intent = new Intent(getApplicationContext(), Ced_CartListing.class);
//                        intent.putExtra("couponcode",  magenativeActivityCartListingBinding.MageNativeApplycoupantag.getText().toString());
//                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
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
    public void onBackPressed() {
        invalidateOptionsMenu();
        super.onBackPressed();
    }

    public void changecartcount() {
        invalidateOptionsMenu();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2777) {
            request();
        }
    }
}