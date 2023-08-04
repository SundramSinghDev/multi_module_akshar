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
package com.akshar.store.ui.homesection.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.akshar.store.base.utilapplication.AnalyticsApplication;
import com.akshar.store.databinding.DealDesignOneBinding;
import com.akshar.store.databinding.DealDesignTwoBinding;
import com.akshar.store.ui.homesection.adapter.BannerSliderAdapter;
import com.akshar.store.ui.homesection.adapter.Ced_FeaturedProducts_Adapter;
import com.akshar.store.ui.homesection.adapter.DealsBannerAdapter;
import com.akshar.store.ui.homesection.adapter.DealsGridAdapter;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;
import com.akshar.store.ui.productsection.activity.Ced_New_Product_Listing;
import com.akshar.store.ui.websection.Ced_Weblink;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.R;
import com.akshar.store.databinding.MageNewHomePageBinding;
import com.akshar.store.rest.ApiResponse;
import com.akshar.store.ui.homesection.viewmodel.HomeViewModel;
import com.akshar.store.utils.ViewModelFactory;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ced_New_home_page extends Ced_NavigationActivity {
    MageNewHomePageBinding homePageBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    HomeViewModel homeViewModel;

    String theme;
    RelativeLayout bannersection;
    LinearLayout dealsection;
    RecyclerView feature_grid;
    JSONArray banner;
    int current = 1;
    NestedScrollView parent_home;
    BroadcastReceiver mRegistrationBroadcastReceiver;
    Timer timer;
    int page = 0;
    private int skipcounter = 0;
    TextView featuretittle;
    Boolean load=true;
    public static ArrayList<HashMap<String,String>> all_feature_productsjsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homePageBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.mage_new_home_page, content, true);
            homeViewModel = new ViewModelProvider(Ced_New_home_page.this, viewModelFactory).get(HomeViewModel.class);
            all_feature_productsjsonArray=new ArrayList<HashMap<String,String>>();
        theme = cedSessionManagement.getcategorytheme();
        bannersection = homePageBinding.bannersection;
        dealsection = homePageBinding.dealsection;
        feature_grid = homePageBinding.featureGrid;
        parent_home = homePageBinding.parentHome;
        featuretittle = homePageBinding.featuretittle;

