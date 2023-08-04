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

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.ui.networkhandlea_activities.Ced_NoInternetconnection;
import com.akshar.store.databinding.MagenativeWriteVendorReviewBinding;
import com.akshar.store.rest.ApiResponse;
import com.akshar.store.ui.sellersection.viewmodel.WriteVendorReviewViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ced_WriteVendorReview extends Ced_NavigationActivity
{

    MagenativeWriteVendorReviewBinding magenativeWriteVendorReviewBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    WriteVendorReviewViewModel writeVendorReviewViewModel;
    String vendor_id;
    String customer_id;
    JSONObject reviewinfo;
    JsonObject dataforreview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        magenativeWriteVendorReviewBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_write_vendor_review, content, true);

        writeVendorReviewViewModel = new ViewModelProvider(Ced_WriteVendorReview.this, viewModelFactory).get(WriteVendorReviewViewModel.class);
        reviewinfo=new JSONObject();
        dataforreview=new JsonObject();
        if(cedConnectionDetector.isConnectingToInternet())
        {
            Intent intent=getIntent();
            vendor_id=intent.getStringExtra("vendor_id");
            customer_id=intent.getStringExtra("customer_id");
                dataforreview.addProperty("customer_id",customer_id);
                dataforreview.addProperty("vendor_id",vendor_id);
            magenativeWriteVendorReviewBinding.MageNativeSubmitReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (magenativeWriteVendorReviewBinding.MageNativeNickname.getText().toString().isEmpty()) {
                        magenativeWriteVendorReviewBinding.MageNativeNickname.setError(getResources().getString(R.string.empty));
                        magenativeWriteVendorReviewBinding.MageNativeNickname.requestFocus();
                    } else {
                        if (magenativeWriteVendorReviewBinding.MageNativeSummary.getText().toString().isEmpty()) {
                            magenativeWriteVendorReviewBinding.MageNativeSummary.setError(getResources().getString(R.string.empty));
                            magenativeWriteVendorReviewBinding.MageNativeSummary.requestFocus();
                        } else {
                            if (magenativeWriteVendorReviewBinding.MageNativeReview.getText().toString().isEmpty()) {
                                magenativeWriteVendorReviewBinding.MageNativeReview.setError(getResources().getString(R.string.empty));
                                magenativeWriteVendorReviewBinding.MageNativeReview.requestFocus();
                            } else {
                                int childcount = magenativeWriteVendorReviewBinding.MageNativeRatingoptions.getChildCount();
                                JsonArray array=new JsonArray();
                                for (int c = 0; c < childcount; c++) {
                                    CardView cardView = (CardView) magenativeWriteVendorReviewBinding.MageNativeRatingoptions.getChildAt(c);
                                    LinearLayout layout = (LinearLayout) cardView.getChildAt(0);
                                    TextView option = (TextView) layout.getChildAt(0);
                                    RatingBar rating = (RatingBar) layout.getChildAt(1);
                                    int ratingvalue = (int) rating.getRating();
                                    int ratingvaluemultipleoftwenty = ratingvalue * 20;
                                    try {
                                        JsonObject object=new JsonObject();
                                        object.addProperty("rating_code",String.valueOf(reviewinfo.get(option.getText().toString())));
                                        object.addProperty("rating_value",String.valueOf(ratingvaluemultipleoftwenty));
                                        array.add(object);
                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                dataforreview.add("ratings", array);

                                dataforreview.addProperty("customer_name", magenativeWriteVendorReviewBinding.MageNativeNickname.getText().toString());
                                    dataforreview.addProperty("subject", magenativeWriteVendorReviewBinding.MageNativeSummary.getText().toString());
                                    dataforreview.addProperty("review", magenativeWriteVendorReviewBinding.MageNativeReview.getText().toString());


                                magenativeWriteVendorReviewBinding.MageNativeSubmitReview.setText(getResources().getString(R.string.sunmitting));
                                try {


                                    writeVendorReviewViewModel.submitdataforvendorreview(Ced_WriteVendorReview.this, dataforreview).observe(Ced_WriteVendorReview.this, apiResponse -> {
                                        switch (apiResponse.status) {
                                            case SUCCESS:
                                                try {
                                                    JSONObject  object = new JSONObject(apiResponse.data);
                                                    String success = object.getJSONObject("data").getString("status");
                                                    if (success.equals("true")) {
                                                        magenativeWriteVendorReviewBinding.MageNativeSubmitReview.setText(getResources().getString(R.string.submit));
                                                        Intent intent = new Intent(getApplicationContext(), Ced_Seller_Shop.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                        intent.putExtra("ID", vendor_id);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        magenativeWriteVendorReviewBinding.MageNativeSubmitReview.setText(getResources().getString(R.string.submit));
                                                    }
                                                    showmsg(object.getJSONObject("data").getString("message"));
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
                                    /*Ced_ClientRequestResponseRest crr = new Ced_ClientRequestResponseRest(new Ced_AsyncResponse() {

                                        @Override
                                        public void processFinish(Object output) throws JSONException {
                                                JSONObject object = new JSONObject(output.toString());
                                                String success = object.getJSONObject("data").getString("status");
                                                if (success.equals("true")) {
                                                    magenativeWriteVendorReviewBinding.MageNativeSubmitReview.setText(getResources().getString(R.string.submit));
                                                    Intent intent = new Intent(getApplicationContext(), Ced_Seller_Shop.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                    intent.putExtra("ID", vendor_id);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    magenativeWriteVendorReviewBinding.MageNativeSubmitReview.setText(getResources().getString(R.string.submit));
                                                }
                                                showmsg(object.getJSONObject("data").getString("message"));

                                        }
                                    }, Ced_WriteVendorReview.this, "POST", dataforreview.toString());
                                    crr.execute(submitoption);*/
                                }
                                catch (Exception e)
                                {

                                    Intent main = new Intent(Ced_WriteVendorReview.this, Ced_MainActivity.class);
                                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(main);
                                }

                                writeVendorReviewViewModel.submitdataforvendorreview(Ced_WriteVendorReview.this, dataforreview).observe(Ced_WriteVendorReview.this, this::consumeSubmitResponse);

                            }
                        }
                    }
                }

                private void consumeSubmitResponse(ApiResponse apiResponse) {
                    switch (apiResponse.status) {
                        case SUCCESS:
                              submitreview(Objects.requireNonNull(apiResponse.data));

                            break;

                        case ERROR:
                            Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                            showmsg(getResources().getString(R.string.errorString));
                            break;
                    }
                }
            });

           /* try
            {
                Ced_ClientRequestResponse crr = new Ced_ClientRequestResponse(new Ced_AsyncResponse()
                {

                    @Override
                    public void processFinish(Object output) throws JSONException
                    {
                        Jstring = output.toString();
                        if(functionalityList.getExtensionAddon())
                        {
                            applydata();
                        }
                        else
                        {
                            Intent intent=new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }, Ced_WriteVendorReview.this);
                if(!(Ced_WriteVendorReview.this).isDestroyed())
                {
                    crr.execute(CurrrentUrl);
                }
                  }
            catch (Exception e)
            {

                Intent main=new Intent(Ced_WriteVendorReview.this,Ced_MainActivity.class);
                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(main);
            }*/

            writeVendorReviewViewModel.getrationoption(this).observe(this, this::consumeRatingResponse);
        }
        else
        {
            Intent intent=new Intent(getApplicationContext(), Ced_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void submitreview(String Jstring) {
        try {
            JSONObject object = new JSONObject(Jstring);
            String success = object.getJSONObject("data").getString("success");
            if (success.equals("true")) {
                magenativeWriteVendorReviewBinding.MageNativeSubmitReview.setText(getResources().getString(R.string.submit));
                showmsg(object.getJSONObject("data").getString("message"));
                Intent intent = new Intent(getApplicationContext(), Ced_Seller_Shop.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("ID", vendor_id);
                startActivity(intent);
                finish();
            } else {
                magenativeWriteVendorReviewBinding.MageNativeSubmitReview.setText(getResources().getString(R.string.submit));
                showmsg(object.getJSONObject("data").getString("message"));
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void consumeRatingResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:

                    try {
                        applydata_(Objects.requireNonNull(apiResponse.data));
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

    private void applydata_(String Jstring) throws JSONException
    {
        JSONObject object=new JSONObject(Jstring);
        JSONObject data=object.getJSONObject("data");
        JSONArray ratingoption=data.getJSONArray("rating-option");
        for(int i=0;i<ratingoption.length();i++)
        {
            JSONObject jsonObject=ratingoption.getJSONObject(i);
            reviewinfo.put(jsonObject.getString("label"),jsonObject.getString("rating_code"));
        }
        createform();
    }
    private void createform() throws JSONException {

        Iterator<String> keys = reviewinfo.keys();
        while(keys.hasNext()) {
            String key = keys.next();
                // do something with jsonObject here
                Log.d("REpo", "createform: ");
                View review=View.inflate(this, R.layout.magenative_add_review_option, null);
                TextView rating_option= (TextView) review.findViewById(R.id.MageNative_option);
                set_regular_font_fortext(rating_option);
                final RatingBar rating_bar= (RatingBar) review.findViewById(R.id.MageNative_review);
                rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        if (rating < 3)
                        {
                            Drawable stars = rating_bar.getProgressDrawable();
                            stars.setColorFilter(Color.parseColor("#ebebeb"), PorterDuff.Mode.SRC_ATOP);
                            LayerDrawable star = (LayerDrawable) rating_bar.getProgressDrawable();
                            star.getDrawable(2).setColorFilter(Color.parseColor("#DD2022"), PorterDuff.Mode.SRC_ATOP);
                        }
                        if (rating >= 3 && rating < 4)
                        {
                            Drawable stars=rating_bar.getProgressDrawable();
                            stars.setColorFilter(Color.parseColor("#ebebeb"), PorterDuff.Mode.SRC_ATOP);;
                            LayerDrawable star = (LayerDrawable) rating_bar.getProgressDrawable();
                            star.getDrawable(2).setColorFilter(Color.parseColor("#DB701B"), PorterDuff.Mode.SRC_ATOP);
                        }
                        if(rating >= 4)
                        {
                            Drawable stars=rating_bar.getProgressDrawable();
                            stars.setColorFilter(Color.parseColor("#ebebeb"), PorterDuff.Mode.SRC_ATOP);;
                            LayerDrawable star = (LayerDrawable) rating_bar.getProgressDrawable();
                            star.getDrawable(2).setColorFilter(Color.parseColor("#136A42"), PorterDuff.Mode.SRC_ATOP);
                        }
                    }
                });
                rating_option.setText(key);
                magenativeWriteVendorReviewBinding.MageNativeRatingoptions.addView(review);
        }
    }
}
