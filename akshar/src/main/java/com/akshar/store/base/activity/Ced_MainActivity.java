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
package com.akshar.store.base.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.akshar.store.ui.networkhandlea_activities.Ced_ConnectionDetector;
import com.akshar.store.Ced_MageNative_SharedPrefrence.Ced_SessionManagement;
import com.akshar.store.Ced_MageNative_SharedPrefrence.Ced_SessionManagement_login;
import com.akshar.store.Ced_MageNative_Upgrade.Ced_Upgrade;
import com.akshar.store.R;
import com.akshar.store.base.viewmodel.MainViewModel;
import com.akshar.store.ui.networkhandlea_activities.Ced_ExpireActivity;
import com.akshar.store.utils.Ced_Load_Language;
import com.akshar.store.ui.networkhandlea_activities.Ced_NoInternetconnection;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import com.akshar.store.ui.websection.Ced_Weblink;
import com.akshar.store.databinding.MagenativeActivityMainBinding;
import com.akshar.store.rest.ApiResponse;
import com.akshar.store.ui.introsection.activity.Ced_Illustration;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;
import com.akshar.store.ui.productsection.activity.Ced_New_Product_Listing;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ced_MainActivity extends AppCompatActivity {

    public static int bottomtabposition = 0;
    Ced_ConnectionDetector cedConnectionDetector;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    @Inject
    ViewModelFactory viewModelFactory;
    MainViewModel mainViewModel;
    public static String latestcartcount = "no_count";
    private static int SPLASH_TIME_OUT = 500;
    String Jstring;
    String URL;
    Ced_SessionManagement cedSessionManagement;
    Ced_SessionManagement_login cedSessionManagement_login;
    JsonObject getcartcountdata;
    Ced_Load_Language cedLoad_language;
    boolean paid = true;
    String product_id = "";
    String ID = "";
    String link = "";
    String validitycheck = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cedConnectionDetector = new Ced_ConnectionDetector(Ced_MainActivity.this);
        MagenativeActivityMainBinding activityMainBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_activity_main, null, false);
        setContentView(activityMainBinding.getRoot());
        mainViewModel = new ViewModelProvider(Ced_MainActivity.this, viewModelFactory).get(MainViewModel.class);
        Window window = this.getWindow();
        getfbreleasesha();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        validitycheck = "https://magenative.cedcommerce.com/refund/download/validitycheck/app_id/2371";
        cedSessionManagement = Ced_SessionManagement.getCed_sessionManagement(Ced_MainActivity.this);
        cedSessionManagement_login = Ced_SessionManagement_login.getShredPrefs(Ced_MainActivity.this);
        cedLoad_language = new Ced_Load_Language();

        // cedLoad_language.setLanguagetoLoad("ar", Ced_MainActivity.this);
        if (cedSessionManagement.getStoreLocale() != null) {
            cedLoad_language.setLanguagetoLoad(cedSessionManagement.getStoreLocale(), Ced_MainActivity.this);
        }
        getcartcountdata = new JsonObject();
        if (getIntent().getDataString() != null) {
            String link = getIntent().getDataString();
            String[] datavalue = link.split("/");
            String valueid = datavalue[datavalue.length - 1];
            if (valueid.contains(getResources().getString(R.string.app_name).replace(" ", "_").trim())) {
                String[] id = valueid.split("#");
                product_id = id[0];
            } else {
                showmsg(getResources().getString(R.string.specifiedapp));
                moveTaskToBack(true);
                finish();
            }
        }

        if (getIntent().getStringExtra("fromnotification") != null) {
            if (getIntent().getStringExtra("product_id") != null) {
                product_id = getIntent().getStringExtra("product_id");
            }
            if (getIntent().getStringExtra("ID") != null) {
                ID = getIntent().getStringExtra("ID");
            }
            if (getIntent().getStringExtra("link") != null) {
                link = getIntent().getStringExtra("link");
            }
        }

        if (cedConnectionDetector.isConnectingToInternet() || cedSessionManagement.get_getcartcountdata() != null) {
            if (cedSessionManagement.getillustration().equals("new")) {
                new Handler().postDelayed(() -> {
                    cedSessionManagement.illustration("old");
                    load();
                }, 500);
            } else {
                if (paid) {
                    //requestaddonsurl();
                    processdata();
                } else {
                    if (cedSessionManagement.getvalidity() != null) {
                        String valid = cedSessionManagement.getvalidity();
                        String[] parts = valid.split("#");
                        if (parts[0].equals(getcurrentdate())) {
                            if (parts[1].equals("true")) {
                                //requestaddonsurl();
                                processdata();
                            } else if (parts[1].equals("false")) {
                                validitycheckurl();
                            } else {
                                Intent intent = new Intent(Ced_MainActivity.this, Ced_ExpireActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            }
                        } else {
                            validitycheckurl();
                        }
                    } else {
                        validitycheckurl();
                    }
                }
            }
        } else {
            setContentView(R.layout.magenative_nointernetconnection);
        }
    }

    private void getfbreleasesha() {
        //E1:28:63:BB:7E:E1:93:05:C0:34:3E:ED:B8:C7:E7:F7:F5:59:9A:3D

        byte[] sha1 = {
                (byte) 0xE1, 0x28, 0x63, (byte) 0xBB, (byte) 0x7E, (byte) 0xE1, (byte) 0x93, (byte) 0x05, (byte) 0xC0, (byte) 0x34, (byte) 0x3E, (byte) 0xED, (byte) 0xB8, (byte) 0xC7, (byte) 0xE7, (byte) 0xF7, (byte) 0xF5, 0x59, (byte) 0x9A, (byte) 0x3D
        };
        Log.d("REpo", "keyhashGooglePlaySignIn:" + Base64.encodeToString(sha1, Base64.NO_WRAP));
    }

    private void validitycheckurl() {
        try {
            mainViewModel.checkValidity(this, validitycheck).observe(this, this::consumeValidityCheck);
            /*//TODO
            Ced_ClientRequestResponse crr = new Ced_ClientRequestResponse(output -> {
                String Jstring = output.toString();
                try {
                    JSONObject object = new JSONObject(Jstring);
                    String success = object.getJSONObject("data").getString("success");
                    if (success.equals("true")) {
                        cedSessionManagement.savevalidity(getcurrentdate() + "#true");
                        requestaddonsurl();
                    } else {
                        cedSessionManagement.savevalidity(getcurrentdate() + "#false");
                        Intent intent = new Intent(Ced_MainActivity.this, Ced_ExpireActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Intent intent = new Intent(Ced_MainActivity.this, Ced_MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
            }, Ced_MainActivity.this);
            crr.execute(validitycheck);*/
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(Ced_MainActivity.this, Ced_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    private void consumeValidityCheck(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                try {
                    JSONObject object = new JSONObject(Objects.requireNonNull(apiResponse.data));
                    String success = object.getJSONObject("data").getString("success");
                    String message = object.getJSONObject("data").getString("message");
                    if (success.equals("true") || message.equalsIgnoreCase("Subscription End")) {
                        cedSessionManagement.savevalidity(getcurrentdate() + "#true");
                        // requestaddonsurl();
                        processdata();
                    } else {
                        cedSessionManagement.savevalidity(getcurrentdate() + "#false");
                        Intent intent = new Intent(Ced_MainActivity.this, Ced_ExpireActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Intent intent = new Intent(Ced_MainActivity.this, Ced_MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
                break;

            case ERROR:
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }

    private void load() {
        //cedSessionManagement.enableshaker(true);
        /*new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {*/
        Intent intent = new Intent(Ced_MainActivity.this, Ced_Illustration.class);
        startActivity(intent);
        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        finish();
           /* }
        }, 1000);*/
    }

    public String getcurrentdate() {
        Calendar date = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        String formatted = format1.format(date.getTime());
        return formatted;
    }

    public void applydata(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("header") && jsonObject.getString("header").equals("false")) {
                Intent intent = new Intent(Ced_MainActivity.this, Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else if (jsonObject.has("update") && jsonObject.getString("update").equals("true")) {
                Intent intent = new Intent(Ced_MainActivity.this, Ced_Upgrade.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                String success = jsonObject.getString("success");
                if (cedSessionManagement.getStoreId() == null) {
                    if (jsonObject.has("gender")) {
                        cedSessionManagement_login.savegender(jsonObject.getString("gender").toLowerCase());
                        cedSessionManagement_login.savename(jsonObject.getString("name"));
                    }
                    cedSessionManagement.saveStoreId(jsonObject.getString("default_store"));
                    cedSessionManagement.setcurrency(jsonObject.getString("currency_code"));
                    if (jsonObject.has("cart_id"))
                        cedSessionManagement.savecartid(String.valueOf(jsonObject.getInt("cart_id")));
                    String[] localecodearray = jsonObject.getString("locale").split("_");
                    String localecode = localecodearray[0];
                    cedSessionManagement.saveStorelocale(localecode);
                    cedLoad_language.setLanguagetoLoad(localecode, Ced_MainActivity.this);
                }
                if (success.equals("true")) {
                    latestcartcount = String.valueOf(jsonObject.getInt("items_count"));
                    if (jsonObject.has("webview_checkout") && jsonObject.getString("webview_checkout").equalsIgnoreCase("1")) {
                        cedSessionManagement.iswebcheckoutenabled(true);
                    } else {
                        cedSessionManagement.iswebcheckoutenabled(false);
                    }
                    if (jsonObject.has("cart_id"))
                        cedSessionManagement.savecartid(String.valueOf(jsonObject.getInt("cart_id")));
                    // cedSessionManagement.iswebcheckoutenabled(false);// this line needs to be removed just for testing
                }
                Categoriesrequest();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO
    public void request() {
        try {
            // JsonObject appversion = new JsonObject();
            //  PackageInfo pInfo = null;
            try {
                PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
                String version = pInfo.versionName;
                getcartcountdata.addProperty("app_version", version);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            // mainViewModel.getModulesList(Ced_MainActivity.this, appversion).observe(this, this::getEnabledAddons);
            request_cartcountdata();
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(Ced_MainActivity.this, Ced_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    private void request_cartcountdata() {
        if (cedConnectionDetector.isConnectingToInternet()) {
            mainViewModel.getCartCount(Ced_MainActivity.this, getcartcountdata).observe(this, this::getCartCountData);
        } else {
            if (cedSessionManagement.get_getcartcountdata() != null) {
                applydata(cedSessionManagement.get_getcartcountdata());
            } else {
                Intent intent = new Intent(Ced_MainActivity.this, Ced_NoInternetconnection.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            }

        }
    }

    private void getCartCountData(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                cedSessionManagement.save_getcartcountdata(apiResponse.data);
                applydata(apiResponse.data);
                break;

            case ERROR:
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }

    public void showmsg(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.AppTheme));
        snackbar.show();
    }

    public void show() {
        new Handler().postDelayed(() -> {
            if (product_id.isEmpty()) {
                if (ID.isEmpty()) {
                    if (link.isEmpty()) {
                        // Intent intent = new Intent(Ced_MainActivity.this, Ced_New_home_page.class);
                        Intent intent = new Intent(Ced_MainActivity.this, Magenative_HomePageNewTheme.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        finish();
                    } else {
                        Intent intent = new Intent(Ced_MainActivity.this, Ced_Weblink.class);
                        intent.putExtra("link", link);
                        startActivity(intent);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(Ced_MainActivity.this, Ced_New_Product_Listing.class);
                    intent.putExtra("ID", ID);
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    finish();
                }
            } else {
                Intent intent = new Intent(Ced_MainActivity.this, Ced_NewProductView.class);
                intent.putExtra("product_id", product_id);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                finish();
            }

        }, SPLASH_TIME_OUT);
    }

    @Override
    protected void onResume() {
        /*if (cedConnectionDetector.isConnectingToInternet()) {
            invalidateOptionsMenu();
            super.onResume();
        } else {
            Intent intent = new Intent(Ced_MainActivity.this, Ced_NoInternetconnection.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            super.onResume();
        }*/
        super.onResume();
    }

    public void processdata() {
        try {
            if (cedSessionManagement_login.isLoggedIn()) {
                getcartcountdata.addProperty("customer_id", cedSessionManagement_login.getCustomerid());
                if (cedSessionManagement.getStoreId() != null) {
                    getcartcountdata.addProperty("store_id", cedSessionManagement.getStoreId());
                }
                request();
            } else {
                if (cedSessionManagement.getCartId() != null) {
                    getcartcountdata.addProperty("cart_id", cedSessionManagement.getCartId());
                    if (cedSessionManagement.getStoreId() != null) {
                        getcartcountdata.addProperty("store_id", cedSessionManagement.getStoreId());
                    }
                    request();
                } else {
                    getcartcountdata.addProperty("cart_id", "0");
                    request();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Categoriesrequest() {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("theme", "5");
            if (cedSessionManagement.getStoreId() != null) {
                jsonObject.addProperty("store_id", cedSessionManagement.getStoreId());
            }
            request_getallcat_data(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(getApplicationContext(), Ced_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    private void request_getallcat_data(JsonObject jsonObject) {
        if (cedConnectionDetector.isConnectingToInternet()) {

            mainViewModel.getAllCategories(Ced_MainActivity.this, jsonObject).observe(this, this::getCategories);
        } else {
            if (cedSessionManagement.get_sidedrawercategory_data() != null) {
                getcategories(cedSessionManagement.get_sidedrawercategory_data());
            } else {
                Intent intent = new Intent(Ced_MainActivity.this, Ced_NoInternetconnection.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            }

        }
    }

    private void getCategories(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                cedSessionManagement.save_sidedrawercategory_data(apiResponse.data);
                getcategories(apiResponse.data);
                break;

            case ERROR:
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }

    public void getcategories(String s) {
        try {
            JSONObject object = new JSONObject(s);
            if (object.has("header") && object.getString("header").equals("false")) {
                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                String status = object.getString("status");
                if (status.equals("success")) {
                    JSONObject data = object.getJSONObject("data");
                    if (data.has("categories")) {
                        JSONArray categories = data.getJSONArray("categories");
                        /*  JSONObject jsonObject = categories.getJSONObject(0);*/
                        cedSessionManagement.savecategories(categories.toString());
                    }
                    if (data.has("special_categories")) {
                        JSONArray special_categories = data.getJSONArray("special_categories");
                        cedSessionManagement.savespecial_categories(special_categories.toString());
                    }

                }
                show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getData(String loaddata) {
        String finaldata = "";
        byte[] data = Base64.decode(loaddata, Base64.DEFAULT);
        try {
            String text = new String(data, StandardCharsets.UTF_8);
            String[] excludingheaders = text.split(getResources().getString(R.string.header));
            byte[] lastjson = Base64.decode(excludingheaders[0], Base64.DEFAULT);
            finaldata = new String(lastjson, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(Ced_MainActivity.this, Ced_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
        return finaldata;
    }

}