//        parent_home.setScrollViewListener(this);

     /*   mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            private static final String TAG = "FirebaseMessageService";

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (Objects.requireNonNull(intent.getAction()).equals(MageNative_Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications'
                    FirebaseMessaging.getInstance().subscribeToTopic(MageNative_Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();
                    Log.e(TAG, "displayFirebaseRegId: ");
                } else if (intent.getAction().equals(MageNative_Config.PUSH_NOTIFICATION)) {   // new push notification is received
                    Log.e(TAG, "PUSH_NOTIFICATION: ");
                    String message = intent.getStringExtra("message");
                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                    Notification.Builder notificationBuilder = new Notification.Builder(context);
                    notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("title")
                            .setContentText("content")
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setCategory(message);
                    Log.d(TAG, "message: " + message);
                    //  txtMessage.setText(message);
                    Log.e(TAG, "onReceive:PUSH_NOTIFICATION ");
                    notificationBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
                    *//*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {NotificationManager notificationManager =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                                        notificationManager.notify(0, notificationBuilder.build()); }*//*
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    Objects.requireNonNull(notificationManager).notify(0, notificationBuilder.build());
                }
            }
        };*/

        if (cedSessionManagement.getStoreId() != null) {
            homeViewModel.getBannersData(this, cedSessionManagement.getStoreId()).observe(this, this::consumeBannersResponse);
            homeViewModel.getDealsData(this, cedSessionManagement.getStoreId()).observe(this, this::consumeDealsResponse);
            homeViewModel.getFeaturedData(this, String.valueOf(current), cedSessionManagement.getStoreId()).observe(this, this::consumeFeaturedResponse);
        } else {
            homeViewModel.getBannersData(this, "").observe(this, this::consumeBannersResponse);
            homeViewModel.getDealsData(this, "").observe(this, this::consumeDealsResponse);
            homeViewModel.getFeaturedData(this, String.valueOf(current), "").observe(this, this::consumeFeaturedResponse);
        }

       /* feature_grid.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {

                    current = current + 1;

                    if (management.getStoreId() != null) {
                        homeViewModel.getFeaturedData(Ced_New_home_page.this, String.valueOf(current), management.getStoreId()).observe(Ced_New_home_page.this, Ced_New_home_page.this::consumeFeaturedResponse);
                    }else{
                        homeViewModel.getFeaturedData(Ced_New_home_page.this, String.valueOf(current), "").observe(Ced_New_home_page.this, Ced_New_home_page.this::consumeFeaturedResponse);
                    }

                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
*/

        parent_home.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged()
            {
                View view = (View)parent_home.getChildAt(parent_home.getChildCount() - 1);

                int diff = (view.getBottom() - (parent_home.getHeight() + parent_home
                        .getScrollY()));
                if (diff == 0 & load) {
                        Log.i("UPDRIECTION", "IN");
                        load=false;
                        current = current + 1;
                        if (cedSessionManagement.getStoreId() != null) {
                            homeViewModel.getFeaturedData(Ced_New_home_page.this, String.valueOf(current), cedSessionManagement.getStoreId()).observe(Ced_New_home_page.this, Ced_New_home_page.this::consumeFeaturedResponse);
                        }else{
                            homeViewModel.getFeaturedData(Ced_New_home_page.this, String.valueOf(current), "").observe(Ced_New_home_page.this, Ced_New_home_page.this::consumeFeaturedResponse);
                        }

                }
            }
        });
    }

    private void consumeBannersResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                processBannerSlider(apiResponse.data);
                break;

            case ERROR:
                Log.e("ERROR ", Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }

    private void consumeDealsResponse(ApiResponse apiResponse){
        switch (apiResponse.status){
            case SUCCESS:
                processDeals(apiResponse.data);
                break;

            case ERROR:
                Log.e("ERROR ", Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }

    private void consumeFeaturedResponse(ApiResponse apiResponse){
        switch (apiResponse.status){
            case SUCCESS:
                processFeaturedProducts(apiResponse.data);
                break;

            case ERROR:
                Log.e("ERROR ", Objects.requireNonNull(apiResponse.error));
                load=false;
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }

    private void processFeaturedProducts(String data) {
        try {
            if(!data.contains("NO_PRODUCTS")|| !data.equalsIgnoreCase("[]")){
                JSONObject jobject = new JSONObject(data);
                featuretittle.setVisibility(View.VISIBLE);
                if (jobject.getString("status").equals("enabled")) {
                    if (jobject.has("featured_products")) {
                        load=true;
                        JSONArray productsjsonArray = jobject.getJSONArray("featured_products");
                        for(int k=0;k<productsjsonArray.length();k++)
                        {
                            JSONObject object=productsjsonArray.getJSONObject(k);
                            HashMap<String,String> allproducts=new HashMap<String,String>();
                            allproducts.put("product_name",object.getString("product_name"));
                            allproducts.put("product_image",object.getString("product_image"));
                            allproducts.put("product_id",object.getString("product_id"));
                            allproducts.put("stock_status",object.getString("stock_status"));
                            if(object.has("special_price"))
                            {
                                allproducts.put("special_price", object.getString("special_price"));
                            }
                            else
                            {
                                allproducts.put("special_price", "no_special");
                            }
                            if(object.has("regular_price"))
                            {
                                allproducts.put("regular_price",object.getString("regular_price"));
                            }
                            else
                            {
                                if(object.has("final_price"))
                                {
                                    allproducts.put("regular_price",object.getString("final_price"));
                                }
                                else
                                {
                                    allproducts.put("regular_price","");
                                }
                            }
                            all_feature_productsjsonArray.add(allproducts);
                        }
                    //Ced_FeaturedProducts_Adapter featuredProductsAdapter = new Ced_FeaturedProducts_Adapter(this, productsjsonArray);
                    Ced_FeaturedProducts_Adapter featuredProductsAdapter = new Ced_FeaturedProducts_Adapter(this, all_feature_productsjsonArray);
                    feature_grid.setAdapter(featuredProductsAdapter);
                    featuredProductsAdapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    load=false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processDeals(String output) {
        try {
            JSONObject object = new JSONObject(output);
            if (object.getBoolean("success")) {
                if (object.getJSONObject("data").get("deal_products") instanceof JSONArray) {
                    JSONArray deals = object.getJSONObject("data").getJSONArray("deal_products");
                    if (deals.length() > 0) {
                        dealsection.setVisibility(View.VISIBLE);
                        int count = 0;
                        for (int i = 0; i < deals.length(); i++) {
                            if (count == 3) {
                                count = 0;
                            }
                            count = count + 1;
                            processDealGroup(count, deals.getJSONObject(i));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processDealGroup(int count, JSONObject jsonObject) {
        try {
            switch (count) {
                case 1: {
                    designDealGroupOne(jsonObject);
                    break;
                }
                case 2: {
                    designDealGroupTwo(jsonObject);
                    break;
                }
                case 3: {
                    designDealGroupThree(jsonObject);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void designDealGroupOne(JSONObject jsonObject) {
        try {
            DealDesignOneBinding designOneBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.deal_design_one, dealsection, false);

            designOneBinding.dealgrouptittle.setText(jsonObject.getString("title"));

            if (jsonObject.getString("timer_status").equals("1")) {
                designOneBinding.dealgrouptimer.setVisibility(View.VISIBLE);
                final CounterClass timer = new CounterClass(jsonObject.getLong("deal_duration"), 1000, designOneBinding.dealgrouptimer);
                timer.start();
            }

            designOneBinding.dealsRecycler.setLayoutManager(new GridLayoutManager(Ced_New_home_page.this, 2));
            designOneBinding.dealsRecycler.setNestedScrollingEnabled(false);

            DealsGridAdapter gridAdapter = new DealsGridAdapter(Ced_New_home_page.this, jsonObject.getJSONArray("content"));
            designOneBinding.dealsRecycler.setAdapter(gridAdapter);

            dealsection.addView(designOneBinding.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void designDealGroupTwo(JSONObject jsonObject) {
        try {
            DealDesignTwoBinding designTwoBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.deal_design_two, dealsection, false);

            designTwoBinding.dealgrouptittle.setText(jsonObject.getString("title"));

            if (jsonObject.getString("timer_status").equals("1")) {
                designTwoBinding.dealgrouptimer.setVisibility(View.VISIBLE);
                final CounterClass timer = new CounterClass(jsonObject.getLong("deal_duration"), 1000, designTwoBinding.dealgrouptimer);
                timer.start();
            }

//            Deal_Adapter pagerbanners = new Deal_Adapter(Ced_New_home_page.this.getSupportFragmentManager(), Ced_New_home_page.this, jsonObject.getJSONArray("content"));
            DealsBannerAdapter dealsBannerAdapter = new DealsBannerAdapter(Ced_New_home_page.this, jsonObject.getJSONArray("content"));
            designTwoBinding.imageSlider.setSliderAdapter(dealsBannerAdapter);

            //set indicator animation by using SliderLayout.IndicatorAnimations. :
            // WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            designTwoBinding.imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM);
            designTwoBinding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            designTwoBinding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
            designTwoBinding.imageSlider.setIndicatorSelectedColor(getResources().getColor(R.color.onwhitetextcolor));
            designTwoBinding.imageSlider.setIndicatorUnselectedColor(getResources().getColor(R.color.main_color_gray_lt));
            //set scroll delay in seconds :
            designTwoBinding.imageSlider.setScrollTimeInSec(5);
            designTwoBinding.imageSlider.setAutoCycle(true);
            designTwoBinding.imageSlider.startAutoCycle();

            dealsection.addView(designTwoBinding.getRoot());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void designDealGroupThree(JSONObject jsonObject) {
        try {
            View view3 = View.inflate(Ced_New_home_page.this, R.layout.deal_design_three, null);
            TextView dealgrouptittle = view3.findViewById(R.id.dealgrouptittle);
            TextView dealgrouptimer = view3.findViewById(R.id.dealgrouptimer);
            LinearLayout dealdata3 = view3.findViewById(R.id.dealdata3);
            dealgrouptittle.setText(jsonObject.getString("title"));
            if (jsonObject.getString("timer_status").equals("1")) {
                dealgrouptimer.setVisibility(View.VISIBLE);
                final CounterClass timer = new CounterClass(jsonObject.getLong("deal_duration"), 1000, dealgrouptimer);
                timer.start();
            }
            View deal;
            ImageView staticdeal;
            TextView dealtype;
            TextView dealid;
            for (int i = 0; i < jsonObject.getJSONArray("content").length(); i++) {
                deal = View.inflate(Ced_New_home_page.this, R.layout.dealfull, null);
                staticdeal = deal.findViewById(R.id.staticdeal);
                dealtype = deal.findViewById(R.id.dealtype);
                dealid = deal.findViewById(R.id.dealid);
                if (jsonObject.getJSONArray("content").getJSONObject(i).getString("deal_type").equals("1")) {
                    dealid.setText(jsonObject.getJSONArray("content").getJSONObject(i).getString("product_link"));
                }
                if (jsonObject.getJSONArray("content").getJSONObject(i).getString("deal_type").equals("2")) {
                    dealid.setText(jsonObject.getJSONArray("content").getJSONObject(i).getString("category_link"));
                }
                if (jsonObject.getJSONArray("content").getJSONObject(i).getString("deal_type").equals("3")) {
                    dealid.setText(jsonObject.getJSONArray("content").getJSONObject(i).getString("static_link"));
                }
                if (!(Ced_New_home_page.this.isDestroyed())) {
                    Glide.with(Ced_New_home_page.this)
                            .load(jsonObject.getJSONArray("content").getJSONObject(i).getString("deal_image_name"))

                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(staticdeal);
                }
                Log.i("DealText", "" + jsonObject.getJSONArray("content").getJSONObject(i).getString("offer_text"));
                dealtype.setText(jsonObject.getJSONArray("content").getJSONObject(i).getString("deal_type"));
                final ImageView finalStaticdeal = staticdeal;
                staticdeal.setOnClickListener(v -> {
                    RelativeLayout dealparent = (RelativeLayout) finalStaticdeal.getParent();
                    TextView deal_type = (TextView) dealparent.getChildAt(1);
                    TextView deal_id = (TextView) dealparent.getChildAt(2);
                    if (deal_type.getText().toString().equals("1")) {
                        Intent intent = new Intent(Ced_New_home_page.this, Ced_NewProductView.class);
                        intent.putExtra("product_id", deal_id.getText().toString());
                        startActivity(intent);
                        Ced_New_home_page.this.overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);

                    }
                    if (deal_type.getText().toString().equals("2")) {
                        Intent intent = new Intent(Ced_New_home_page.this, Ced_New_Product_Listing.class);
                        intent.putExtra("ID", deal_id.getText().toString());
                        startActivity(intent);
                        Ced_New_home_page.this.overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    }
                    if (deal_type.getText().toString().equals("3")) {
                        Intent intent = new Intent(Ced_New_home_page.this, Ced_Weblink.class);
                        intent.putExtra("link", deal_id.getText().toString());
                        startActivity(intent);
                        Ced_New_home_page.this.overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    }
                });
                dealdata3.addView(deal);
            }
            dealsection.addView(view3);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processBannerSlider(String output) {
        try {
            JSONObject object = new JSONObject(output);
            String bannerstatus = object.getJSONObject("data").getString("status");
            if (bannerstatus.equals("enabled")) {
                bannersection.setVisibility(View.VISIBLE);
                banner = object.getJSONObject("data").getJSONArray("banner");

                BannerSliderAdapter sliderAdapter = new BannerSliderAdapter(Ced_New_home_page.this, banner);
                homePageBinding.imageSlider.setSliderAdapter(sliderAdapter);

                //set indicator animation by using SliderLayout.IndicatorAnimations. :
                // WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                homePageBinding.imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM);
                homePageBinding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                homePageBinding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
                homePageBinding.imageSlider.setIndicatorSelectedColor(getResources().getColor(R.color.onwhitetextcolor));
                homePageBinding.imageSlider.setIndicatorUnselectedColor(getResources().getColor(R.color.white));
                //set scroll delay in seconds :
                homePageBinding.imageSlider.setScrollTimeInSec(5);
                homePageBinding.imageSlider.setAutoCycle(true);
                homePageBinding.imageSlider.startAutoCycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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
    public void onBackPressed() {
        super.onBackPressed();
        AnalyticsApplication.productresponse = "";
        AnalyticsApplication.categoryresponse = "";
        AnalyticsApplication.dealResponse = "";
        AnalyticsApplication.loadedproducts = false;
        AnalyticsApplication.loadeddeal = false;
    }

    public void pageSwitcher(int seconds) {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay
        // in
        // milliseconds
    }

   /* @Override
    public void onScrollChanged(ScrollViewExt scrollView) {
        try {
            View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
            int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));
            if (diff == 0) {
                skipcounter = skipcounter + 1;
                if (skipcounter == 2) {
                    Log.i("UPDRIECTION", "IN");
                    skipcounter = 0;
                    current = current + 1;

                    if (management.getStoreId() != null) {
                        homeViewModel.getFeaturedData(this, String.valueOf(current), management.getStoreId()).observe(this, this::consumeFeaturedResponse);
                    }else{
                        homeViewModel.getFeaturedData(this, String.valueOf(current), "").observe(this, this::consumeFeaturedResponse);
                    }
                }
            } else {
                Log.i("UPDRIECTION", "out" + diff);
                skipcounter = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    class RemindTask extends TimerTask {

        @Override
        public void run() {
            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            if (Ced_New_home_page.this == null)
                return;
            /*Ced_New_home_page.this.runOnUiThread(() -> {

                if (page > banner.length()) {
                    page = 0;
                    MageNative_homepagebanner.setCurrentItem(page++);
                } else {
                    MageNative_homepagebanner.setCurrentItem(page++);
                }
            });*/
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")
    public class CounterClass extends CountDownTimer {
        TextView counter;

        public CounterClass(long millisInFuture, long countDownInterval, TextView dealcounter) {
            super(millisInFuture, countDownInterval);
            counter = dealcounter;
        }

        @Override
        public void onFinish() {
            counter.setText("00:00:00");
        }

        @SuppressLint("NewApi")
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void onTick(long millisUntilFinished) {
            String hms = String.format("%02d:%02d:%02d:%02d", TimeUnit.MILLISECONDS.toDays(millisUntilFinished), TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

            counter.setText(hms);
        }
    }

}