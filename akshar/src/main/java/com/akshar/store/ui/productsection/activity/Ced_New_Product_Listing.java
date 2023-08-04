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
package com.akshar.store.ui.productsection.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;


import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.base.utilapplication.AnalyticsApplication;
import com.akshar.store.ui.productsection.adapter.Ced_SortAdapter;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import com.akshar.store.databinding.NewProductListingBinding;
import com.akshar.store.ui.productsection.adapter.RecyclerGridViewAdapter;
import com.akshar.store.rest.ApiResponse;
import com.akshar.store.ui.productsection.adapter.Sub_category_recycler_Adapter;
import com.akshar.store.ui.productsection.viewmodel.ProductListViewModel;
import com.akshar.store.ui.searchsection.activity.Ced_Searchview;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ced_New_Product_Listing extends Ced_NavigationActivity {
    NewProductListingBinding productListingBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    ProductListViewModel productListViewModel;
    String category_id;
    JsonObject dataforproductlist;
    String theme = "";
    boolean list = false;
    boolean gridlayout = true;
    ArrayList<HashMap<String, String>> keyvalsort2;
    Dialog listDialog;
    Ced_SortAdapter cedSortAdapter;
    public static String selectedorder = " ";
    public static String selecteddir = " ";
    public static String order = "";
    public static String dir = "";
    boolean filteravailable = false;
    GridLayoutManager gridLayoutManager;
    ArrayList<HashMap<String, String>> gridviewdata;
    RecyclerGridViewAdapter mageNative_gridViewAdapter;
    JSONArray filterlabel = null;
    JSONArray filters = null;
    JSONArray sub_category_array = new JSONArray();
    boolean load = true;
    private boolean loading = false;
    int current = 1;
    JSONObject keyvalfilter;
    JSONObject keyvalfilter2;
    HashMap<String, String> filterlabelcode;
    HashMap<String, String> filtercodelabel;
    boolean filterapplied = false;
    boolean firstselected = true;
    boolean tagstoshow = false, isClearFilterClicked = false;
    JSONObject filterData;
    BottomSheetBehavior<LinearLayout> filterSheetBehavior;
    BottomSheetBehavior<LinearLayout> sortSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productListingBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.new_product_listing, content, true);
        productListViewModel = new ViewModelProvider(Ced_New_Product_Listing.this, viewModelFactory).get(ProductListViewModel.class);
        keyvalsort2 = new ArrayList<>();
        theme = cedSessionManagement.getcategorytheme();
        gridviewdata = new ArrayList<>();
        dataforproductlist = new JsonObject();
        category_id = getIntent().getStringExtra("ID");
        gridLayoutManager = new GridLayoutManager(Ced_New_Product_Listing.this, 2, RecyclerView.VERTICAL, false);
        productListingBinding.newlistlayout.recycler.setHasFixedSize(false);
        productListingBinding.newlistlayout.MageNativeSubcategoriesListing.setHasFixedSize(true);
        productListingBinding.newlistlayout.MageNativeSubcategoriesListing.setLayoutManager(new GridLayoutManager(Ced_New_Product_Listing.this, 3, RecyclerView.VERTICAL, false));
        productListingBinding.newlistlayout.recycler.setLayoutManager(gridLayoutManager);
        filterSheetBehavior = BottomSheetBehavior.from(productListingBinding.filterSheet.filterSheetLayout);
        sortSheetBehavior = BottomSheetBehavior.from(productListingBinding.sortSheet.sortSheetLayout);
        setbottomsheet(filterSheetBehavior, productListingBinding.newlistlayout.view);
        setbottomsheet(sortSheetBehavior, productListingBinding.newlistlayout.view);
        productListingBinding.newlistlayout.conti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        productListingBinding.newlistlayout.MageNativeFiltersection.setOnClickListener(v -> {

            productListingBinding.filterSheet.MageNativeFilterscroll.fullScroll(View.FOCUS_UP);
            getFilterData();
            if (filterSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                filterSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                filterSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            sortSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });

        productListingBinding.newlistlayout.MageNativeSortsection.setOnClickListener(v -> {
            try {
                if (keyvalsort2.size() > 0) {
                    showdialog();
                    if (sortSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        sortSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    } else {
                        sortSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    filterSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    showmsg(getResources().getString(R.string.sortoptionsnotavailable));
                }
            } catch (Exception e) {
                Log.i("FilterLog:", "IN");
                e.printStackTrace();
            }
        });
        productListingBinding.newlistlayout.MageNativeSubcatselectsection.setOnClickListener(v -> {
            try {
                if (sub_category_array.length() > 0) {
                    Intent intent = new Intent(Ced_New_Product_Listing.this, SubCategory_SelectPage.class);
                    intent.putExtra("sub_category", sub_category_array.toString());
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                } else {
                    showmsg(getResources().getString(R.string.nosubcategory_toexplore));
                }
            } catch (Exception e) {
                Log.i("FilterLog:", "IN");
                e.printStackTrace();
            }
        });
        productListingBinding.newlistlayout.view.setOnClickListener(view -> {
            if (filterSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                filterSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                productListingBinding.newlistlayout.view.setVisibility(View.GONE);
            }

            if (sortSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                sortSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                productListingBinding.newlistlayout.view.setVisibility(View.GONE);
            }
        });
        productListingBinding.filterSheet.MageNativeClearfilter.setOnClickListener(view -> {
            if (filterSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                filterSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                productListingBinding.newlistlayout.view.setVisibility(View.GONE);
            }
            filterapplied = true;
            AnalyticsApplication.main_filters.clear();
            selecteddir = "";
            selectedorder = "";
            isClearFilterClicked = true;
            productListingBinding.filterSheet.MageNativeFiltervalues.removeAllViews();
            productListingBinding.filterSheet.MageNativeFiltertags.removeAllViews();
            dataforproductlist.remove("multi_filter");
            refreshListing(dataforproductlist);
        });
        productListingBinding.filterSheet.MageNativeApplyfilter.setOnClickListener(view -> {
            filterData = new JSONObject();
            if (filterSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                filterSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                productListingBinding.newlistlayout.view.setVisibility(View.GONE);
            }
            filterapplied = true;
            JSONObject object = new JSONObject();
            for (Map.Entry<String, ArrayList<String>> stringArrayListEntry : AnalyticsApplication.main_filters.entrySet()) {
                String key = String.valueOf(((Map.Entry) stringArrayListEntry).getKey());
                ArrayList<String> value = (ArrayList<String>) ((Map.Entry) stringArrayListEntry).getValue();
                Iterator<String> innerlist = value.iterator();
                JSONObject object1 = new JSONObject();
                while (innerlist.hasNext()) {
                    String id_name = innerlist.next();
                    String[] parts = id_name.split("#");
                    try {
                        object1.put(parts[0], parts[1]);
                    } catch (JSONException e) {
                        Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(main);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        e.printStackTrace();
                    }
                }
                try {
                    object.put(key, object1);
                } catch (JSONException e) {
                    Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(main);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    e.printStackTrace();
                }
            }
            try {
                if (order.isEmpty()) {
                    filterData.put("multifilter", object.toString());
                    filterData.put("ID", category_id);
//                filterData.put("filteravailable", true);
                    filterData.put("filterlabel", filterlabel.toString());
                    filterData.put("filters", filters.toString());
                } else {
                    filterData.put("multifilter", object.toString());
                    filterData.put("ID", category_id);
                    filterData.put("order", order);
                    filterData.put("dir", dir);
//                filterData.put("filteravailable", true);
                    filterData.put("filterlabel", filterlabel.toString());
                    filterData.put("filters", filters.toString());
                }
                filterData.put("MULTIURL", "FILTER");

                if (filterData.has("MULTIURL")) {
                    try {
                        dataforproductlist.addProperty("multi_filter", filterData.getString("multifilter"));
                        if (filterData.has("order")) {
                            dataforproductlist.addProperty("order", filterData.getString("order"));
                            dataforproductlist.addProperty("dir", filterData.getString("dir"));
                        }
                        if (filterData.has("filteravailable"))
                            filteravailable = filterData.getBoolean("filteravailable");
                        JSONArray array = null;
                        array = new JSONArray(Objects.requireNonNull(filterData.get("filterlabel")).toString());
                        Log.i("filterlabel", "" + array);
                        filterlabel = array;
                        filters = new JSONArray(Objects.requireNonNull(filterData.get("filters")).toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            refreshListing(dataforproductlist);
        });

        productListingBinding.newlistlayout.MageNativeChangeview.setOnClickListener(v -> {
            if (list) {

                productListingBinding.newlistlayout.MageNativeChangeview.setEnabled(false);
                gridlayout = true;
                productListingBinding.newlistlayout.MageNativeChangeview.setImageResource(R.drawable.list_icon);
                list = false;
                gridLayoutManager = new GridLayoutManager(Ced_New_Product_Listing.this, 2, RecyclerView.VERTICAL, false);
                productListingBinding.newlistlayout.recycler.setLayoutManager(gridLayoutManager);
                mageNative_gridViewAdapter = new RecyclerGridViewAdapter(Ced_New_Product_Listing.this, gridviewdata, gridlayout);
                productListingBinding.newlistlayout.recycler.setAdapter(mageNative_gridViewAdapter);
                /*mageNative_gridViewAdapter.notifyDataSetChanged();*/
                animateRecyclerLayoutChange(2, gridLayoutManager, productListingBinding.newlistlayout.recycler);
                productListingBinding.newlistlayout.MageNativeChangeview.setEnabled(true);
                /*ObjectAnimator animation = ObjectAnimator.ofFloat(productListingBinding.newlistlayout.MageNativeChangeview, "rotationY", 0.0f, 180f);
                animation.setDuration(100);
                animation.setRepeatCount(0);
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                animation.start();*/
            } else {
                productListingBinding.newlistlayout.MageNativeChangeview.setEnabled(false);
                gridlayout = false;
                productListingBinding.newlistlayout.MageNativeChangeview.setImageResource(R.drawable.grid_icon);
                list = true;
                gridLayoutManager = new GridLayoutManager(Ced_New_Product_Listing.this, 1, RecyclerView.VERTICAL, false);
                productListingBinding.newlistlayout.recycler.setLayoutManager(gridLayoutManager);
                mageNative_gridViewAdapter = new RecyclerGridViewAdapter(Ced_New_Product_Listing.this, gridviewdata, gridlayout);
                productListingBinding.newlistlayout.recycler.setAdapter(mageNative_gridViewAdapter);
                /*mageNative_gridViewAdapter.notifyDataSetChanged();*/
                animateRecyclerLayoutChange(1, gridLayoutManager, productListingBinding.newlistlayout.recycler);
                productListingBinding.newlistlayout.MageNativeChangeview.setEnabled(true);
                /*ObjectAnimator animation = ObjectAnimator.ofFloat(productListingBinding.newlistlayout.MageNativeChangeview, "rotationY", 0.0f, 180f);
                animation.setDuration(100);
                animation.setRepeatCount(0);
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                animation.start();*/
            }
        });
        productListingBinding.newlistlayout.nestedscroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {
                        int visibleItemCount = gridLayoutManager.getChildCount();
                        int totalItemCount = gridLayoutManager.getItemCount();
                        int pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                        if (!loading && load) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                Log.i("onScrollStateChanged", "loadmore");
                                loading = true;
                                //-------------------
                                current = current + 1;
                                load = false;
                                dataforproductlist.remove("page");
                                dataforproductlist.addProperty("page", current);
                                productListViewModel.getProductListData(Ced_New_Product_Listing.this, dataforproductlist)
                                        .observe(Ced_New_Product_Listing.this, Ced_New_Product_Listing.this::consumePaginationResponse);
                            }
                        }
                    }
                }
            }
        });
        try {
            dataforproductlist.addProperty("id", category_id);
            dataforproductlist.addProperty("page", "1");
            dataforproductlist.addProperty("theme", theme);
            if (cedSessionManagement.getStoreId() != null) {
                dataforproductlist.addProperty("store_id", cedSessionManagement.getStoreId());
            }
            if (session.isLoggedIn()) {
                dataforproductlist.addProperty("customer_id", session.getCustomerid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        productListViewModel.getProductListData(this, dataforproductlist).observe(this, this::consumeResponse);
    }

    private void refreshListing(JsonObject data) {
        gridviewdata.clear();
        productListingBinding.newlistlayout.recycler.removeAllViewsInLayout();
        productListViewModel.getProductListData(this, data).observe(this, this::consumeResponse);
    }

    private void consumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                if (Objects.requireNonNull(apiResponse.data).contains("NO_PRODUCTS") || Objects.requireNonNull(apiResponse.data).equalsIgnoreCase("[]")) {
                    productListingBinding.newlistlayout.noproductLayout.setVisibility(View.VISIBLE);
                        /*MagenativeNoProductListingBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_no_product_listing, content, true);
                        binding1.conti.setOnClickListener(v -> finish());*/
                } else {
                    applydata(Objects.requireNonNull(apiResponse.data));
                }

                break;

            case ERROR:
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }

    private void consumePaginationResponse(ApiResponse apiResponse) {
        if (filterSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            filterSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            productListingBinding.newlistlayout.view.setVisibility(View.GONE);
        } else if (sortSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sortSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            productListingBinding.newlistlayout.view.setVisibility(View.GONE);
        }
        switch (apiResponse.status) {
            case SUCCESS:
                loading = false;
                if (Objects.requireNonNull(apiResponse.data).contains("NO_PRODUCTS") || Objects.requireNonNull(apiResponse.data).equalsIgnoreCase("[]")) {
                    load = false;
                } else {
                    applydata(Objects.requireNonNull(apiResponse.data));
                }
                break;

            case ERROR:
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }

    public void applydata(String Jstring) {
        try {
            JSONObject jsonObj = new JSONObject(Jstring);
            if (jsonObj.has("header") && jsonObj.getString("header").equals("false")) {
                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                keyvalfilter2 = new JSONObject();
                filterlabelcode = new HashMap<>();
                filtercodelabel = new HashMap<>();
                if (jsonObj.getJSONObject("data").has("filter_label")) {
                    filterlabel = jsonObj.getJSONObject("data").getJSONArray("filter_label");
                }
                if (jsonObj.getJSONObject("data").has("filter")) {
                    filters = jsonObj.getJSONObject("data").getJSONArray("filter");
                }
                //----------subcategories data------------------------------
                if (jsonObj.getJSONObject("data").has("sub_category")) {
                    sub_category_array = jsonObj.getJSONObject("data").getJSONArray("sub_category");
                    if (sub_category_array.length() > 1) {
                        Sub_category_recycler_Adapter adapter = new Sub_category_recycler_Adapter(Ced_New_Product_Listing.this, sub_category_array);
                        productListingBinding.newlistlayout.MageNativeSubcategoriesListing.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }
                //----------subcategories data------------------------------
                sortdata(Jstring, jsonObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    public void sortdata(String Jstring, JSONObject json) {
        try {
            JSONObject jsonObj = new JSONObject(Jstring);
            if (jsonObj.getJSONObject("data").has("sort")) {
                JSONObject sorts = jsonObj.getJSONObject("data").getJSONObject("sort");
                if (keyvalsort2.size() > 0) {
                    keyvalsort2.clear();
                }
                Iterator<String> keys = sorts.keys();
                while (keys.hasNext()) {
                    String name = (String) keys.next();
                    String value = sorts.getString(name);
                    HashMap<String, String> keyvalsort = new HashMap<String, String>();
                    keyvalsort.put(name, value);
                    keyvalsort2.add(keyvalsort);
                }
            }

            createproductgrid(json);
        } catch (Exception e) {
            e.printStackTrace();
            Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    private void showdialog() {

        productListingBinding.sortSheet.MageNativeSortlistview.setOnItemClickListener((parent, view, position, id) -> {
            Ced_New_Product_Listing.order = "";
            JSONObject object = new JSONObject();
            TextView direction = view.findViewById(R.id.MageNative_sortDirection);
            TextView label = view.findViewById(R.id.MageNative_SortLabel);
            selectedorder = label.getText().toString();
            selecteddir = direction.getText().toString();
            String[] odr = selecteddir.split("#");
            String part1 = odr[0];
            String part2 = odr[1];
            Ced_New_Product_Listing.order = part1;
            Ced_New_Product_Listing.dir = part2;
            filterData = new JSONObject();
            try {
                if (AnalyticsApplication.main_filters.size() <= 0) {
                    filterData.put("order", order);
                    filterData.put("dir", dir);
                    filterData.put("ID", category_id);
                    filterData.put("keyvalsort2", keyvalsort2);
                    filterData.put("filterlabel", filterlabel.toString());
                    filterData.put("filters", filters.toString());
                    filterData.put("filteravailable", true);
                } else {
                    for (Map.Entry<String, ArrayList<String>> stringArrayListEntry : AnalyticsApplication.main_filters.entrySet()) {
                        String key = String.valueOf(((Map.Entry) stringArrayListEntry).getKey());
                        ArrayList<String> value = (ArrayList<String>) ((Map.Entry) stringArrayListEntry).getValue();
                        Iterator<String> innerlist = value.iterator();
                        JSONObject object1 = new JSONObject();
                        while (innerlist.hasNext()) {
                            String id_name = innerlist.next();
                            String[] parts = id_name.split("#");
                            try {
                                object1.put(parts[0], parts[1]);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
                                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(main);
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            }
                        }
                        try {
                            object.put(key, object1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
                            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(main);
                            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        }
                    }
                    filterData.put("order", order);
                    filterData.put("dir", dir);
                    filterData.put("ID", category_id);
                    filterData.put("keyvalsort2", keyvalsort2);
                    filterData.put("filterlabel", filterlabel.toString());
                    filterData.put("filters", filters.toString());
                    filterData.put("multifilter", object.toString());
                    filterData.put("filteravailable", true);
                }
                filterData.put("SORTURL", "SORT");

                if (Objects.requireNonNull(filterData).has("SORTURL")) {
                    try {
                        keyvalsort2 = (ArrayList<HashMap<String, String>>) filterData.get("keyvalsort2");
                        dataforproductlist.addProperty("order", filterData.getString("order"));
                        dataforproductlist.addProperty("dir", filterData.getString("dir"));
                        if (filterData.has("multifilter")) {
                            dataforproductlist.addProperty("multi_filter", filterData.getString("multifilter"));
                        }
                        filteravailable = filterData.getBoolean("filteravailable");

                        JSONArray array = null;

                        array = new JSONArray(Objects.requireNonNull(filterData.get("filterlabel")).toString());
                        Log.i("filterlabel", "" + array);
                        filterlabel = array;
                        filters = new JSONArray(Objects.requireNonNull(filterData.get("filters")).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            refreshListing(dataforproductlist);
        });
        cedSortAdapter = new Ced_SortAdapter(this, keyvalsort2, selectedorder, selecteddir);
        productListingBinding.sortSheet.MageNativeSortlistview.setAdapter(cedSortAdapter);
        /*listDialog.show();*/
    }

    private void getFilterData() {
        try {
            if (filters != null && filters.length() > 0) {
                for (int i = 0; i < filters.length(); i++) {
                    JSONObject obj2 = filters.getJSONObject(i);
                    JSONArray nameArray = obj2.names();
                    String name = Objects.requireNonNull(nameArray).getString(0);
                    JSONArray valArray = obj2.getJSONArray(name);
                    keyvalfilter = new JSONObject();
                    for (int j = 0; j < valArray.length(); j++) {
                        String value = valArray.getString(j);
                        String[] parts = value.split("#");

                        keyvalfilter.put(parts[0], parts[1]);
                    }
                    keyvalfilter2.put(name, keyvalfilter);
                }
                filterdatalabel();
            } else {
                showmsg(getResources().getString(R.string.filternotavailblefornow));
            }
        } catch (Exception w) {
            w.printStackTrace();
            Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    private void filterdatalabel() {
        try {
            for (int l = 0; l < filterlabel.length(); l++) {
                JSONObject obj2 = filterlabel.getJSONObject(l);
                filterlabelcode.put(obj2.getString("att_code"), obj2.getString("att_label"));
                if (filtercodelabel.containsKey(obj2.getString("att_label"))) {
                    JSONArray checkfilters = filters;
                    for (int i = 0; i < checkfilters.length(); i++) {
                        JSONObject obj3 = checkfilters.getJSONObject(i);
                        JSONArray nameArray = obj3.names();
                        if (Objects.requireNonNull(nameArray).get(0).toString().equals(obj2.getString("att_code"))) {
                            int subFiltercount = Objects.requireNonNull(obj3.toJSONArray(nameArray)).length();
                            if (subFiltercount > 0) {
                                filtercodelabel.remove(obj2.getString("att_label"));
                                filtercodelabel.put(obj2.getString("att_label"), obj2.getString("att_code"));
                            }
                        }
                    }
                } else {
                    filtercodelabel.put(obj2.getString("att_label"), obj2.getString("att_code"));
                }
            }
            listfilters();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listfilters() {
        Iterator<String> iterator = keyvalfilter2.keys();
        productListingBinding.filterSheet.MageNativeFiltertags.removeAllViews();

        while (iterator.hasNext()) {
           /* String
            Map.Entry pair = (Map.Entry) iterator.next();*/
            String key = String.valueOf(iterator.next());
            LinearLayout linebelowbutton = new LinearLayout(getApplicationContext());
            linebelowbutton.setOrientation(LinearLayout.VERTICAL);
            final Button button = new Button(getApplicationContext());
            final TextView label = new TextView(getApplicationContext());
            label.setVisibility(View.GONE);
            if (firstselected || isClearFilterClicked) {
                if (AnalyticsApplication.main_filters.containsKey(key)) {
                    button.setBackgroundColor(getResources().getColor(R.color.AppTheme));
                    button.setTextColor(getResources().getColor(R.color.txtapptheme_color));
                } else {
                    button.setBackgroundColor(getResources().getColor(R.color.txtapptheme_color));
                    button.setTextColor(getResources().getColor(R.color.AppTheme));
                }
                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                linearLayout.requestFocus();
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                JSONObject filtervalueshash = null;
                try {
                    filtervalueshash = keyvalfilter2.getJSONObject(key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Iterator<String> filter = Objects.requireNonNull(filtervalueshash).keys();
                while (filter.hasNext()) {
                    String filterid = String.valueOf(filter.next());
                    /* String filterid = String.valueOf(filterpair.getKey());*/
                    String filterval = "";
                    try {
                        filterval = filtervalueshash.getString(filterid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    LinearLayout layout = new LinearLayout(getApplicationContext());
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    final CheckBox filtervalue = new CheckBox(getApplicationContext());
                    filtervalue.setButtonDrawable(checkbox_visibility);
                    filtervalue.setText(Html.fromHtml(filterval));
                    filtervalue.setTextColor(getResources().getColor(R.color.black));
                    if (containskeyalready(filterval, key, filterid)) {
                        filtervalue.setChecked(true);
                    } else {
                        filtervalue.setChecked(false);
                    }
                    TextView filId = new TextView(getApplicationContext());
                    filId.setText(filterid);
                    filId.setVisibility(View.GONE);
                    layout.addView(filtervalue, 0);
                    layout.addView(filId, 1);
                    linearLayout.addView(layout);
                    filtervalue.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            button.setBackgroundColor(getResources().getColor(R.color.AppTheme));
                            button.setTextColor(getResources().getColor(R.color.txtapptheme_color));
                            if (AnalyticsApplication.main_filters.size() <= 0) {
                                ArrayList<String> list = new ArrayList<>();
                                LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                list.add(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());
                                AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);

                            } else {
                                if (AnalyticsApplication.main_filters.containsKey(filtercodelabel.get(button.getText().toString()))) {
                                    LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                    TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                    ArrayList<String> list = AnalyticsApplication.main_filters.get(filtercodelabel.get(button.getText().toString()));
                                    if (Objects.requireNonNull(list).contains(filter_ID.getText().toString() + "#" + filtervalue.getText().toString())) {
                                    } else {
                                        list.add(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());
                                        AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);
                                    }
                                } else {
                                    ArrayList<String> list = new ArrayList<>();
                                    LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                    TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                    list.add(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());

                                    AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);
                                }
                            }
                        } else {
                            boolean ischecked = false;
                            LinearLayout checkboxparent = (LinearLayout) filtervalue.getParent();
                            LinearLayout checkboxparentParent = (LinearLayout) checkboxparent.getParent();
                            int childcount = checkboxparentParent.getChildCount();
                            for (int child = 0; child <= childcount - 1; child++) {
                                LinearLayout layout1 = (LinearLayout) checkboxparentParent.getChildAt(child);
                                CheckBox box = (CheckBox) layout1.getChildAt(0);
                                if (box.isChecked()) {
                                    ischecked = true;
                                }
                            }
                            if (ischecked) {
                                button.setBackgroundColor(getResources().getColor(R.color.AppTheme));
                                button.setTextColor(getResources().getColor(R.color.txtapptheme_color));
                            } else {
                                button.setBackgroundColor(getResources().getColor(R.color.txtapptheme_color));
                                button.setTextColor(getResources().getColor(R.color.AppTheme));
                            }
                            if (AnalyticsApplication.main_filters.containsKey(filtercodelabel.get(button.getText().toString()))) {
                                ArrayList<String> list = AnalyticsApplication.main_filters.get(filtercodelabel.get(button.getText().toString()));
                                LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                Objects.requireNonNull(list).remove(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());
                                AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);
                                ArrayList<String> emptylist = AnalyticsApplication.main_filters.get(filtercodelabel.get(button.getText().toString()));
                                if (Objects.requireNonNull(emptylist).size() <= 0) {
                                    AnalyticsApplication.main_filters.remove(filtercodelabel.get(button.getText().toString()));
                                }
                            }
                        }
                    });
                }
                productListingBinding.filterSheet.MageNativeFiltervalues.addView(linearLayout);
                firstselected = false;
            } else {
                if (AnalyticsApplication.main_filters.containsKey(key)) {
                    button.setBackgroundColor(getResources().getColor(R.color.AppTheme));
                    button.setTextColor(getResources().getColor(R.color.txtapptheme_color));
                } else {
                    button.setBackgroundColor(getResources().getColor(R.color.darker_gray));
                    button.setTextColor(getResources().getColor(R.color.black));
                }
            }
            if (isClearFilterClicked) {
                isClearFilterClicked = false;
            }
            label.setText(key);
            button.setText(filterlabelcode.get(key));
            LinearLayout border = new LinearLayout(getApplicationContext());
            border.setBackgroundColor(getResources().getColor(R.color.border));
            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 2);
            border.setLayoutParams(buttonParams);
            linebelowbutton.addView(button, 0);
            linebelowbutton.addView(border, 1);
            linebelowbutton.addView(label, 2);
            productListingBinding.filterSheet.MageNativeFiltertags.addView(linebelowbutton);
            productListingBinding.filterSheet.MageNativeFiltertags.setWeightSum(keyvalfilter2.length());
            button.setOnClickListener(v -> {
                productListingBinding.filterSheet.MageNativeFilterscroll.scrollTo(0, 0);
                String currenttext = filtercodelabel.get(button.getText().toString());
                LinearLayout buttontags = (LinearLayout) button.getParent();
                LinearLayout buttontagsparent = (LinearLayout) buttontags.getParent();
                int childcountoftags = buttontagsparent.getChildCount();
                for (int child = 0; child <= childcountoftags - 1; child++) {
                    LinearLayout buttonlayout = (LinearLayout) buttontagsparent.getChildAt(child);
                    Button button1 = (Button) buttonlayout.getChildAt(0);
                    if (AnalyticsApplication.main_filters.containsKey(filtercodelabel.get(button1.getText().toString()))) {
                    } else {
                        if (Objects.requireNonNull(currenttext).equals(filtercodelabel.get(button1.getText().toString()))) {
                            button1.setBackgroundColor(getResources().getColor(R.color.txtapptheme_color));
                            button1.setTextColor(getResources().getColor(R.color.AppTheme));
                        } else {
                            button1.setTextColor(getResources().getColor(R.color.black));
                            button1.setBackgroundColor(getResources().getColor(R.color.darker_gray));
                        }
                    }
                }
                if (tagstoshow) {
                    if (productListingBinding.filterSheet.MageNativeFiltervalues.getChildCount() > 0) {
                        productListingBinding.filterSheet.MageNativeFiltervalues.removeAllViewsInLayout();
                    }
                    LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                    linearLayout.requestFocus();
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    JSONObject filtervalueshash = null;
                    try {
                        filtervalueshash = keyvalfilter2.getJSONObject(Objects.requireNonNull(filtercodelabel.get(button.getText().toString())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Iterator<String> filter = Objects.requireNonNull(filtervalueshash).keys();
                    while (filter.hasNext()) {
                        String filterid = String.valueOf(filter.next());
                        /* String filterid = String.valueOf(filterpair.getKey());*/
                        String filterval = "";
                        try {
                            filterval = filtervalueshash.getString(filterid);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        LinearLayout layout = new LinearLayout(getApplicationContext());
                        layout.setOrientation(LinearLayout.HORIZONTAL);
                        final CheckBox filtervalue = new CheckBox(getApplicationContext());
                        filtervalue.setButtonDrawable(checkbox_visibility);
                        filtervalue.setText(Html.fromHtml(filterval));
                        if (containskeyalready(filterval, filtercodelabel.get(button.getText().toString()), filterid)) {
                            filtervalue.setChecked(true);
                        } else {
                            filtervalue.setChecked(false);
                        }
                        TextView filId = new TextView(getApplicationContext());
                        filId.setText(filterid);
                        filId.setVisibility(View.GONE);
                        layout.addView(filtervalue, 0);
                        layout.addView(filId, 1);
                        linearLayout.addView(layout);
                        filtervalue.setOnCheckedChangeListener((buttonView, isChecked) -> {
                            if (isChecked) {

                                button.setBackgroundColor(getResources().getColor(R.color.AppTheme));
                                button.setTextColor(getResources().getColor(R.color.txtapptheme_color));
                                if (AnalyticsApplication.main_filters.size() <= 0) {
                                    ArrayList<String> list = new ArrayList<>();
                                    LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                    TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                    list.add(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());
                                    AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);

                                } else {
                                    if (AnalyticsApplication.main_filters.containsKey(filtercodelabel.get(button.getText().toString()))) {
                                        LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                        TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                        ArrayList<String> list = AnalyticsApplication.main_filters.get(filtercodelabel.get(button.getText().toString()));
                                        if (Objects.requireNonNull(list).contains(filter_ID.getText().toString() + "#" + filtervalue.getText().toString())) {
                                        } else {
                                            list.add(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());
                                            AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);

                                        }
                                    } else {
                                        ArrayList<String> list = new ArrayList<>();
                                        LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                        TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                        list.add(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());
                                        AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);
                                    }
                                }
                            } else {
                                boolean ischecked = false;
                                LinearLayout checkboxparent = (LinearLayout) filtervalue.getParent();
                                LinearLayout checkboxparentParent = (LinearLayout) checkboxparent.getParent();
                                int childcount = checkboxparentParent.getChildCount();
                                for (int child = 0; child <= childcount - 1; child++) {
                                    LinearLayout layout1 = (LinearLayout) checkboxparentParent.getChildAt(child);
                                    CheckBox box = (CheckBox) layout1.getChildAt(0);
                                    if (box.isChecked()) {
                                        ischecked = true;
                                    }
                                }
                                if (ischecked) {
                                    button.setBackgroundColor(getResources().getColor(R.color.AppTheme));
                                    button.setTextColor(getResources().getColor(R.color.txtapptheme_color));
                                } else {
                                    button.setBackgroundColor(getResources().getColor(R.color.txtapptheme_color));
                                    button.setTextColor(getResources().getColor(R.color.AppTheme));
                                }
                                if (AnalyticsApplication.main_filters.containsKey(filtercodelabel.get(button.getText().toString()))) {
                                    ArrayList<String> list = AnalyticsApplication.main_filters.get(filtercodelabel.get(button.getText().toString()));
                                    LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                    TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                    Objects.requireNonNull(list).remove(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());
                                    AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);
                                    ArrayList<String> emptylist = AnalyticsApplication.main_filters.get(filtercodelabel.get(button.getText().toString()));
                                    if (Objects.requireNonNull(emptylist).size() <= 0) {
                                        AnalyticsApplication.main_filters.remove(filtercodelabel.get(button.getText().toString()));
                                    }
                                }
                            }
                        });
                    }
                    productListingBinding.filterSheet.MageNativeFiltervalues.addView(linearLayout);
                    tagstoshow = false;
                } else {
                    productListingBinding.filterSheet.MageNativeFiltervalues.removeAllViewsInLayout();
                    LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    JSONObject filtervalueshash = null;
                    try {
                        filtervalueshash = keyvalfilter2.getJSONObject(Objects.requireNonNull(filtercodelabel.get(button.getText().toString())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Iterator<String> filter = Objects.requireNonNull(filtervalueshash).keys();
                    while (filter.hasNext()) {
                        String filterid = String.valueOf(filter.next());
                        String filterval = "";
                        try {
                            filterval = filtervalueshash.getString(filterid);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        LinearLayout layout = new LinearLayout(getApplicationContext());
                        layout.setOrientation(LinearLayout.HORIZONTAL);
                        final CheckBox filtervalue = new CheckBox(getApplicationContext());
                        filtervalue.setButtonDrawable(checkbox_visibility);
                        filtervalue.setText(filterval);
                        filtervalue.setTextColor(getResources().getColor(R.color.black));
                        if (containskeyalready(filterval, filtercodelabel.get(button.getText().toString()), filterid)) {
                            filtervalue.setChecked(true);
                        } else {
                            filtervalue.setChecked(false);
                        }
                        TextView filId = new TextView(getApplicationContext());
                        filId.setText(filterid);
                        filId.setVisibility(View.GONE);
                        layout.addView(filtervalue, 0);
                        layout.addView(filId, 1);
                        layout.requestFocus();
                        linearLayout.addView(layout);
                        filtervalue.setOnCheckedChangeListener((buttonView, isChecked) -> {
                            if (isChecked) {
                                button.setBackgroundColor(getResources().getColor(R.color.AppTheme));
                                button.setTextColor(getResources().getColor(R.color.txtapptheme_color));
                                if (AnalyticsApplication.main_filters.size() <= 0) {
                                    ArrayList<String> list = new ArrayList<>();
                                    LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                    TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                    list.add(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());

                                    AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);

                                } else {
                                    if (AnalyticsApplication.main_filters.containsKey(filtercodelabel.get(button.getText().toString()))) {
                                        ArrayList<String> list = AnalyticsApplication.main_filters.get(filtercodelabel.get(button.getText().toString()));
                                        LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                        TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                        Objects.requireNonNull(list).add(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());
                                        AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);
                                    } else {
                                        ArrayList<String> list = new ArrayList<>();
                                        LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                        TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                        list.add(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());
                                        AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);
                                    }
                                }
                            } else {
                                boolean ischecked = false;
                                LinearLayout checkboxparent = (LinearLayout) filtervalue.getParent();
                                LinearLayout checkboxparentParent = (LinearLayout) checkboxparent.getParent();
                                int childcount = checkboxparentParent.getChildCount();
                                for (int child = 0; child <= childcount - 1; child++) {
                                    LinearLayout layout1 = (LinearLayout) checkboxparentParent.getChildAt(child);
                                    CheckBox box = (CheckBox) layout1.getChildAt(0);
                                    if (box.isChecked()) {
                                        ischecked = true;
                                    }
                                }
                                if (ischecked) {
                                    button.setBackgroundColor(getResources().getColor(R.color.AppTheme));
                                    button.setTextColor(getResources().getColor(R.color.txtapptheme_color));
                                } else {
                                    button.setBackgroundColor(getResources().getColor(R.color.txtapptheme_color));
                                    button.setTextColor(getResources().getColor(R.color.AppTheme));
                                }
                                if (AnalyticsApplication.main_filters.containsKey(filtercodelabel.get(button.getText().toString()))) {
                                    ArrayList<String> list = AnalyticsApplication.main_filters.get(filtercodelabel.get(button.getText().toString()));
                                    LinearLayout filterlayout = (LinearLayout) filtervalue.getParent();
                                    TextView filter_ID = (TextView) filterlayout.getChildAt(1);
                                    Objects.requireNonNull(list).remove(filter_ID.getText().toString() + "#" + filtervalue.getText().toString());
                                    AnalyticsApplication.main_filters.put(filtercodelabel.get(button.getText().toString()), list);
                                    ArrayList<String> emptylist = AnalyticsApplication.main_filters.get(filtercodelabel.get(button.getText().toString()));
                                    if (Objects.requireNonNull(emptylist).size() <= 0) {
                                        AnalyticsApplication.main_filters.remove(filtercodelabel.get(button.getText().toString()));
                                    }
                                }
                            }
                        });
                    }
                    productListingBinding.filterSheet.MageNativeFiltervalues.addView(linearLayout);
                }
            });
        }
    }

    private void createproductgrid(JSONObject jsonObj) {
        try {
            ///-----------product grid section
            if (jsonObj.getJSONObject("data").has("products")) {
                productListingBinding.newlistlayout.noproductLayout.setVisibility(View.GONE);
                JSONArray productsjsonArray = jsonObj.getJSONObject("data").getJSONArray("products");
                for (int i = 0; i < productsjsonArray.length(); i++) {
                    HashMap<String, String> grid_data = new HashMap<>();
                    JSONObject object = productsjsonArray.getJSONObject(i);

                    grid_data.put("type", object.getString("type"));
                    grid_data.put("product_image", object.getString("product_image"));
                    grid_data.put("product_id", object.getString("product_id"));
                    grid_data.put("product_name", object.getString("product_name"));
                    grid_data.put("stock_status", object.getString("stock_status"));
                    if (object.has("special_price")) {
                        grid_data.put("special_price", object.getString("special_price"));
                    } else {
                        grid_data.put("special_price", "no_special");
                    }
                    if (object.has("regular_price")) {
                        grid_data.put("regular_price", object.getString("regular_price"));
                    } else {
                        if (object.has("final_price")) {
                            grid_data.put("regular_price", object.getString("final_price"));
                        } else {
                            grid_data.put("regular_price", "");
                        }
                    }
                    grid_data.put("Inwishlist", object.getString("Inwishlist"));
                    grid_data.put("wishlist_item_id", object.getString("wishlist_item_id"));
                    if (object.has("offer")) {
                        grid_data.put("offer", String.valueOf(object.getInt("offer")));
                    } else {
                        grid_data.put("offer", "0");
                    }
                    if (object.has("review")) {
                        grid_data.put("review", object.getString("review"));
                    } else {
                        grid_data.put("review", "No_Review");
                    }

                    gridviewdata.add(grid_data);
                }
                mageNative_gridViewAdapter = new RecyclerGridViewAdapter(Ced_New_Product_Listing.this, gridviewdata, gridlayout/*category_product*/);
                productListingBinding.newlistlayout.recycler.setAdapter(mageNative_gridViewAdapter);
                mageNative_gridViewAdapter.notifyDataSetChanged();
                productListingBinding.newlistlayout.recycler.scrollToPosition((gridviewdata.size()) - (productsjsonArray.length()));
                load = true;
                productListingBinding.newlistlayout.recycler.setVisibility(View.VISIBLE);
                productListingBinding.newlistlayout.nestedscroll.setVisibility(View.VISIBLE);
            } else {
                productListingBinding.newlistlayout.noproductLayout.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    private boolean containskeyalready(String checkbox, String parent, String id) {
        if (AnalyticsApplication.main_filters.size() > 0) {
            if (AnalyticsApplication.main_filters.containsKey(parent)) {
                ArrayList<String> list = AnalyticsApplication.main_filters.get(parent);
                return Objects.requireNonNull(list).contains(id + "#" + checkbox);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onResume() {
        Log.d(Urls.TAG, "onResume: ");
        invalidateOptionsMenu();
        if (gridviewdata.size() > 0) {
            mageNative_gridViewAdapter = new RecyclerGridViewAdapter(Ced_New_Product_Listing.this, gridviewdata, gridlayout);
            productListingBinding.newlistlayout.recycler.setAdapter(mageNative_gridViewAdapter);
            mageNative_gridViewAdapter.notifyDataSetChanged();
        }
        if (filterSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            filterSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            productListingBinding.newlistlayout.view.setVisibility(View.GONE);
        } else if (sortSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sortSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            productListingBinding.newlistlayout.view.setVisibility(View.GONE);
        }

        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(Urls.TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (filterSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            filterSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            productListingBinding.newlistlayout.view.setVisibility(View.GONE);
        } else if (sortSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sortSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            productListingBinding.newlistlayout.view.setVisibility(View.GONE);
        } else {
            AnalyticsApplication.main_filters.clear();
            Ced_New_Product_Listing.order = "";
            Ced_New_Product_Listing.dir = "";
            Ced_New_Product_Listing.selecteddir = "";
            Ced_New_Product_Listing.selectedorder = "";
            super.onBackPressed();
        }
    }
}