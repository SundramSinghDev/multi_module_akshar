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
 *           * @license      http://cedcommerce.com/license-agreement.txt
 *
 */

package com.akshar.store.ui.orderssection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshar.store.databinding.NewOrderViewLayoutBinding;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.JsonObject;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import com.akshar.store.databinding.MagenativeOrderviewBinding;
import com.akshar.store.ui.cartsection.activity.Ced_CartListing;
import com.akshar.store.ui.orderssection.adapter.Ced_OrderViewAdapter;
import com.akshar.store.ui.orderssection.viewmodel.OrderViewModel;
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

/**
 * Created by Developer on 1/29/2016.
 */
@AndroidEntryPoint
public class Ced_Orderview extends Ced_NavigationActivity {
    //    MagenativeOrderviewBinding orderviewBinding;
    NewOrderViewLayoutBinding orderviewBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    OrderViewModel orderViewModel;
    String order_id;
    JsonObject hashMap;
    JSONArray orderdetail, ordered_item = null;
    JSONObject jsonObject = null;
    public static final String KEY_ITEM = "data";
    public static final String KEY_NAME = "orderview";
    public static final String KEY_ORDERED_ITEM = "ordered_items";
    public static final String KEY_ORDER_DATE = "orderdate";
    public static final String KEY_SHIP_TO = "ship_to";
    public static final String KEY_ORDER_LABEL = "orderlabel";
    public static final String KEY_STREET = "street";
    public static final String KEY_CITY = "city";
    public static final String KEY_STATE = "state";
    public static final String KEY_PINCODE = "pincode";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_SHIPPING_METHOD = "shipping_method";
    public static final String KEY_METHOD_TITLE = "method_title";
    public static final String KEY_METHOD_CODE = "method_code";
    public static final String KEY_PRODUCT_ID = "product_id";
    public static final String KEY_PRODUCT_NAME = "product_name";
    public static final String KEY_PRODUCT_PRICE = "product_price";
    public static final String KEY_PRODUCT_IMAGE = "product_image";
    public static final String KEY_PRODUCT_QTY = "product_qty";
    public static final String KEY_ROW_SUB_TOTAL = "rowsubtotal";
    public static final String KEY_SUB_TOTAL = "subtotal";
    public static final String KEY_SHIPPING = "shipping";
    public static final String KEY_TAX_AMOUNT = "tax_amount";
    public static final String KEY_DISCOUNT = "discount";
    public static final String KEY_GRAND_TOTAL = "grandtotal";
    public static final String KEY_PRODUCT_TYPE = "product_type";
    public static final String KEY_OPTIONS_SELECTED = "option";
    public static final String KEY_LABEL = "option_title";
    public static final String KEY_VALUE = "option_value";
    ArrayList<HashMap<String, String>> Orderinfo;
    Ced_OrderViewAdapter orderViewAdapter;
    String Url = "";
    String orderdate, orderlabel, shipto, street, city, state, pincode, country, mobile, shippingmethod, methodcode, methodtitle, product_id, product_name, product_price, product_image, product_qty, rowsubtotal, subtotal, tax_amount, shipping, discount, grandtotal, product_type;
    TextView items_text, items_text_value, paidamount, OrderId, Order_date, ship_to, Street, City, State, Pincode, Country, Mobile, method, payment_method, Subtotal, Tax_amount, Shipping_Charges, Discount, Grand_Total;
    ScrollView mainScrollView;
    RecyclerView ordersRecycler;
    private Tracker mTracker;
    HashMap<String, HashMap<String, String>> finalconfigdata;
    int items = 0;
    String order_number, order_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderViewModel = new ViewModelProvider(Ced_Orderview.this, viewModelFactory).get(OrderViewModel.class);
//        orderviewBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_orderview, content, true);
        orderviewBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.new_order_view_layout, content, true);
        showBackButtonWithTitle("Orders Details");
        hidebottombar();
        hashMap = new JsonObject();
        finalconfigdata = new HashMap<>();
        Orderinfo = new ArrayList<>();
        paidamount = orderviewBinding.paidamount;
        items_text = orderviewBinding.itemsText;
        items_text_value = orderviewBinding.itemsTextValue;
        OrderId = orderviewBinding.MageNativeOrderId;
        mainScrollView = orderviewBinding.MageNativeMainScrollView;
        mainScrollView.fullScroll(ScrollView.FOCUS_UP);
        Order_date = orderviewBinding.MageNativeOrderDate;
        ship_to = orderviewBinding.MageNativeShipTo;
        Street = orderviewBinding.MageNativeStreet;
        City = orderviewBinding.MageNativeCity;
        State = orderviewBinding.MageNativeState;
        Pincode = orderviewBinding.MageNativePincode;
        Country = orderviewBinding.MageNativeCountry;
        Mobile = orderviewBinding.MageNativeMobile;
        method = orderviewBinding.MageNativeMethod;
        payment_method = orderviewBinding.MageNativePaymentMethod;
        Subtotal = orderviewBinding.MageNativeSubtotal;
        Tax_amount = orderviewBinding.MageNativeTaxAmount;
        Shipping_Charges = orderviewBinding.MageNativeShippingCharges;
        Discount = orderviewBinding.MageNativeDiscount;
        Grand_Total = orderviewBinding.MageNativeGrandTotal;
