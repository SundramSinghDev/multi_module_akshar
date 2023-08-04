package com.akshar.store.ui.newhomesection.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.gson.JsonObject;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.base.utilapplication.AnalyticsApplication;
import com.akshar.store.ui.networkhandlea_activities.Ced_NoInternetconnection;
import com.akshar.store.ui.newhomesection.adapter.BannerRecycler_Adapter;
import com.akshar.store.ui.searchsection.activity.Ced_Searchview;
import com.akshar.store.ui.websection.Ced_Weblink;
import com.akshar.store.databinding.HomeBannerDynamicBinding;
import com.akshar.store.databinding.HomeCategorySliderDynamicBinding;
import com.akshar.store.databinding.HomeDealSliderDynamicBinding;
import com.akshar.store.databinding.HomeOtherCategoryDynamicBinding;
import com.akshar.store.databinding.HomeSingleBannerDynamicBinding;
import com.akshar.store.databinding.HomeTrendingProductsDynamicBinding;
import com.akshar.store.databinding.NewTheme2HomepageBinding;
import com.akshar.store.rest.ApiResponse;
import com.akshar.store.ui.homesection.viewmodel.HomeViewModel;
import com.akshar.store.ui.newhomesection.adapter.BannerPagerAdapter;
import com.akshar.store.ui.newhomesection.adapter.HomeCategoryListAdapter;
import com.akshar.store.ui.newhomesection.adapter.HomeDealSliderAdapter;
import com.akshar.store.ui.newhomesection.adapter.HomeOtherCategoryAdapter;
import com.akshar.store.ui.newhomesection.adapter.HomeProductListAdapter;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;
import com.akshar.store.ui.productsection.activity.Ced_New_Product_Listing;
import com.akshar.store.ui.searchsection.activity.Ced_Search;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Magenative_HomePageNewTheme extends Ced_NavigationActivity  {
    NewTheme2HomepageBinding newTheme2HomepageBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    HomeViewModel homeViewModel;
    LinearLayout home_parent;
    //--------------------------
    public static final int RC_APP_UPDATE = 809;
    public AppUpdateManager mAppUpdateManager ;
    public InstallStateUpdatedListener installStateUpdatedListener;
    public BroadcastReceiver mMessageReceiver = null;
    String currentVersion, latestVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        newTheme2HomepageBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.new_theme2_homepage, content, true);
        homeViewModel = new ViewModelProvider(Magenative_HomePageNewTheme.this, viewModelFactory).get(HomeViewModel.class);
        home_parent=newTheme2HomepageBinding.homeParent;
        selecthometab();
        showplaystoreupdatepopup_accordingly();
        JsonObject param=new JsonObject();
        if (cedSessionManagement.getStoreId() != null) {
            param.addProperty("store_id", cedSessionManagement.getStoreId());
        }
        requesthomepagedata(param);

        newTheme2HomepageBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requesthomepagedata(param);
                newTheme2HomepageBinding.swipeRefresh.setRefreshing(false);
            }
        });
        newTheme2HomepageBinding.edtsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyClickHandler().gotosearch(v);
            }
        });
    }

    private void showplaystoreupdatepopup_accordingly() {
        DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        Date currentTime = Calendar.getInstance().getTime();
        Date newDate = new Date(currentTime.getTime() + TimeUnit.HOURS.toMillis(8));
        if(session.getupdateavailablepopup()==null)
        {
            showupdateifavailableonplaystore();
            session.saveupdateavailablepopup(String.valueOf(newDate));
        }
        else
        {
            try {
                if ((format.parse(String.valueOf(currentTime))).after(format.parse(session.getupdateavailablepopup()))) {
                    //outdated
                    showupdateifavailableonplaystore();
                    session.saveupdateavailablepopup(String.valueOf(newDate));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void requesthomepagedata(JsonObject param)
    {
        if (cedConnectionDetector.isConnectingToInternet()) {
            homeViewModel.getnewthemehomepage(this, param).observe(this, this::HomeResponse);
        }
        else
        {
            try {
                if(cedSessionManagement.get_homedata()!=null)
                {
                    showhomepage(cedSessionManagement.get_homedata());
                }
                else
                {
                    Intent intent = new Intent(Magenative_HomePageNewTheme.this, Ced_NoInternetconnection.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    protected void HomeResponse(ApiResponse apiResponse) {
            switch (apiResponse.status) {
                case SUCCESS:
                        try
                        {
                            cedSessionManagement.save_homedata(Objects.requireNonNull(apiResponse.data));
                            showhomepage(Objects.requireNonNull(apiResponse.data));
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

    private void showhomepage(String output) throws JSONException {
        JSONObject response = new JSONObject(output.toString()).getJSONObject("data");
        if (response.getString("status").equalsIgnoreCase("success"))
        {
            newTheme2HomepageBinding.homecontainer.setVisibility(View.VISIBLE);
            home_parent.removeAllViews();
            JSONArray design = response.getJSONArray("design");
            for (int i = 0; i < design.length(); i++) {
                try {
                    JSONObject object = design.getJSONObject(i);
                    if (object.getString("link_type").equalsIgnoreCase("banner_slider")) {
                        JSONArray banner_response = object.getJSONArray("widget");
                       /* if (banner_response.length() > 1) {*/
                            createBanner(banner_response,object.getString("banner_orientiation"));
                        /*} else {
                            createSingleBanner(banner_response.getJSONObject(0));
                        }*/
                    }
                    else if (object.getString("link_type").equalsIgnoreCase("animation_banner")) {
                        JSONObject banner = object.getJSONObject("banner");
                        String type_of_banner=object.getString("type_of_banner");
                        createSingleBanner(banner,type_of_banner);
                    }
                    else if (object.getString("link_type").equalsIgnoreCase("category_slider")) {
                        String category_type = object.getString("category_type");
                        String category_shape = object.getString("category_shape");
                        JSONArray categories = object.getJSONArray("categories");
                        createCategory(categories,category_type,category_shape);
                    }
                    else if (object.getString("link_type").equalsIgnoreCase("products_slider")) {
                        JSONArray products = object.getJSONArray("products");
                        String cat_id="";
                        if(object.has("category_id"))
                        {
                            cat_id=object.getString("category_id");
                        }
                        createTrendingProducts(products, object.getString("title"),cat_id);
                    }
                    else if (object.getString("link_type").equalsIgnoreCase("dealgroup_slider")) {
                        JSONObject deal_group = object.getJSONObject("deal_group");
                        CreateDealSlider(deal_group);
                    }

                   /* else if (object.getString("link_type").equalsIgnoreCase("category_slider") && object.getString("title").equalsIgnoreCase("another_category_slider")) {
                        JSONArray categories = object.getJSONArray("categories");
                        otherCategory(categories);
                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            home_parent.setVisibility(View.VISIBLE);
        }
        else
        {
            if(response.has("message"))
            {
                showmsg(response.getString("message"));
            }
            home_parent.setVisibility(View.GONE);
        }
    }

    private void CreateDealSlider(JSONObject deal_group)
    {
        try
        {
            Long deal_duration=deal_group.getLong("deal_duration");
            String title=deal_group.getString("title");
            JSONArray content=deal_group.getJSONArray("content");
            HomeDealSliderDynamicBinding dealSliderDynamicBinding= DataBindingUtil.inflate(getLayoutInflater(),R.layout.home_deal_slider_dynamic,  null,false);
            dealSliderDynamicBinding.dealTitle.setText(toTitleCase(title));
            final CounterClass timer = new CounterClass(deal_duration, 1000,"home_page", dealSliderDynamicBinding.dealTimer);
            timer.start();
            dealSliderDynamicBinding.dealsList.setAdapter(new HomeDealSliderAdapter(this, content));
            home_parent.addView(dealSliderDynamicBinding.getRoot());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createSingleBanner(final JSONObject single_banner,String type_of_banner) {
        try {
            HomeSingleBannerDynamicBinding homeSingleBannerDynamicBinding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.home_single_banner_dynamic, null, false);
            homeSingleBannerDynamicBinding.singleBannerImage.getLayoutParams().width=device_width-(device_width);
                    switch (type_of_banner)
                    {
                        case "small_size":
                            homeSingleBannerDynamicBinding.singleBannerImage.getLayoutParams().height= (Ced_NavigationActivity.device_width/4);
                            break;
                        case "medium_size":
                            //homeSingleBannerDynamicBinding.singleBannerImage.getLayoutParams().height=(Ced_NavigationActivity.device_width/2);
                            homeSingleBannerDynamicBinding.singleBannerImage.getLayoutParams().height=(Ced_NavigationActivity.device_width-(Ced_NavigationActivity.device_width/6));

                            break;
                        case "large_size":
                           // homeSingleBannerDynamicBinding.singleBannerImage.getLayoutParams().height=(Ced_NavigationActivity.device_width-(Ced_NavigationActivity.device_width/6));
                            break;
                        default:break;
                    }

            Glide.with(this)
                    .load(single_banner.getString("banner_image"))
                    .error(R.drawable.bannerplaceholder)
                    .placeholder(R.drawable.bannerplaceholder)
                    .into(homeSingleBannerDynamicBinding.singleBannerImage);
            BannerIntent( homeSingleBannerDynamicBinding.singleBannerImage,single_banner.getString("link_to"),single_banner.getString("product_id"));
            home_parent.addView(homeSingleBannerDynamicBinding.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void BannerIntent(ImageView singleBannerImage, String link_to, String product_id) {
        singleBannerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (link_to.equals("category")) {
                        Intent intent = new Intent(Magenative_HomePageNewTheme.this, Ced_New_Product_Listing.class);
                        intent.putExtra("ID", product_id);
                        startActivity(intent);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    }
                    if (link_to.equals("product")) {
                        Intent prod_link = new Intent(Magenative_HomePageNewTheme.this, Ced_NewProductView.class);
                        prod_link.putExtra("product_id", product_id);
                        startActivity(prod_link);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    }
                    if (link_to.equals("website")) {
                        Intent weblink = new Intent(Magenative_HomePageNewTheme.this, Ced_Weblink.class);
                        weblink.putExtra("link", product_id);
                        startActivity(weblink);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void createBanner(JSONArray widget,String banner_orientiation) {
        try {
            HomeBannerDynamicBinding homeBannerDynamicBinding= DataBindingUtil.inflate(getLayoutInflater(),R.layout.home_banner_dynamic,  null,false);
            if(banner_orientiation.equalsIgnoreCase("vertical"))
            {
                homeBannerDynamicBinding.verticalbannerRecycler.setVisibility(View.VISIBLE);
                homeBannerDynamicBinding.verticalbannerRecycler.setLayoutManager(new GridLayoutManager(this,2,RecyclerView.VERTICAL,false));
                homeBannerDynamicBinding.verticalbannerRecycler.setAdapter(new BannerRecycler_Adapter(Magenative_HomePageNewTheme.this, widget,banner_orientiation));
            }
            else
            {
                        homeBannerDynamicBinding.bannerSection.setVisibility(View.VISIBLE);
                        homeBannerDynamicBinding.MageNativeHomepagebanner.getLayoutParams().height=(Ced_NavigationActivity.device_width/2);
                        homeBannerDynamicBinding.MageNativeHomepagebanner.setAdapter(new BannerPagerAdapter(widget, Magenative_HomePageNewTheme.this));
                        homeBannerDynamicBinding.tabIndecator.setupWithViewPager(homeBannerDynamicBinding.MageNativeHomepagebanner);
                        // homeBannerDynamicBinding.MageNativeHomepagebanner.startAutoScroll();
                        homeBannerDynamicBinding.MageNativeHomepagebanner.setInterval(3000);
                        homeBannerDynamicBinding.MageNativeHomepagebanner.setCycle(true);
                        //     pageSwitcher(widget, MageNative_homepagebanner);
            }
            home_parent.addView(homeBannerDynamicBinding.getRoot());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void createCategory(JSONArray category_response, String category_type,String category_shape) {
        HomeCategorySliderDynamicBinding homeCategorySliderDynamicBinding= DataBindingUtil.inflate(getLayoutInflater(),R.layout.home_category_slider_dynamic,  null,false);
        if (!category_type.equalsIgnoreCase("slider"))
        {
            homeCategorySliderDynamicBinding.homeCategoryList.setLayoutManager((new GridLayoutManager(this,3, RecyclerView.VERTICAL, false)));
        }
        homeCategorySliderDynamicBinding.homeCategoryList.setAdapter(new HomeCategoryListAdapter(this, category_response,category_type,category_shape));
        home_parent.addView(homeCategorySliderDynamicBinding.getRoot());
    }
    private void createTrendingProducts(JSONArray products, String title, final String category_id) {
        HomeTrendingProductsDynamicBinding homeTrendingProductsDynamicBinding= DataBindingUtil.inflate(getLayoutInflater(),R.layout.home_trending_products_dynamic, null, false);
       if(category_id.equals(""))
       {
           homeTrendingProductsDynamicBinding.viewAll.setVisibility(View.GONE);
       }
        homeTrendingProductsDynamicBinding.viewAll.setOnClickListener(view -> {
            Intent intent = new Intent(Magenative_HomePageNewTheme.this, Ced_New_Product_Listing.class);
            intent.putExtra("ID", category_id);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        });
        homeTrendingProductsDynamicBinding.trendingProductTitle.setText(toTitleCase(title));
        homeTrendingProductsDynamicBinding.homeProductsList.setAdapter(new HomeProductListAdapter(this, products));
        home_parent.addView(homeTrendingProductsDynamicBinding.getRoot());
    }

    public class MyClickHandler{
        public void gotosearch(View v)
        {
            Log.d(Urls.TAG, "gotosearch: ");
           Intent search = new Intent(getApplicationContext(), Ced_Searchview.class);
            //search.putExtra("ID",newTheme2HomepageBinding.edtsearch.getText().toString());
            /*Intent search = new Intent(getApplicationContext(), Ced_New_Product_Listing.class);
            search.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);*/
            search.putExtra("issearchpage",true);
            startActivity(search);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
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
    public void onBackPressed() {
        super.onBackPressed();
        AnalyticsApplication.productresponse = "";
        AnalyticsApplication.categoryresponse = "";
        AnalyticsApplication.dealResponse = "";
        AnalyticsApplication.loadedproducts = false;
        AnalyticsApplication.loadeddeal = false;
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void showupdateifavailableonplaystore() {

        mAppUpdateManager = AppUpdateManagerFactory.create(Magenative_HomePageNewTheme.this);

        installStateUpdatedListener= state -> {
            if (state.installStatus() == InstallStatus.DOWNLOADED){
                popupSnackbarForCompleteUpdate();
            } else if (state.installStatus() == InstallStatus.INSTALLED){
                Log.i("REpo", "INSTALLED" );
                if (mAppUpdateManager != null){
                    mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                }
            } else {
                Log.i("REpo", "InstallStateUpdatedListener: main state: " + state.installStatus());
            }
        };

        mAppUpdateManager.registerListener(installStateUpdatedListener);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) ){
                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.FLEXIBLE, Magenative_HomePageNewTheme.this, RC_APP_UPDATE);
                }
                catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED ){
                popupSnackbarForCompleteUpdate();
            } else {
                Log.e("REpo", "checkForAppUpdateAvailability splash: main something else "+ appUpdateInfo.installStatus());
                getCurrentVersion();
            }
        });

      /*  FakeAppUpdateManager fakeAppUpdateManager = new FakeAppUpdateManager(this);
        fakeAppUpdateManager.setUpdateAvailable(25); // add app version code greater than current version.
        fakeAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, AppUpdateType.FLEXIBLE, Splash.this, RC_APP_UPDATE);
                    }
                    catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.e("REpo", "onActivityResult: app download failed");
            }
        }
    }
    public void popupSnackbarForCompleteUpdate() {
        Log.d("REpo", "popupSnackbarForCompleteUpdate: main");
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.MageNative_frame_container),
                        "New app is ready!",
                        Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Install", view -> {
            if (mAppUpdateManager != null){
                mAppUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.AppTheme));
        snackbar.show();
    }


    private void getCurrentVersion(){
        PackageManager pm = Magenative_HomePageNewTheme.this.getPackageManager();
        PackageInfo pInfo = null;
        try {
            pInfo =  pm.getPackageInfo(Magenative_HomePageNewTheme.this.getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;

        new GetLatestVersion().execute();

    }

    private class GetLatestVersion extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                //It retrieves the latest version by scraping the content of current version from play store at runtime
                Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=com.akshar.store&hl=en").get();
                latestVersion = doc.getElementsByClass("htlgb").get(6).text();
                Log.d("REpo", "doInBackground: "+latestVersion);
            }catch (Exception e){
                e.printStackTrace();

            }

            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(latestVersion!=null) {
                try {
                    if ((!currentVersion.equalsIgnoreCase(latestVersion)) && (Float.valueOf(latestVersion) > Float.valueOf(currentVersion))){
                        if(!isFinishing()){ //This would help to prevent Error : BinderProxy@45d459c0 is not valid; is your activity running? error
                            showUpdateDialog();
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            super.onPostExecute(jsonObject);
        }
    }

    private void showUpdateDialog()
    {
         new MaterialAlertDialogBuilder(this,R.style.MaterialDialog)
        .setTitle("A New Update is Available")
        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=com.akshar.store")));
                dialog.dismiss();
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
        .setCancelable(false)
        .show();
    }

}