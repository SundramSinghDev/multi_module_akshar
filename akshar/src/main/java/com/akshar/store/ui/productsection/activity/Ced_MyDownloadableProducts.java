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

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;

import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import com.akshar.store.databinding.MagenativeActivityMyDownloadableProductsBinding;
import com.akshar.store.databinding.MagenativeEmptyDownloadableProductBinding;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.akshar.store.ui.productsection.adapter.Ced_DownloadsAdapter;
import com.akshar.store.ui.productsection.viewmodel.DownloadsViewModel;
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
public class Ced_MyDownloadableProducts extends Ced_NavigationActivity {
    @Inject
    ViewModelFactory viewModelFactory;
    DownloadsViewModel downloadsViewModel;

    ListView downloadslist;
    ArrayList<HashMap<String, String>> downloads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        downloadsViewModel = new ViewModelProvider(Ced_MyDownloadableProducts.this, viewModelFactory).get(DownloadsViewModel.class);
        MagenativeActivityMyDownloadableProductsBinding binding= DataBindingUtil.inflate(getLayoutInflater(),R.layout.magenative_activity_my_downloadable_products, content,true);
        downloadslist = binding.MageNativeDownloadslist;
        downloads = new ArrayList<>();

        try {
            if (session.isLoggedIn()) {
                JsonObject cartlist = new JsonObject();
                cartlist.addProperty("customer_id", session.getCustomerid());
                if (cedSessionManagement.getStoreId() != null) {
                    cartlist.addProperty("store_id", cedSessionManagement.getStoreId());
                }

                downloadsViewModel.getDownloadProducts(this, cartlist,session.getHahkey()).observe(this, apiResponse -> {
                    switch (apiResponse.status){
                        case SUCCESS:
                            try {
                                    JSONObject object = new JSONObject(apiResponse.data);
                                    if (object.has("header") && object.getString("header").equals("false")) {
                                        Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                    } else {
                                        String status = object.getJSONObject("data").getString("status");
                                        if (status.equals("true")) {
                                            applydata(apiResponse.data);
                                        } else {
                                            if (status.equals("false")) {
                                                showmsg(object.getJSONObject("data").getString("message"));
                                                MagenativeEmptyDownloadableProductBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_empty_downloadable_product, content, true);
                                                binding1.conti.setOnClickListener(v -> {
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

            } else {
                Intent intent = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(getApplicationContext(), Ced_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    private void applydata(String output) {
        try {
            JSONObject jsonObject = new JSONObject(output);
            JSONArray array = jsonObject.getJSONObject("data").getJSONArray("downloadable-product");
            for (int d = 0; d < array.length(); d++) {
                JSONObject object = array.getJSONObject(d);
                HashMap<String, String> map = new HashMap<>();
                map.put("order_id", object.getString("order_id"));
                map.put("date", object.getString("date"));
                map.put("title", object.getString("title"));
                map.put("download_url", object.getString("download_url"));
                map.put("file_name", object.getString("file_name"));
                map.put("link_title", object.getString("link_title"));
                map.put("status", object.getString("status"));
                map.put("remaining_dowload", object.getString("remaining_dowload"));
                downloads.add(map);
            }
            Ced_DownloadsAdapter cedDownloadsAdapter = new Ced_DownloadsAdapter(Ced_MyDownloadableProducts.this, downloads);
            View cat = View.inflate(this, R.layout.magenative_download, null);
            cat.setOnClickListener(null);
            TextView count = cat.findViewById(R.id.MageNative_count);
            set_bold_font_fortext(count);
            count.setText(getResources().getString(R.string.youhave) + " " + String.valueOf(jsonObject.getJSONObject("data").getInt("item_count")) + " " + getResources().getString(R.string.itemtodownload));

            downloadslist.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
            downloadslist.setDividerHeight(0);
            downloadslist.addHeaderView(cat);
            downloadslist.setAdapter(cedDownloadsAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
            Intent intent = new Intent(getApplicationContext(), Ced_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }
}
