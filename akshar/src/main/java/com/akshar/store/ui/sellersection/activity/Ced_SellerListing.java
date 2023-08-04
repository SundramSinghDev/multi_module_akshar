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
package com.akshar.store.ui.sellersection.activity;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.MagenativeNovendorBinding;
import com.akshar.store.databinding.MagenativeSellerListingBinding;
import com.akshar.store.databinding.MagenativeSellerSearchLayoutBinding;
import com.akshar.store.rest.ApiResponse;
import com.akshar.store.ui.sellersection.adapter.Ced_SellerList_RecyclerApapter;
import com.akshar.store.ui.sellersection.utils.Ced_MapComparator;
import com.akshar.store.ui.sellersection.utils.Ced_MapComprator_des;
import com.akshar.store.ui.sellersection.viewmodel.SellerListViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class Ced_SellerListing extends Ced_NavigationActivity {
    public static boolean loadfrombottom = false;
    MagenativeSellerListingBinding magenativeSellerListingBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    SellerListViewModel sellerListViewModel;
    RecyclerView sellerlist_recyclerview;
    Ced_SellerList_RecyclerApapter sellerList_recyclerApapter;
    ArrayList<HashMap<String, String>> finalsellerlist;
    boolean load = false;
    int current = 1;
    Dialog listDialog;
    List<String> countrylabellist;
    List<String> countrycodelist;
    List<String> statecodelist;
    List<String> statelabellist;
    HashMap<String, String> codecountry;
    String country_code = "";
    String state_code = "";
    String country_label = "";
    String state_label = "";
    JsonObject searchdata_param;
    String sellersearch = "";
    HashMap<String, String> label_code;
    boolean a_z = false;
    Boolean nostates = false;

    LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        linearLayoutManager = new LinearLayoutManager(Ced_SellerListing.this, RecyclerView.VERTICAL, false);
        magenativeSellerListingBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_seller_listing, content, true);
        sellerListViewModel = new ViewModelProvider(Ced_SellerListing.this, viewModelFactory).get(SellerListViewModel.class);
       countrylabellist = new ArrayList<String>();
        countrycodelist = new ArrayList<String>();
        label_code = new HashMap<String, String>();
        sellerlist_recyclerview = magenativeSellerListingBinding.sellerlistRecyclerview;
        sellerlist_recyclerview.setLayoutManager(linearLayoutManager);
        finalsellerlist = new ArrayList<HashMap<String, String>>();
        codecountry = new HashMap<String, String>();

        searchdata_param = new JsonObject();
        searchdata_param.addProperty("page", String.valueOf(current));
        
        getcountries();

        Intent seller = getIntent();
        if (seller.getExtras() != null) {
            if (seller.hasExtra("seller")) {
                if (seller.getStringExtra("seller").equals("out")) {
                    searchdata_param.addProperty("seller_search", seller.getStringExtra("sellersearch"));
                    Log.i("sdf", "" + sellersearch);
                    sellersearch = seller.getStringExtra("sellersearch");
                } else {
                    searchdata_param.addProperty("seller_search", "no_data");
                    sellersearch = "";
                }
            } else {
                searchdata_param.addProperty("seller_search", "no_data");
                sellersearch = "";
            }
            if (seller.getStringExtra("seller").equals("out")) {
                searchdata_param.addProperty("seller_search", seller.getStringExtra("sellersearch"));
                sellersearch = seller.getStringExtra("sellersearch");
            } else {
                searchdata_param.addProperty("seller_search", "no_data");
                sellersearch = "";
            }
        }

        magenativeSellerListingBinding.find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showsearchpopup();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        magenativeSellerListingBinding.sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a_z) {
                    a_z = false;
                    loadfrombottom = true;
                    magenativeSellerListingBinding.sort.setImageResource(R.drawable.sort_a_z);
                    Collections.sort(finalsellerlist, new Ced_MapComparator("public_name"));
                    sellerList_recyclerApapter = new Ced_SellerList_RecyclerApapter(Ced_SellerListing.this, finalsellerlist,sellerListViewModel);
                    sellerlist_recyclerview.setAdapter(sellerList_recyclerApapter);
                    load = true;
                } else {
                    a_z = true;
                    loadfrombottom = true;
                    magenativeSellerListingBinding.sort.setImageResource(R.drawable.sort_z_to_a);
                    Collections.sort(finalsellerlist, new Ced_MapComprator_des("public_name"));
                    sellerList_recyclerApapter = new Ced_SellerList_RecyclerApapter(Ced_SellerListing.this, finalsellerlist,sellerListViewModel);
                    sellerlist_recyclerview.setAdapter(sellerList_recyclerApapter);
                    load = true;
                }
            }
        });

        sellerListViewModel.getSellerListData(this, searchdata_param).observe(this, this::consumeResponse);

        magenativeSellerListingBinding.sellerlistRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && load) {
                    Log.i("onScrollStateChanged", "loadmore");
                    load = false;
                    //-------------------
                    current = current + 1;
                    load = false;
                    searchdata_param.addProperty("page", String.valueOf(current));
                    sellerListViewModel.getSellerListData(Ced_SellerListing.this, searchdata_param)
                            .observe(Ced_SellerListing.this, Ced_SellerListing.this::consumeResponse);

                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public void applydata(String Jstring) throws JSONException {
        JSONObject object = new JSONObject(Jstring);
        String status = object.getJSONObject("data").getString("success");
        if (status.equals("true")) {
            magenativeSellerListingBinding.main.setVisibility(View.VISIBLE);
            loadfrombottom = false;
            String bannerurl = object.getJSONObject("data").getString("banner_url");
           JSONArray sellerjsonarray = object.getJSONObject("data").getJSONArray("sellers");
            for (int i = 0; i < sellerjsonarray.length(); i++) {
                JSONObject sellerobject = sellerjsonarray.getJSONObject(i);
                HashMap<String, String> seller = new HashMap<String, String>();
                seller.put("entity_id", sellerobject.getString("entity_id"));
                seller.put("public_name", sellerobject.getString("public_name"));
               // seller.put("shop_url", sellerobject.getString("shop_url"));
                if (sellerobject.has("vendor_review")) {
                    if (sellerobject.getString("vendor_review").equals("false")) {
                        seller.put("review", "no_review");
                    } else {
                        seller.put("review", sellerobject.getString("vendor_review"));
                    }
                } else {
                    seller.put("review", "no_review");
                }
                if (sellerobject.has("vendor_review_count"))
                {
                    seller.put("reviewcount", sellerobject.getString("vendor_review_count"));
                } else {
                    seller.put("reviewcount", "no_count");
                }
                if (sellerobject.has("city") && sellerobject.has("country_id")) {
                    if (sellerobject.getString("city").isEmpty()) {
                        seller.put("citycountrey", "no info regarding address");
                    } else {
                        if (codecountry.containsKey(sellerobject.get("country_id"))) {
                            seller.put("citycountrey", sellerobject.get("city") + ", " + codecountry.get(sellerobject.get("country_id")));
                        } else {
                            seller.put("citycountrey", "no info regarding address");
                        }
                    }
                } else {
                    seller.put("citycountrey", "no info regarding address");
                }
                if (sellerobject.has("company_banner")) {
                    seller.put("company_logo", sellerobject.getString("company_banner"));
                } else {
                    seller.put("company_logo", "false");
                }
                finalsellerlist.add(seller);
            }
            Glide.with(Ced_SellerListing.this)
                    .load(bannerurl)
                    .placeholder(R.drawable.bannerplaceholder)
                    .error(R.drawable.bannerplaceholder)
                    .into(magenativeSellerListingBinding.magenativeMarketplacebannerHeader);
            sellerList_recyclerApapter = new Ced_SellerList_RecyclerApapter(this, finalsellerlist,sellerListViewModel);
            sellerlist_recyclerview.setAdapter(sellerList_recyclerApapter);
            load = true;
        }
        else {
            if(current==1)
            {
                MagenativeNovendorBinding novendorBinding= DataBindingUtil.setContentView( this,R.layout.magenative_novendor);
                novendorBinding.magenativeCanceldata.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!(sellersearch.isEmpty())) {
                            sellersearch = "";
                            Intent intent = new Intent(getApplicationContext(), Ced_SellerListing.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            finish();
                        }
                    }
                });
                novendorBinding.msg.setText(object.getJSONObject("data").getString("message"));
            }
            load = false;
        }

    }

    private void consumeResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                try {
                    applydata(Objects.requireNonNull(apiResponse.data));
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

    private void showsearchpopup() throws JSONException {
        listDialog = new Dialog(this, R.style.PauseDialog);
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        MagenativeSellerSearchLayoutBinding binding = DataBindingUtil.inflate(li, R.layout.magenative_seller_search_layout, null, false);
        final EditText country = binding.magenativeCountry;
        final EditText state = binding.magenativeState;
        final EditText dropdown_state = binding.magenativeDropdownState;
        final TextInputLayout state_dropdown_layout = binding.stateDropdownLayout;
        final TextInputLayout state_layout =binding.stateLayout;
        final EditText zip = binding.magenativeZip;
        final EditText city = binding.magenativeCity;
        final EditText vendorname = binding.magenativeVendorname;
        final AppCompatButton submit = binding.magenativeSubmit;

        country.requestFocus();
        country.callOnClick();

        if (!(sellersearch.isEmpty())) {
            JSONObject object = new JSONObject(sellersearch);
            if (codecountry.containsKey(object.getString("country"))) {
                country.setText(codecountry.get(object.getString("country")));
            }
            if (object.has("region_id")) {
                state_code = object.getString("region_id");
                state_label = object.getString("region_label");
                state.setText(state_label);
                dropdown_state.setText(state_label);
            } else {
                state.setText(object.getString("state"));
                dropdown_state.setText(object.getString("state"));
            }
            zip.setText(object.getString("zip"));
            vendorname.setText(object.getString("vendorname"));
            city.setText(object.getString("estimate_city"));
        }

        dropdown_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (statecodelist != null) {
                    if (statecodelist.size() > 0 && statelabellist.size() > 0) {
                        nostates = false;
                        /*state.setEnabled(false);*/
                        final CharSequence[] arrayOfInt = (CharSequence[]) statecodelist.toArray(new CharSequence[statecodelist.size()]);
                        final CharSequence[] arrayOfInt2 = (CharSequence[]) statelabellist.toArray(new CharSequence[statelabellist.size()]);
                        new MaterialAlertDialogBuilder(Ced_SellerListing.this,R.style.SingleChoiceRadioStyle)
                                .setTitle("Select Your  State")
                                .setSingleChoiceItems(arrayOfInt2, -1, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int postion) {
                                        state_code = (String) arrayOfInt[postion];
                                        state_label = (String) arrayOfInt2[postion];
                                        state.setText(state_label);
                                        dropdown_state.setText(state_label);
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    } else {
                        /*state.setEnabled(true);*/
                        nostates = true;
                    }
                } else {
                    if (country.getText().toString().equals("")) {
                        showmsg("Please select country first..");
                    }

                }
            }
        });


        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] arrayOfInt = (CharSequence[]) countrycodelist.toArray(new CharSequence[countrycodelist.size()]);
                final CharSequence[] arrayOfInt2 = (CharSequence[]) countrylabellist.toArray(new CharSequence[countrylabellist.size()]);
                Dialog levelDialog1 = new Dialog(Ced_SellerListing.this);
                new MaterialAlertDialogBuilder(Ced_SellerListing.this, R.style.SingleChoiceRadioStyle)
                        .setTitle("Select Your  Country")
                        .setSingleChoiceItems(arrayOfInt2, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int postion) {
                                country_code = (String) arrayOfInt[postion];
                                country_label = (String) arrayOfInt2[postion];
                                country.setText(country_label);
                                state.setText("");
                                dropdown_state.setText("");
                                getState(country_code, state, dropdown_state, state_layout, state_dropdown_layout);
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject seacrhseller = new JSONObject();
                try {
                    listDialog.dismiss();
                    seacrhseller.put("country", country_code);
                    if (state_code.isEmpty()) {
                        seacrhseller.put("state", state.getText().toString());
                    } else {
                        if (nostates) {
                            seacrhseller.put("region_label", state_label);
                        } else {
                            seacrhseller.put("region_id", state_code);
                        }

                    }
                    seacrhseller.put("zip", zip.getText().toString());
                    seacrhseller.put("vendorname", vendorname.getText().toString());
                    seacrhseller.put("estimate_city", city.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Ced_SellerListing.this, Ced_SellerListing.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("seller", "out");
                intent.putExtra("sellersearch", seacrhseller.toString());
                startActivity(intent);

            }
        });
        listDialog.setTitle(getResources().getString(R.string.app_name));
        listDialog.setContentView(binding.getRoot());
        listDialog.setCancelable(true);
        listDialog.show();
    }

    private void getcountries() {
        try {
            sellerListViewModel.getCountriesData(Ced_SellerListing.this).observe(Ced_SellerListing.this, apiResponse -> {
                switch (apiResponse.status) {
                    case SUCCESS:
                        try {
                            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(apiResponse.data));
                            JSONArray jsonArray = jsonObject.getJSONArray("country");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                countrycodelist.add(object.getString("value"));
                                countrylabellist.add(object.getString("label"));
                                codecountry.put(object.getString("value"), object.getString("label"));
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

    private void getState(String country_code, EditText state, final EditText dropdown_state, TextInputLayout state_layout, TextInputLayout state_dropdown_layout) {
        sellerListViewModel.getStates(this, String.valueOf(country_code)).observe(Ced_SellerListing.this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                    try {

                        JSONObject object = new JSONObject(apiResponse.data);
                        Boolean status = object.getBoolean("success");
                        if (status.equals(true) && object.getJSONArray("states").length() > 0) {
                            JSONArray jsonArray = object.getJSONArray("states");
                            statelabellist = new ArrayList<String>();
                            statecodelist = new ArrayList<String>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);
                                statecodelist.add(c.getString("region_id"));
                                statelabellist.add(c.getString("default_name"));
                            }
                            // state.setHint("select your state here...");
                            state.setEnabled(false);
                            state_layout.setVisibility(View.GONE);
                            state_dropdown_layout.setVisibility(View.VISIBLE);
                            state.setText("");
                            dropdown_state.setText("");
                            dropdown_state.requestFocus();
                            state.callOnClick();
                        } else {
                            //  state.setHint("State");
                            state.setEnabled(true);
                            state_layout.setVisibility(View.VISIBLE);
                            state_dropdown_layout.setVisibility(View.GONE);
                            state.requestFocus();
                        }


                        ;
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
    }
}

