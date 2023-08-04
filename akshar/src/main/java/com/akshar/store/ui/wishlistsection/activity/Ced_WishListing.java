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
package com.akshar.store.ui.wishlistsection.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonObject;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.MagenativeWishlistheaderBinding;
import com.akshar.store.ui.cartsection.activity.Ced_CartListing;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import com.akshar.store.databinding.MagenativeActivityWishListingBinding;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;
import com.akshar.store.ui.wishlistsection.adapter.Ced_WishlistAdapter;
import com.akshar.store.ui.wishlistsection.viewmodel.WishListViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ced_WishListing extends Ced_NavigationActivity {
    MagenativeActivityWishListingBinding wishListingBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    WishListViewModel wishListViewModel;
    static String Jstring = "";
    public static final String KEY_ITEM = "data";
    public static final String KEY_SUB_ITEM = "products";
    public static final String KEY_ID = "product_id";
    public static final String KEY_NAME = "product_name";
    public static final String KEY_Image = "product_image";
    public static final String KEY_Price = "regular_price";
    public static final String KEY_Price_special_price = "special_price";
    public static final String KEY_wishlist_item_id = "wishlist_item_id";
    JSONArray products = null;
    JSONObject jsonObj = null;
    private Ced_WishlistAdapter listViewAdapter;
    ArrayList<HashMap<String, String>> ProductData;
    ListView list;
    ArrayList<String> ids;
    String wishlistcount = "";
    String prod_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectwishtab();
        wishListViewModel = new ViewModelProvider(Ced_WishListing.this, viewModelFactory).get(WishListViewModel.class);
        wishListingBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_activity_wish_listing, content, true);
        list = wishListingBinding.MageNativeWishlist;
        ids = new ArrayList<>();
        ProductData = new ArrayList<>();
        if (session.isLoggedIn()) {
            try {
                JsonObject wishlisting = new JsonObject();
                wishlisting.addProperty("customer_id", session.getCustomerid());
                if (cedSessionManagement.getStoreId() != null) {
                    wishlisting.addProperty("store_id", cedSessionManagement.getStoreId());
                }

                wishListViewModel.getWishListData(Ced_WishListing.this, wishlisting,session.getHahkey()).observe(Ced_WishListing.this, apiResponse -> {
                    switch (apiResponse.status){
                        case SUCCESS:

                                try{
                                    Jstring = apiResponse.data;
                                    JSONObject object = new JSONObject(Objects.requireNonNull(Jstring));
                                    if (object.has("header") && object.getString("header").equals("false")) {
                                        Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                    }
                                    else {
                                        String status = object.getJSONObject("data").getString("status");
                                        if (status.equals("false")) {
                                            getLayoutInflater().inflate(R.layout.magenative_empty_wishlist, content, true);
                                            TextView conti = findViewById(R.id.conti);
                                            conti.setOnClickListener(v -> {
                                                Intent intent = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.putExtra("exceptfromhome", "true");
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                                finish();
                                            });
                                        } else {
                                            applydata();
                                        }
                                    }
                                }catch (Exception e){
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
                Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(main);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            }
        } else {
            showmsg(getResources().getString(R.string.loginfirst));
            getLayoutInflater().inflate(R.layout.magenative_empty_wishlist, content, true);
            finish();
        }
    }

    public void applydata() {
        try {
            ProductData = new ArrayList<>();
            jsonObj = new JSONObject(Jstring);
            products = jsonObj.getJSONObject(KEY_ITEM).getJSONArray(KEY_SUB_ITEM);
            for (int i = 0; i < products.length(); i++) {
                JSONObject c = null;
                c = products.getJSONObject(i);
                /*String id = c.getString(KEY_ID);
                ids.add(id);*/
                prod_id = c.getString(KEY_ID);
                ids.add(prod_id);
                String name = c.getString(KEY_NAME);
                String image = c.getString(KEY_Image);
                String price = c.getString(KEY_Price);
                String special_price = c.getString(KEY_Price_special_price);
                String wishlist_item_id = c.getString(KEY_wishlist_item_id);
                HashMap<String, String> productdata = new HashMap<>();// adding each child node to HashMap key => valu
//                productdata.put(KEY_ID, id);
                productdata.put(KEY_ID, prod_id);
                productdata.put(KEY_NAME, name);
                productdata.put(KEY_Image, image);
                productdata.put(KEY_Price, price);
                productdata.put(KEY_Price_special_price, special_price);
                productdata.put(KEY_wishlist_item_id, wishlist_item_id);
                ProductData.add(productdata);
            }
            listViewAdapter = new Ced_WishlistAdapter(Ced_WishListing.this, ProductData);
            wishlistcount = String.valueOf(jsonObj.getJSONObject(KEY_ITEM).getInt("item_count"));
            list.setOnItemClickListener((parent, view, position, id) -> {
                Intent intent = new Intent(getApplicationContext(), Ced_NewProductView.class);
                intent.putExtra("CURRENT", position);
                intent.putExtra("product_id", ids.get(position - 1));
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            });
            list.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
            list.setDividerHeight(0);
            list.setAdapter(listViewAdapter);

            MagenativeWishlistheaderBinding wishlistheaderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_wishlistheader, null,false);
            wishlistheaderBinding.MageNativeCount.setText(wishlistcount.toUpperCase() + " " + getResources().getString(R.string.wishlistitemsin));
            wishlistheaderBinding.MageNativeCount.setOnClickListener(null);
            wishlistheaderBinding.MageNativeClearcartsection.setOnClickListener(v -> {
                try {
                    new MaterialAlertDialogBuilder(Ced_WishListing.this,R.style.MaterialDialog)
                            .setTitle(getResources().getString(R.string.app_name))
                            .setIcon(R.mipmap.ic_launcher)
                            .setMessage(getResources().getString(R.string.confirm_allwishdelete))
                            .setNegativeButton("Cancel", (dialog, which) -> {})
                            .setPositiveButton("Confirm", (dialog, which) -> {
                                clearall_wishlist();
                            })
                            .show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(main);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
            });
            list.addHeaderView(wishlistheaderBinding.getRoot());

        } catch (Exception e) {
            e.printStackTrace();
            Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    private void clearall_wishlist() {
        JsonObject deletefromcart = new JsonObject();
        deletefromcart.addProperty("customer_id", session.getCustomerid());
        wishListViewModel.clearWishListData(Ced_WishListing.this, deletefromcart,session.getHahkey()).observe(Ced_WishListing.this, apiResponse -> {
            switch (apiResponse.status){
                case SUCCESS:
                    Jstring = apiResponse.data;

                    try {
                        JSONObject jsonObject = new JSONObject(Jstring);
                        if (jsonObject.has("header") && jsonObject.getString("header").equals("false")) {
                            Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        } else {
                            if (jsonObject.getString("success").equals("true")) {
                                Intent intent = new Intent(getApplicationContext(), Ced_WishListing.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            }
                            showmsg(jsonObject.getString("message"));
                        }
                    }catch (Exception e){
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

    @Override
    public void onBackPressed() {
        invalidateOptionsMenu();
        super.onBackPressed();
    }
}
