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

package com.akshar.store.ui.product_review_section.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import com.akshar.store.databinding.HeaderreviesBinding;
import com.akshar.store.databinding.MagenativeBethefirstonetoreviewBinding;
import com.akshar.store.databinding.MagenativeNewReviewlistingLayoutBinding;
import com.akshar.store.ui.product_review_section.adapter.Ced_Productreviewrecycler_Adapter;
import com.akshar.store.ui.product_review_section.viewmodel.ReviewListingViewModel;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Created by developer on 2/13/2016.
 */
@AndroidEntryPoint
public class Ced_ReviewListing extends Ced_NavigationActivity {
    String prodID = "";
    static final String KEY_OBJECT = "data";
    static final String KEY_STATUS = "status";
    static final String KEY_MESSAGE = "message";
    static final String KEY_PRODUCT_REVIEW = "productreview";
    static final String KEY_VOTES = "vote";
    static final String KEY_REVIEW_BY = "review-by";
    static final String KEY_POSTED_ON = "posted_on";
    static final String KEY_REVIEW_TITLE = "review-title";
    static final String KEY_REVIEW_DESCRIPTION = "review-description";
    static final String KEY_REVIEW_COUNT = "review-count";
    static final String KEY_GUEST_CAN_REVIEW = "guest-can-review";
    static final String KEY_RATING_CODE = "rating-code";
    static final String KEY_RATING_VALUE_PRICE = "rating-value-price";
    static final String KEY_RATING_VALUE_VALUE = "rating-value-value";
    static final String KEY_RATING_VALUE_QUALITY = "rating-value-quality";
    static final String KEY_REVIEWED_PRODUCT = "reviewed-product";
    static final String KEY_OVERALL_REVIEW = "overall-review";
    String guest_can_review, message, review_count,  reviewed_product, overall_review;
    int current = 1;
    String prod_id;
    MagenativeNewReviewlistingLayoutBinding binding1;
    @Inject
    ViewModelFactory viewModelFactory;
    ReviewListingViewModel reviewListingViewModel;
    HeaderreviesBinding headerreviesBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_new_reviewlisting_layout, content, true);
        reviewListingViewModel = new ViewModelProvider(Ced_ReviewListing.this, viewModelFactory).get(ReviewListingViewModel.class);
        headerreviesBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.headerrevies, null, false);
        prod_id = getIntent().getStringExtra("Product_id");
        Drawable myFabSrc = getResources().getDrawable(android.R.drawable.ic_input_add);
       //copy it in a new one
        Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
        willBeWhite.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        binding1.MageNativeAddreview.setImageDrawable(willBeWhite);
        binding1.MageNativeAddreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Ced_AddReview.class);
                intent.putExtra("productname", reviewed_product);
                intent.putExtra("prod_id", prod_id);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            }
        });
        JsonObject objectdata=new JsonObject();
       if (cedSessionManagement.getStoreId() != null)
       {
             objectdata.addProperty("store_id", cedSessionManagement.getStoreId());
       }
        objectdata.addProperty("prodID", prod_id);
        /*objectdata.addProperty("page",current);*/
        objectdata.addProperty("show_all", "true");

        reviewListingViewModel.productreviewlisting(Ced_ReviewListing.this, objectdata).observe(Ced_ReviewListing.this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                         fetchdata(Objects.requireNonNull(apiResponse.data));
                    break;

                case ERROR:
                    showmsg(getResources().getString(R.string.errorString));
                    break;
            }
        });

    }

    private void fetchdata(String output_data) {
        try {
            JSONObject jsonObj = new JSONObject(output_data);
            if (jsonObj.has("header") && jsonObj.getString("header").equals("false")) {
                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            }
            else {
                String  respo = jsonObj.getJSONObject(KEY_OBJECT).getString(KEY_STATUS);
                JSONObject dataobject=jsonObj.getJSONObject(KEY_OBJECT);
                if (respo.equals("no_review")) {
                    showmsg(dataobject.getString(KEY_MESSAGE));
                    reviewed_product = dataobject.getString(KEY_REVIEWED_PRODUCT);
                    guest_can_review = dataobject.getString(KEY_GUEST_CAN_REVIEW);
                    if (!(session.isLoggedIn())) {
                        if (guest_can_review.equals("0")) {  // view.setVisibility(View.GONE);
                           showmsg(getResources().getString(R.string.registercanreview));
                            binding1.MageNativeAddreview.setVisibility(View.GONE);
                            finish();
                        } else {
                            MagenativeBethefirstonetoreviewBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_bethefirstonetoreview, content, true);
                            binding.MageNativeText.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), Ced_AddReview.class);
                                    intent.putExtra("productname", reviewed_product);
                                    intent.putExtra("prod_id", prod_id);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                }
                            });
                        }
                    } else {
                       showmsg(message);
                        MagenativeBethefirstonetoreviewBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_bethefirstonetoreview, content, true);

                        binding.MageNativeText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), Ced_AddReview.class);
                                intent.putExtra("productname", reviewed_product);
                                intent.putExtra("prod_id", prod_id);
                                startActivity(intent);
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            }
                        });
                    }
                }
                else if (respo.equals("success")) {
                    binding1.mainreviewlisting.setVisibility(View.VISIBLE);
                    if (dataobject.has(KEY_REVIEW_COUNT)) {
                        review_count = dataobject.getString(KEY_REVIEW_COUNT);
                        headerreviesBinding.MageNativeReviewProductReview.setText(getResources().getString(R.string.totalreviews) + "" + review_count);
                        set_regular_font_fortext(headerreviesBinding.MageNativeReviewProductReview);
                    }
                    if (dataobject.has(KEY_GUEST_CAN_REVIEW)) {
                        guest_can_review = dataobject.getString(KEY_GUEST_CAN_REVIEW);
                    }
                    if (dataobject.has(KEY_REVIEWED_PRODUCT)) {
                        reviewed_product = dataobject.getString(KEY_REVIEWED_PRODUCT);
                        headerreviesBinding.MageNativeReviewProductName.setText(reviewed_product);
                        set_regular_font_fortext(headerreviesBinding.MageNativeReviewProductName);
                    }
                    if (dataobject.has(KEY_OVERALL_REVIEW)) {
                        overall_review = dataobject.getString(KEY_OVERALL_REVIEW);
                        Float aFloat = Float.parseFloat(overall_review);
                        Double value = aFloat.doubleValue();
                        if (aFloat < 2) {
                            headerreviesBinding.MageNativeReviewsection.setBackground(getResources().getDrawable(R.drawable.round_corner_red));
                            headerreviesBinding.MageNativeReviewtext.setText(new DecimalFormat("#.#").format(value));
                        }
                        if (aFloat >= 2 && aFloat < 4) {
                            headerreviesBinding.MageNativeReviewsection.setBackground(getResources().getDrawable(R.drawable.round_corner_yellow));
                            headerreviesBinding.MageNativeReviewtext.setText(new DecimalFormat("#.#").format(value));
                            headerreviesBinding.MageNativeReviewtext.setTextColor(getResources().getColor(R.color.black));
                            headerreviesBinding.MageNativeStar.setImageResource(R.drawable.stat_black);
                        }
                        if (aFloat >= 4) {
                            headerreviesBinding.MageNativeReviewsection.setBackground(getResources().getDrawable(R.drawable.round_corner));
                            headerreviesBinding.MageNativeReviewtext.setText(new DecimalFormat("#.#").format(value));
                        }
                        set_regular_font_fortext(headerreviesBinding.MageNativeReviewtext);
                    }
                    if (!(session.isLoggedIn())) {
                        if (guest_can_review.equals("0")) { //  view.setVisibility(View.GONE);
                            showmsg(getResources().getString(R.string.registercanreview));
                            binding1.MageNativeAddreview.setVisibility(View.GONE);
                        }
                    }
                    if (dataobject.has(KEY_PRODUCT_REVIEW)) {
                       JSONArray response = dataobject.getJSONArray(KEY_PRODUCT_REVIEW);
                        Ced_Productreviewrecycler_Adapter ced_productreviewrecycler_adapter=new Ced_Productreviewrecycler_Adapter(Ced_ReviewListing.this,response);
                        binding1.reviewList.setAdapter(ced_productreviewrecycler_adapter);
                        ced_productreviewrecycler_adapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    if(dataobject.has(KEY_MESSAGE))
                    showmsg(dataobject.getString(KEY_MESSAGE));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}