//        orderplaced = orderviewBinding.MageNative_orderplaced);
        ordersRecycler = orderviewBinding.ordersRecycler;
        ordersRecycler.setLayoutManager(new LinearLayoutManager(Ced_Orderview.this, RecyclerView.VERTICAL, false));

        Intent detail = getIntent();
        order_id = detail.getStringExtra("orderid");

        try {
            hashMap.addProperty("order_id", order_id);
            hashMap.addProperty("customer_id", session.getCustomerid());
            if (cedSessionManagement.getStoreId() != null) {
                hashMap.addProperty("store_id", cedSessionManagement.getStoreId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        orderViewModel.getOrderViewData(Ced_Orderview.this, hashMap, session.getHahkey()).observe(this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                    fetchdata(Objects.requireNonNull(apiResponse.data));
                    break;

                case ERROR:
                    Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                    showmsg(getResources().getString(R.string.errorString));
                    break;
            }
        });

        orderviewBinding.reorderBtn.setOnClickListener(v -> {
            JsonObject param = new JsonObject();
            param.addProperty("order_id", order_id);
            param.addProperty("customer_id", session.getCustomerid());
            if (cedSessionManagement.getStoreId() != null) {
                param.addProperty("store_id", cedSessionManagement.getStoreId());
            }
            orderViewModel.getReorder(Ced_Orderview.this, param, session.getHahkey()).observe(Ced_Orderview.this, apiResponse -> {
                switch (apiResponse.status) {
                    case SUCCESS:
                        try {
                            JSONObject data = new JSONObject(Objects.requireNonNull(apiResponse.data));
                            if (data.has("cart_id")) {
                                if (data.getJSONObject("cart_id").getString("success").equals("true")) {
                                    cedSessionManagement.savecartid(data.getJSONObject("cart_id").getString("cart_id"));
                                    orderviewBinding.reorderBtn.setText(getResources().getString(R.string.re_ordered));
                                    Intent intent = new Intent(getApplicationContext(), Ced_CartListing.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                } else {
                                    if (data.getJSONObject("cart_id").has("message")) {
                                        showmsg(data.getJSONObject("cart_id").getString("message"));
                                    }
                                }
                            } else if (data.has("message")) {
                                showmsg(data.getString("message"));
                            }

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case ERROR:
                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                        showmsg(getResources().getString(R.string.errorString));
                        break;
                }
            });
        });

//        request();
    }

    private void fetchdata(String out) {
        try {
            jsonObject = new JSONObject(out);
            if (jsonObject.has("header") && jsonObject.getString("header").equals("false")) {
                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                mainScrollView.setVisibility(View.VISIBLE);
                orderdetail = jsonObject.getJSONObject(KEY_ITEM).getJSONArray(KEY_NAME);
                for (int i = 0; i < orderdetail.length(); i++) {
                    JSONObject c = null;
                    c = orderdetail.getJSONObject(i);
                    orderdate = c.getString(KEY_ORDER_DATE);
                    orderlabel = c.getString(KEY_ORDER_LABEL);

                    if (c.has(KEY_SHIP_TO))
                        shipto = c.getString(KEY_SHIP_TO);
                    if (c.has(KEY_STREET)) {
                        if (c.getString(KEY_STREET).contains("{")) {
                            street = String.valueOf(new JSONObject(c.getString(KEY_STREET)).get("0"));
                        } else {
                            street = c.getString(KEY_STREET);
                        }
                    }
                    if (c.has(KEY_CITY))
                        city = c.getString(KEY_CITY);
                    if (c.has(KEY_STATE))
                        state = c.getString(KEY_STATE);
                    if (c.has(KEY_PINCODE))
                        pincode = c.getString(KEY_PINCODE);
                    if (c.has(KEY_COUNTRY))
                        country = c.getString(KEY_COUNTRY);
                    if (c.has(KEY_MOBILE))
                        mobile = c.getString(KEY_MOBILE);
                    if (c.has(KEY_SHIPPING_METHOD))
                        shippingmethod = c.getString(KEY_SHIPPING_METHOD);
                    if (c.has(KEY_METHOD_CODE))
                        methodcode = c.getString(KEY_METHOD_CODE);
                    if (c.has(KEY_METHOD_TITLE))
                        methodtitle = c.getString(KEY_METHOD_TITLE);
                    subtotal = c.getString(KEY_SUB_TOTAL);
                    tax_amount = c.getString(KEY_TAX_AMOUNT);
                    shipping = c.getString(KEY_SHIPPING);
                    discount = c.getString(KEY_DISCOUNT);
                    grandtotal = c.getString(KEY_GRAND_TOTAL);
                    ordered_item = c.getJSONArray(KEY_ORDERED_ITEM);
                    if (c.has("incl_of_igst")) {
                        orderviewBinding.MageNativeIGST.setText(c.getString("incl_of_igst"));
                    } else {
                        orderviewBinding.parentOfIGST.setVisibility(View.GONE);
                    }
                    if (c.has("incl_of_sgst")) {
                        orderviewBinding.MageNativeSGST.setText(c.getString("incl_of_sgst"));
                    } else {
                        orderviewBinding.parentOfSGST.setVisibility(View.GONE);
                    }
                    if (c.has("incl_of_cgst")) {
                        orderviewBinding.MageNativeCGST.setText(c.getString("incl_of_cgst"));
                    } else {
                        orderviewBinding.parentOfCGST.setVisibility(View.GONE);
                    }

                    items = ordered_item.length();
                    items_text_value.setText(items + " " + getResources().getString(R.string.items));

                    for (int j = 0; j < ordered_item.length(); j++) {
                        JSONObject data = null;
                        data = ordered_item.getJSONObject(j);
                        product_id = data.getString(KEY_PRODUCT_ID);
                        product_name = data.getString(KEY_PRODUCT_NAME);
                        product_price = data.getString(KEY_PRODUCT_PRICE);
                        product_image = data.getString(KEY_PRODUCT_IMAGE);
                        product_qty = data.getString(KEY_PRODUCT_QTY);
                        rowsubtotal = data.getString(KEY_ROW_SUB_TOTAL);
                        if (data.has(KEY_PRODUCT_TYPE)) {
                            product_type = data.getString(KEY_PRODUCT_TYPE);
                        } else {
                            product_type = "";
                        }

                        HashMap<String, String> order = new HashMap<>();
                        order.put(KEY_PRODUCT_ID, product_id);
                        order.put(KEY_PRODUCT_NAME, product_name);
                        order.put(KEY_PRODUCT_PRICE, product_price);
                        order.put(KEY_PRODUCT_IMAGE, product_image);
                        order.put(KEY_PRODUCT_QTY, product_qty);
                        order.put(KEY_ROW_SUB_TOTAL, rowsubtotal);
                        order.put(KEY_PRODUCT_TYPE, product_type);
                        Orderinfo.add(order);

                        if (product_type.equals("configurable") || product_type.equals("bundle") || product_type.equals("downloadable")) {
                            if (data.has(KEY_OPTIONS_SELECTED)) {
                                JSONArray object = data.getJSONArray(KEY_OPTIONS_SELECTED);
                                HashMap<String, String> config_data = new HashMap<>();
                                for (int option = 0; option < object.length(); option++) {
                                    JSONObject jsonObject = object.getJSONObject(option);
                                    if (product_type.equals("downloadable")) {
                                        String label = jsonObject.getString("link_title");
                                        config_data.put(label, label);
                                    } else {
                                        String label = jsonObject.getString(KEY_LABEL);
                                        String value = jsonObject.getString(KEY_VALUE);
                                        config_data.put(value, label);
                                    }
                                }
                                finalconfigdata.put(product_id, config_data);
                            }
                        }
                    }
                }
                if (finalconfigdata.size() > 0) {
                    orderViewAdapter = new Ced_OrderViewAdapter(Ced_Orderview.this, Orderinfo);
                }
                orderViewAdapter = new Ced_OrderViewAdapter(Ced_Orderview.this, Orderinfo, finalconfigdata);
                ordersRecycler.setAdapter(orderViewAdapter);
//                set_regular_font_fortext(orderviewBinding.MageNativeShippingText);
                set_bold_font_fortext(orderviewBinding.MageNativeShippingText);
                set_regular_font_fortext(orderviewBinding.MageNativeAddress);
                set_regular_font_fortext(orderviewBinding.MageNativeMethod);
                set_regular_font_fortext(orderviewBinding.MageNativeOrderStatusLabel);
                set_bold_font_fortext(orderviewBinding.MageNativeMobileLabel);
                set_bold_font_fortext(orderviewBinding.MageNativeOrderStatus);
                set_bold_font_fortext(orderviewBinding.shippingDetailsLabel);
                set_bold_font_fortext(orderviewBinding.paymentDetailsLabel);
                set_bold_font_fortext(items_text);
                set_regular_font_fortext(items_text_value);
                set_bold_font_fortext(OrderId);
                set_bold_font_fortext(Order_date);
                set_regular_font_fortext(paidamount);
                set_bold_font_fortext(ship_to);
                set_regular_font_fortext(Street);
                set_regular_font_fortext(City);
                set_regular_font_fortext(State);
                set_regular_font_fortext(Pincode);
                set_regular_font_fortext(Country);
                set_regular_font_fortext(Mobile);
                set_regular_font_fortext(method);
                set_regular_font_fortext(orderviewBinding.itemsTextValue);
                set_regular_font_fortext(payment_method);
                set_regular_font_fortext(Subtotal);
                set_regular_font_fortext(Tax_amount);
                set_regular_font_fortext(Shipping_Charges);
                set_regular_font_fortext(orderviewBinding.MageNativeCGST);
                set_regular_font_fortext(orderviewBinding.MageNativeSGST);
                set_regular_font_fortext(orderviewBinding.MageNativeIGST);
                set_regular_font_fortext(Discount);
                if (orderlabel != null) {
                    String[] order_label = orderlabel.split("-");
                    orderviewBinding.MageNativeOrderStatus.setText(order_label[1]);
                    OrderId.setText(order_label[0].replace("Order", ""));
                }
                Order_date.setText(getResources().getString(R.string.placedontext) + " " + orderdate);
                ship_to.setText(shipto);
                Street.setText(street);
                City.setText(city);
                State.setText(state);
                Pincode.setText(pincode);
                Country.setText(country);
                Mobile.setText(mobile);
                method.setText(shippingmethod);

                orderviewBinding.MageNativeAddress.setText(street + "\n" + city + "\n" + state + "\n" + country + " " + pincode);
//                payment_method.setText(getResources().getString(R.string.tobepaid) + " " + methodtitle);
                orderviewBinding.itemsTextValue.setText(getResources().getString(R.string.tobepaid) + " " + methodtitle);

                Subtotal.setText(subtotal);
                Tax_amount.setText(tax_amount);
                Shipping_Charges.setText(shipping);
                Discount.setText(discount);
                Grand_Total.setText(grandtotal);
                paidamount.setText(grandtotal);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        super.onResume();
    }
}