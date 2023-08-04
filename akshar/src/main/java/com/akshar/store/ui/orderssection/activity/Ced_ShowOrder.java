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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshar.store.ui.orderssection.adapter.OrderRecyclerAdapterNew;
import com.akshar.store.ui.orderssection.datamodel.Order_Item_Model;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.rest.ApiResponse;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import com.akshar.store.databinding.MagenativeShoworderPageBinding;
import com.akshar.store.databinding.NoorderBinding;
import com.akshar.store.ui.loginsection.activity.Ced_Login;
import com.akshar.store.ui.orderssection.adapter.OrderRecyclerAdapter;
import com.akshar.store.ui.orderssection.viewmodel.OrderViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Created by developer on 9/27/2015.
 */
@AndroidEntryPoint
public class Ced_ShowOrder extends Ced_NavigationActivity {
    MagenativeShoworderPageBinding orderPageBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    OrderViewModel orderViewModel;
    JsonObject hashmap;
    String Url = "";
    static final String KEY_ITEM = "data";
    static final String KEY_NAME = "orderdata";
    static final String KEY_TOTAL_AMOUNT = "total_amount";
    static final String KEY_ID = "order_id";
    static final String KEY_DATE = "date";
    static final String KEY_SHIP_TO = "ship_to";
    static final String KEY_ORDER_ID = "number";
    static final String KEY_ORDER_STATUS = "order_status";
    static final String KEY_STATUS = "status";
    RecyclerView OrderList;
    ArrayList<HashMap<String, String>> Orderinfo;
    int current = 1;
    private boolean loading = false;
    HashMap<HashMap<String, String>, HashMap<String, String>> orderdata;
    HashMap<String, HashMap<String, String>> order_data_map;
    ArrayList<String> order_data_list;
    private boolean firstTime = true;
//    private OrderRecyclerAdapter recyclerAdapter;
    private OrderRecyclerAdapterNew recyclerAdapter;
    private final Gson converter = new Gson();
    private final Type type = new TypeToken<List<Order_Item_Model>>() {
    }.getType();
    private List<Order_Item_Model> orderModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackButtonWithTitle("My Orders");
        orderViewModel = new ViewModelProvider(Ced_ShowOrder.this, viewModelFactory).get(OrderViewModel.class);
        hashmap = new JsonObject();
        Orderinfo = new ArrayList<>();
        orderdata = new HashMap<>();
        order_data_map = new HashMap<>();
        order_data_list = new ArrayList<>();

        if (session.isLoggedIn()) {
            orderPageBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_showorder_page, content, true);
            OrderList = orderPageBinding.MageNativeOrderlist;
            try {
                hashmap.addProperty("customer_id", session.getCustomerid());
                hashmap.addProperty("page", current);
                if (cedSessionManagement.getStoreId() != null) {
                    hashmap.addProperty("store_id", cedSessionManagement.getStoreId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            OrderList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && !loading) {
                        Log.i("onScrollStateChanged", "loadmore");
                        loading = true;
                        //-------------------
                        current = current + 1;
                        hashmap.addProperty("page", current);
                        orderViewModel.getOrdersData(Ced_ShowOrder.this, hashmap,session.getHahkey()).observe(Ced_ShowOrder.this, Ced_ShowOrder.this::Response);
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });

            orderViewModel.getOrdersData(Ced_ShowOrder.this, hashmap,session.getHahkey()).observe(Ced_ShowOrder.this,Ced_ShowOrder.this::Response);

        } else {
            Intent goto_login = new Intent(getApplicationContext(), Ced_Login.class);
            goto_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            goto_login.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            finish();
            startActivity(goto_login);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    private void Response(ApiResponse apiResponse) {
            switch (apiResponse.status){
                case SUCCESS:
                    applydata(apiResponse.data);
                    break;

                case ERROR:
                    Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                    showmsg(getResources().getString(R.string.errorString));
                    break;
            }

    }

    @SuppressLint("NotifyDataSetChanged")
    private void applydata(String Jstring) {
        try {
            JSONObject jsonObject = new JSONObject(Jstring);
            if (jsonObject.has("header") && jsonObject.getString("header").equals("false")) {
                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                if (jsonObject.getJSONObject(KEY_ITEM).getString(KEY_STATUS).equals("no_order") && current==1) {
                    NoorderBinding noorderBinding= DataBindingUtil.inflate(getLayoutInflater(),R.layout.noorder, content,true);
                    noorderBinding.conti.setOnClickListener(v -> finish());
                } else {
                    if (firstTime) {
                        firstTime = false;
                        if (jsonObject.getJSONObject(KEY_ITEM).getString(KEY_STATUS).equals("no_order") && current == 1) {
                            NoorderBinding noorderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.noorder, content, true);
                            noorderBinding.conti.setOnClickListener(v -> finish());
                        }
                        else {
                            loading = false;
                            orderModelList = converter.fromJson(jsonObject.getJSONObject(KEY_ITEM).getJSONArray(KEY_NAME).toString(), type);
                            recyclerAdapter = new OrderRecyclerAdapterNew(Ced_ShowOrder.this, orderModelList);
                            OrderList.setLayoutManager(new LinearLayoutManager(Ced_ShowOrder.this, RecyclerView.VERTICAL, false));
                            OrderList.setAdapter(recyclerAdapter);
                        }
                    } else {
                        if (jsonObject.getJSONObject(KEY_ITEM).getString(KEY_STATUS).equals("no_order")) {
                            showmsg(getString(R.string.nomoreorders));
                            loading = false;
                        } else {
                            loading = false;
                            if (jsonObject.getJSONObject(KEY_ITEM).has(KEY_NAME)) {
                                orderModelList = converter.fromJson(jsonObject.getJSONObject(KEY_ITEM).getJSONArray(KEY_NAME).toString(), type);
                                recyclerAdapter.data.addAll(orderModelList);
                                recyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
//        OrderList.setIndicatorBounds(OrderList.getRight() - 40, OrderList.getWidth());
        invalidateOptionsMenu();
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        OrderList.setIndicatorBounds(OrderList.getRight() - 40, OrderList.getWidth());
    }

}