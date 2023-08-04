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

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.R;
import com.google.android.gms.analytics.Tracker;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import com.akshar.store.databinding.MagenativeAddreviewBinding;
import com.akshar.store.databinding.MagenativeDynamicReviewLayoutBinding;
import com.akshar.store.ui.product_review_section.viewmodel.AddProductReviewViewModel;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ced_AddReview extends Ced_NavigationActivity {
    String desc, title, poster;
    static final String KEY_OBJECT = "data";
    static final String KEY_RATING_OPTION = "rating-option";
    static final String KEY_STATUS = "status";
    String  status, rating_id, rating_code,  usr_rating, prodid;
    JSONObject jsonObj = null;
    String price_review, value_review, quality_review, price_value, value_value, quality_value, Product_name;
    ArrayList<HashMap<String, String>> reviews_to_fill;
    HashMap<String, ArrayList> reviews_available;
    JSONArray response;
    private Tracker mTracker;
    JSONObject reviewinfo;

    @Inject
    ViewModelFactory viewModelFactory;
    AddProductReviewViewModel addProductReviewViewModel;
    MagenativeAddreviewBinding binding1;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_addreview, content, true);
        addProductReviewViewModel = new ViewModelProvider(Ced_AddReview.this, viewModelFactory).get(AddProductReviewViewModel.class);
        Product_name = getIntent().getStringExtra("productname");
        prodid = getIntent().getStringExtra("prod_id");
        reviewinfo = new JSONObject();
        binding1.MageNativeReviewedProd.setText(Product_name);
        getRating();
        binding1.MageNativeSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desc = binding1.MageNativeThoughts.getText().toString();
                title = binding1.MageNativeSummary.getText().toString();
                poster = binding1.MageNativeNickname.getText().toString();
                if (desc.length() < 0) {
                    binding1.MageNativeThoughts.setError(getResources().getString(R.string.empty));
                } else {
                    if (title.length() < 0) {
                        binding1.MageNativeSummary.setError(getResources().getString(R.string.empty));
                    } else {
                        if (poster.length() < 0) {
                            binding1.MageNativeNickname.setError(getResources().getString(R.string.empty));
                        } else {
                            for (int i = 0; i < binding1.MageNativeDynamicLayoutLinear.getChildCount(); i++) {
                                LinearLayout linearLayout = (LinearLayout) binding1.MageNativeDynamicLayoutLinear.getChildAt(i);
                                LinearLayout linearLayout1 = (LinearLayout) linearLayout.getChildAt(0);
                                RatingBar ratingBar = (RatingBar) linearLayout1.getChildAt(1);
                                TextView textView = (TextView) linearLayout1.getChildAt(2);
                                String rating_id = textView.getText().toString();
                                int review_stars = (int) ratingBar.getRating();
                                int ratings_to_send = (Integer.parseInt(rating_id) - 1) * 5 + review_stars;
                                try {
                                    reviewinfo.put(textView.getText().toString(), String.valueOf(ratings_to_send));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

                usr_rating = "3-" + price_value + "@2-" + value_value + "@1-" + quality_value;
                /*Ced_ClientRequestResponseRest cedClientRequestResponse = new Ced_ClientRequestResponseRest(new Ced_AsyncResponse() {
                    @Override
                    public void processFinish(Object output) throws JSONException {
                            revievedata(output.toString());
                    }
                }, Ced_AddReview.this, "POST", hashMap.toString());
                cedClientRequestResponse.execute(add_review_url);*/
                JsonObject hashMap = new JsonObject();
                hashMap.addProperty("ratings", reviewinfo.toString());
                hashMap.addProperty("title", title);
                hashMap.addProperty("nickname", poster);
                hashMap.addProperty("detail", desc);
                hashMap.addProperty("prodID", prodid);
                if (cedSessionManagement.getStoreId() != null) {
                    hashMap.addProperty("store_id", cedSessionManagement.getStoreId());
                }
                addProductReviewViewModel.addproductreview(Ced_AddReview.this,hashMap).observe(Ced_AddReview.this, apiResponse -> {
                    switch (apiResponse.status) {
                        case SUCCESS:
                            revievedata(Objects.requireNonNull(apiResponse.data));
                            break;

                        case ERROR:
                            showmsg(getResources().getString(R.string.errorString));
                            break;
                    }
                });
            }
        });

        binding1.MageNativePriceRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating == 5) {
                    price_review = String.valueOf(rating);
                    price_value = "15";
                }
                if (rating == 4) {
                    price_review = String.valueOf(rating);
                    price_value = "14";
                }
                if (rating == 3) {
                    price_review = String.valueOf(rating);
                    price_value = "13";
                }
                if (rating == 2) {
                    price_review = String.valueOf(rating);
                    price_value = "12";
                }
                if (rating == 1) {
                    price_review = String.valueOf(rating);
                    price_value = "11";
                }
            }
        });

        binding1.MageNativeValueRatings.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating == 5) {
                    value_review = String.valueOf(rating);
                    value_value = "10";
                }
                if (rating == 4) {
                    value_review = String.valueOf(rating);
                    value_value = "9";
                }
                if (rating == 3) {
                    value_review = String.valueOf(rating);
                    value_value = "8";
                }
                if (rating == 2) {
                    value_review = String.valueOf(rating);
                    value_value = "7";
                }
                if (rating == 1) {
                    value_review = String.valueOf(rating);
                    value_value = "6";
                }
            }
        });

        binding1.MageNativeQuantityReview.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating == 5) {
                    quality_review = String.valueOf(rating);
                    quality_value = "5";
                }
                if (rating == 4) {
                    quality_review = String.valueOf(rating);
                    quality_value = "4";
                }
                if (rating == 3) {
                    quality_review = String.valueOf(rating);
                    quality_value = "3";
                }
                if (rating == 2) {
                    quality_review = String.valueOf(rating);
                    quality_value = "2";
                }
                if (rating == 1) {
                    quality_review = String.valueOf(rating);
                    quality_value = "1";
                }
            }
        });

        Drawable stars_quantity = binding1.MageNativeQuantityReview.getProgressDrawable();
        Drawable stars_value = binding1.MageNativeValueRatings.getProgressDrawable();
        Drawable stars_price = binding1.MageNativePriceRating.getProgressDrawable();
        stars_value.setColorFilter(Color.parseColor("#ebebeb"), PorterDuff.Mode.SRC_ATOP);
        stars_price.setColorFilter(Color.parseColor("#ebebeb"), PorterDuff.Mode.SRC_ATOP);
        stars_quantity.setColorFilter(Color.parseColor("#ebebeb"), PorterDuff.Mode.SRC_ATOP);
        LayerDrawable stars = (LayerDrawable) binding1.MageNativeQuantityReview.getProgressDrawable();
        LayerDrawable stars2 = (LayerDrawable) binding1.MageNativeValueRatings.getProgressDrawable();
        LayerDrawable stars3 = (LayerDrawable) binding1.MageNativePriceRating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#FDB727"), PorterDuff.Mode.SRC_ATOP);
        stars2.getDrawable(2).setColorFilter(Color.parseColor("#FDB727"), PorterDuff.Mode.SRC_ATOP);
        stars3.getDrawable(2).setColorFilter(Color.parseColor("#FDB727"), PorterDuff.Mode.SRC_ATOP);
    }

    private void getRating() {
        addProductReviewViewModel.getproductratingoption(Ced_AddReview.this).observe(Ced_AddReview.this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                    getRatingOption(Objects.requireNonNull(apiResponse.data));
                    break;

                case ERROR:
                    showmsg(getResources().getString(R.string.errorString));
                    break;
            }
        });
    }

    private void revievedata(String output_data) {
        try {
            jsonObj = new JSONObject(output_data);
            if (jsonObj.has("header") && jsonObj.getString("header").equals("false")) {
                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                status = jsonObj.getJSONObject(KEY_OBJECT).getString(KEY_STATUS);
                if (status.equals("success")) {
                    /*Intent intent = new Intent(getApplicationContext(), Ced_ReviewListing.class);
                    intent.putExtra("Product_id", prodid);*/
                    Intent intent= new Intent(getApplicationContext(), Ced_NewProductView.class);
                    intent.putExtra("product_id",prodid);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
                showmsg(jsonObj.getJSONObject(KEY_OBJECT).getString("message").replace(".,",".\n"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getRatingOption(String output_data) {
        try {
            jsonObj = new JSONObject(output_data);
            if (jsonObj.has("header") && jsonObj.getString("header").equals("false")) {
                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                status = jsonObj.getJSONObject(KEY_OBJECT).getString(KEY_STATUS);
                if (status.equals("success")) {
                    reviews_to_fill = new ArrayList<>();
                    reviews_available = new HashMap<>();
                    if (jsonObj.getJSONObject(KEY_OBJECT).has(KEY_RATING_OPTION)) {
                        response = jsonObj.getJSONObject(KEY_OBJECT).getJSONArray(KEY_RATING_OPTION);

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject object = null;
                            object = response.getJSONObject(i);
                            if (object.has("rating-id")) {
                                rating_id = object.getString("rating-id");
                            }
                            if (object.has("rating-code")) {
                                rating_code = object.getString("rating-code");
                            }

                            HashMap hashMap = new HashMap();
                            hashMap.put("rating_id", rating_id);
                            hashMap.put("rating_code", rating_code);
                            reviews_to_fill.add(hashMap);
                            reviews_available.put(rating_id + "#" + rating_code, reviews_to_fill);
                        }

                        Iterator iterator = reviews_available.entrySet().iterator();
                        while (iterator.hasNext()) {
                            MagenativeDynamicReviewLayoutBinding binding2 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_dynamic_review_layout, null, false);
                            Map.Entry entry = (Map.Entry) iterator.next();
                            String key = String.valueOf(entry.getKey());
                            String parts[] = key.split("#");
                            binding2.MageNativeRatingId.setText(parts[0].toString());
                            binding2.MageNativeDynamciRatingCode.setText(parts[1].toString());

                            set_regular_font_fortext(binding2.MageNativeDynamciRatingCode);
                            binding2.MageNativeRatingDynamic.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                @Override
                                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                    if (rating < 3) {
                                        Drawable stars = binding2.MageNativeRatingDynamic.getProgressDrawable();
                                        stars.setColorFilter(Color.parseColor("#ebebeb"), PorterDuff.Mode.SRC_ATOP);
                                        LayerDrawable star = (LayerDrawable) binding2.MageNativeRatingDynamic.getProgressDrawable();
                                        star.getDrawable(2).setColorFilter(Color.parseColor("#DD2022"), PorterDuff.Mode.SRC_ATOP);
                                    }
                                    if (rating >= 3 && rating < 4) {
                                        Drawable stars = binding2.MageNativeRatingDynamic.getProgressDrawable();
                                        stars.setColorFilter(Color.parseColor("#ebebeb"), PorterDuff.Mode.SRC_ATOP);
                                        ;
                                        LayerDrawable star = (LayerDrawable) binding2.MageNativeRatingDynamic.getProgressDrawable();
                                        star.getDrawable(2).setColorFilter(Color.parseColor("#DB701B"), PorterDuff.Mode.SRC_ATOP);
                                    }
                                    if (rating >= 4) {
                                        Drawable stars = binding2.MageNativeRatingDynamic.getProgressDrawable();
                                        stars.setColorFilter(Color.parseColor("#ebebeb"), PorterDuff.Mode.SRC_ATOP);
                                        ;
                                        LayerDrawable star = (LayerDrawable) binding2.MageNativeRatingDynamic.getProgressDrawable();
                                        star.getDrawable(2).setColorFilter(Color.parseColor("#136A42"), PorterDuff.Mode.SRC_ATOP);
                                    }
                                }
                            });
                           /* if(binding1.getRoot().getParent() != null) {
                                ((ViewGroup)binding1.getRoot().getParent()).removeView(binding1.getRoot()); // <- fix
                            }*/
                            binding1.MageNativeDynamicLayoutLinear.addView(binding2.getRoot());
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}