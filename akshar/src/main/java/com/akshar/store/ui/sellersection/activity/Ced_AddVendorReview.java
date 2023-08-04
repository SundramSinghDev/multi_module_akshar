/*
 * Copyright/* *
 *           * CedCommerce
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

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.ui.networkhandlea_activities.Ced_NoInternetconnection;
import com.akshar.store.databinding.MagenativeAddVendorReviewBinding;
import com.akshar.store.databinding.MagenativeHeaderAtReviewBinding;
import com.akshar.store.rest.ApiResponse;
import com.akshar.store.ui.sellersection.adapter.Ced_VendorReviewAdapter;
import com.akshar.store.ui.sellersection.viewmodel.AddVendorReviewViewModel;
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
public class Ced_AddVendorReview extends Ced_NavigationActivity {
    MagenativeAddVendorReviewBinding magenativeAddVendorReviewBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    AddVendorReviewViewModel addVendorReviewViewModel;
    int current = 1;
    JsonObject dataforreviewlist;
    String vendor_id;
    String ShopName;
    String banner_url;
    String reviewcount;
    String address;
    String averagerating;
    ListView reviewlist;
    ArrayList<HashMap<String, String>> reviewinfo;
    Ced_VendorReviewAdapter vendorReviewAdapter;
    public boolean load = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        magenativeAddVendorReviewBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_add_vendor_review, content, true);

        addVendorReviewViewModel = new ViewModelProvider(Ced_AddVendorReview.this, viewModelFactory).get(AddVendorReviewViewModel.class);
        dataforreviewlist = new JsonObject();
        reviewinfo = new ArrayList<HashMap<String, String>>();
        if (cedConnectionDetector.isConnectingToInternet()) {
            reviewlist = magenativeAddVendorReviewBinding.reviewlist;
            MagenativeHeaderAtReviewBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_header_at_review, null, false);
            final Intent intent = getIntent();
            vendor_id = intent.getStringExtra("vendor_id");
            ShopName = intent.getStringExtra("ShopName");
            banner_url = intent.getStringExtra("banner_url");
            reviewcount = intent.getStringExtra("reviewcount");
            address = intent.getStringExtra("address");
            averagerating = intent.getStringExtra("averagerating");
            dataforreviewlist.addProperty("customer_id", session.getCustomerid());
            dataforreviewlist.addProperty("vendor_id", vendor_id);
            dataforreviewlist.addProperty("page", String.valueOf(current));
            reviewlist.addHeaderView(binding1.getRoot());/*
            ImageView comapnybanner = (ImageView) reviewheader.findViewById(R.id.comapnybanner);
            TextView shopname = (TextView) reviewheader.findViewById(R.id.ShopName);
            TextView reviewcounttext = (TextView) reviewheader.findViewById(R.id.reviewcount);
            TextView averageratingtext = (TextView) reviewheader.findViewById(R.id.averagerating);
            TextView addresstext = (TextView) reviewheader.findViewById(R.id.address);*/

            Drawable myFabSrc = getResources().getDrawable(android.R.drawable.ic_input_add);
            //copy it in a new one
            Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
            //set the color filter, you can use also Mode.SRC_ATOP
            willBeWhite.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            //set it to your fab button initialized before
            magenativeAddVendorReviewBinding.addreviewfab.setImageDrawable(willBeWhite);

            Glide.with(Ced_AddVendorReview.this)
                    .load(banner_url)

                    .placeholder(R.drawable.bannerplaceholder)
                    .error(R.drawable.bannerplaceholder)
                    .into(binding1.comapnybanner);
            binding1.ShopName.setText(ShopName);
            binding1.reviewcount.setText(reviewcount);
            binding1.averagerating.setText(averagerating);

            if (!binding1.averagerating.getText().toString().equalsIgnoreCase("")) {
                Float aFloat = Float.valueOf(binding1.averagerating.getText().toString());
                if (aFloat < 2) {
                    binding1.averagerating.setBackground(getResources().getDrawable(R.drawable.round_corner_red));
                }
                if (aFloat >= 2 && aFloat < 4) {
                    binding1.averagerating.setBackground(getResources().getDrawable(R.drawable.round_corner_yellow));
                    binding1.averagerating.setTextColor(getResources().getColor(R.color.black));
                }
                if (aFloat >= 4) {
                    binding1.averagerating.setBackground(getResources().getDrawable(R.drawable.round_corner));
                }
            }

            binding1.address.setText(address);
            magenativeAddVendorReviewBinding.addreviewfab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(getApplicationContext(), Ced_WriteVendorReview.class);
                    intent1.putExtra("vendor_id", vendor_id);
                    intent1.putExtra("customer_id", session.getCustomerid());
                    startActivity(intent1);
                    finish();
                }
            });
            set_bold_font_fortext(binding1.averagerating);
            set_regular_font_fortext(binding1.ShopName);
            set_regular_font_fortext(binding1.address);
            set_regular_font_fortext(binding1.reviewcount);

            addVendorReviewViewModel.getVendorReview(this, dataforreviewlist).observe(this, this::consumeResponse);

            reviewlist.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    if ((firstVisibleItem + visibleItemCount) != 0) {

                        if (((firstVisibleItem + visibleItemCount) == totalItemCount) && load) {
                            current = current + 1;
                            load = false;
                            dataforreviewlist.addProperty("page", String.valueOf(current));
                            addVendorReviewViewModel.getVendorReview(Ced_AddVendorReview.this, dataforreviewlist).observe(Ced_AddVendorReview.this, this::consumepaginationResponse);

                        }
                    }

                }

                private void consumepaginationResponse(ApiResponse apiResponse) {
                    switch (apiResponse.status) {
                        case SUCCESS:
                                try {
                                    applydata2_(Objects.requireNonNull(apiResponse.data));
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
            });
        }
        else {
            Intent intent = new Intent(getApplicationContext(), Ced_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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

    private void applydata2_(String Jstring) throws JSONException {
        JSONObject object = new JSONObject(Jstring);
        JSONObject data = object.getJSONObject("data");
        String sucess = data.getString("success");
        if (sucess.equals("false")) {
            load = false;
        } else {

            JSONArray productreview = data.getJSONArray("productreview");
            for (int i = 0; i < productreview.length(); i++) {
                JSONObject jsonObject = productreview.getJSONObject(i);
                HashMap<String, String> review = new HashMap<String, String>();
                review.put("v_review", jsonObject.getString("v_review"));
                review.put("review-by", jsonObject.getString("review-by"));
                review.put("posted_on", jsonObject.getString("posted_on"));
                review.put("review-title", jsonObject.getString("review-title"));
                review.put("review-description", jsonObject.getString("review-description"));
                reviewinfo.add(review);
            }
            vendorReviewAdapter = new Ced_VendorReviewAdapter(Ced_AddVendorReview.this, reviewinfo);
            int cp = reviewlist.getFirstVisiblePosition();
            reviewlist.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
            reviewlist.setDividerHeight(0);
            reviewlist.setAdapter(vendorReviewAdapter);
            reviewlist.setSelectionFromTop(cp + 1, 0);
            vendorReviewAdapter.notifyDataSetChanged();
            load = true;
        }

    }

    private void applydata(String Jstring) throws JSONException {
        JSONObject object = new JSONObject(Jstring);
        JSONObject data = object.getJSONObject("data");
        String sucess = data.getString("status");
        if (sucess.equals("true")) {
            JSONArray productreview = data.getJSONArray("productreview");
            for (int i = 0; i < productreview.length(); i++) {
                JSONObject jsonObject = productreview.getJSONObject(i);
                HashMap<String, String> review = new HashMap<String, String>();
                review.put("v_review", jsonObject.getString("v_review"));
                review.put("review-by", jsonObject.getString("review-by"));
                review.put("posted_on", jsonObject.getString("posted_on"));
                review.put("review-title", jsonObject.getString("review-title"));
                review.put("review-description", jsonObject.getString("review-description"));
                reviewinfo.add(review);
            }
            vendorReviewAdapter = new Ced_VendorReviewAdapter(Ced_AddVendorReview.this, reviewinfo);
            reviewlist.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
            reviewlist.setDividerHeight(0);
            reviewlist.setAdapter(vendorReviewAdapter);
        }
        else {
           // showmsg(getResources().getString(R.string.bethefirstone));
            Intent intent1 = new Intent(getApplicationContext(), Ced_WriteVendorReview.class);
            intent1.putExtra("vendor_id", vendor_id);
            intent1.putExtra("customer_id", session.getCustomerid());
            startActivity(intent1);
            finish();
        }


    }


}
