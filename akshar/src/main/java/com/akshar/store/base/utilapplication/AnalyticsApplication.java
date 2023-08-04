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
package com.akshar.store.base.utilapplication;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.FirebaseApp;
import com.akshar.store.R;
//import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

import java.util.ArrayList;
import java.util.HashMap;

import dagger.hilt.android.HiltAndroidApp;

/*@HiltAndroidApp*/
public class AnalyticsApplication extends Application {

    public static HashMap<String, ArrayList<String>> main_filters;
    public static HashMap<String, ArrayList<String>> seller_main_filters;
    public static String dealResponse = "";
    public static String categoryresponse = "";
    public static String productresponse = "";
    public static boolean loadeddeal = false;
    public static boolean loadedproducts = false;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        main_filters = new HashMap<>();
        seller_main_filters = new HashMap<>();
        MultiDex.install(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
