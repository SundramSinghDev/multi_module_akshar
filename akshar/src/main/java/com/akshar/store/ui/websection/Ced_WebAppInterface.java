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
package com.akshar.store.ui.websection;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.akshar.store.Ced_MageNative_SharedPrefrence.Ced_SessionManagement;
import com.akshar.store.Ced_MageNative_SharedPrefrence.Ced_SessionManagement_login;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.akshar.store.ui.orderssection.activity.Ced_Orderview;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.ui.homesection.activity.Ced_New_home_page;
import com.google.gson.JsonObject;

public class Ced_WebAppInterface {
    private Context mContext;
    private Ced_SessionManagement session;
    private Ced_SessionManagement_login session_login;

    public Ced_WebAppInterface(Context c) {
        mContext = c;
        session = Ced_SessionManagement.getCed_sessionManagement(mContext);
        session_login = new Ced_SessionManagement_login(mContext);
    }

    @JavascriptInterface
    public void ContinueShopping(String status) {
        if (status.equals("true")) {
            session.clearcartId();
            if (session.getSubSellerIdFromCart().length() > 0) {
                session_login.saveSubSellerId(session.getSubSellerIdFromCart());
                session.clearSubSellerIdFromCart();
            }
            Ced_MainActivity.latestcartcount = "0";
            Intent intent = new Intent(mContext, Magenative_HomePageNewTheme.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            Log.i("ContinueShopping", "IN");
        }
    }

 /*   @JavascriptInterface
    public void orderSuccessEvent(String status) {
        if (status.equals("true")) {
            session.clearcartId();
            Ced_MainActivity.latestcartcount = "0";
            Log.i("Ordersuccess", "IN");

        }
    }*/

    @JavascriptInterface
    public void orderViewEvent(String orderid) {
        Log.i("OrderView", "IN");
        session.clearcartId();
        Ced_MainActivity.latestcartcount = "0";
        Intent intent = new Intent(mContext, Ced_Orderview.class);
        intent.putExtra("orderid", orderid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

    }
}