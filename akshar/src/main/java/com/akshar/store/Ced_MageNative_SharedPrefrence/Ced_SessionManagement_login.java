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

package com.akshar.store.Ced_MageNative_SharedPrefrence;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.akshar.store.ui.loginsection.activity.Ced_Login;

import java.util.HashMap;

/**
 * Created by developer on 9/21/2015.
 */
public class Ced_SessionManagement_login {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int Private_Mode = 0;
    Context con;
    String counter;
    String customer_id;
    String Checkout_GuestEmail;
    String Checkout_GuestMobile;
    String Checkout_CustomerMobile;
    String language;
    String storeid;
    Boolean instance = false;
    private static final String PREF_NAME = "Login_Pref";
    private static final String Is_Login = "IS_Logged_In";
    public static final String Key_Email = "Email";
    public static final String Key_Name = "Password";
    public static final String Key_Checkout_CustomerMobile = "Checkout_CustomerMobile";
    public static final String Key_Checkout_GuestMobile = "Checkout_GuestMobile";
    public static final String Key_Checkout_GuestEmail = "Checkout_GuestEmail";
    public static final String KEY_SESSIONID = "Sessionid";
    public static final String KEY_CUSTOMER_ID = "CustomerId";
    public static final String KEY_INSTANCE = "instance";
    public static final String KEY_CUSTOMERGROUP_ID = "customergroup_id";
    public static final String KEY_gender = "gender";
    public static final String KEY_DeviceId = "device_id";
    public static final String KEY_iffreshinstall = "iffreshinstall";
    public static final String KEY_DeviceToken = "device_token";
    public static final String KEY_name = "name";

    public Ced_SessionManagement_login(Context context) {
        this.con = context;
        pref = con.getSharedPreferences(PREF_NAME, Private_Mode);
        editor = pref.edit();
        editor.apply();
    }


    public static Ced_SessionManagement_login session;

    public static Ced_SessionManagement_login getShredPrefs(Context context) {
        if (session == null) {
            session = new Ced_SessionManagement_login(context);
        }
        return session;
    }

    public void createLoginSession(String name, String email) {
        // Storing login value as TRUE
        editor.putBoolean(Is_Login, true);
        // Storing name in pref
        editor.putString(Key_Name, name);
        // Storing email in pref
        editor.putString(Key_Email, email);
        // commit changes
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(Key_Name, pref.getString(Key_Name, "213"));
        user.put(Key_Email, pref.getString(Key_Email, "213"));
        return user;
    }


    public boolean isLoggedIn() {
        return pref.getBoolean(Is_Login, false);
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.remove(Is_Login).commit();
        editor.remove(Key_Name).commit();
        editor.remove(Key_Email).commit();
        editor.remove(KEY_CUSTOMERGROUP_ID).commit();
        // editor.clear();
        // editor.commit();
        // After logout redirect user to Loing Activity

    }

    public void saveSessionID(String id) {
        counter = id;
        editor.putString(KEY_SESSIONID, id);
        editor.commit();

    }

    public String getSessionID() {
        return pref.getString(KEY_SESSIONID, counter);

    }

    public void clearSessionID() {
        pref.edit().remove(KEY_SESSIONID);
    }

    public void saveCustomerId(String cst_id) {
        editor.putString(KEY_CUSTOMER_ID, cst_id);
        editor.commit();
    }

    public void saveCheckout_GuestEmail(String Checkout_GuestEmail) {
        editor.putString(Key_Checkout_GuestEmail, Checkout_GuestEmail);
        editor.commit();
    }

    public String getCheckout_GuestEmail() {
        return pref.getString(Key_Checkout_GuestEmail, Checkout_GuestEmail);

    }

    public void saveCheckout_GuestMobile(String Checkout_GuestMobile) {
        editor.putString(Key_Checkout_GuestMobile, Checkout_GuestMobile);
        editor.commit();
    }

    public String getCheckout_GuestMobile() {
        return pref.getString(Key_Checkout_GuestMobile, Checkout_GuestMobile);
    }

    public void saveCheckout_CustomerMobile(String Checkout_CustomerMobile) {
        editor.putString(Key_Checkout_CustomerMobile, Checkout_CustomerMobile);
        editor.commit();
    }

    public String getCheckout_CustomerMobile() {
        return pref.getString(Key_Checkout_CustomerMobile, Checkout_CustomerMobile);

    }

    public String getCustomerid() {
        return pref.getString(KEY_CUSTOMER_ID, customer_id);

    }

    public void ClearCustomerId() {
        editor.remove(KEY_CUSTOMER_ID).commit();
        // editor.commit();
    }

    public String getHahkey() {

        return pref.getString(Key_Name, "213");
    }

    public void saveUrlRun(Boolean instance) {
        editor.putBoolean(KEY_INSTANCE, instance);
        editor.commit();
    }

    public Boolean getUrlRun() {
        return pref.getBoolean(KEY_INSTANCE, instance);
    }

    public void set_customergroup_id(String cust_groupid) {
        editor.putString(KEY_CUSTOMERGROUP_ID, cust_groupid);
        editor.commit();
    }

    public String get_customergroup_id() {
        return pref.getString(KEY_CUSTOMERGROUP_ID, null);
    }

    public void savegender(String gender) {
        editor.putString(KEY_gender, gender);
        editor.commit();
    }

    public void savename(String name) {
        editor.putString(KEY_name, name);
        editor.commit();
    }

    public String getname() {
        return pref.getString(KEY_name, "guest");
    }

    public void saveSubSellerId(String sub_seller_id) {
        editor.putString("sub_seller_id", sub_seller_id);
        editor.commit();
    }

    public String getSubSellerId() {
        return pref.getString("sub_seller_id", "");
    }

    public void clearSellerId(){
        editor.remove("sub_seller_id").commit();
    }

    public String getgender() {
        return pref.getString(KEY_gender, "guest");
    }

    public void clear_data() {
        editor.remove(KEY_name).commit();
        editor.remove(KEY_gender).commit();
        /*editor.remove("name");
        editor.remove("gender");
        editor.commit();*/
    }

    public void clear_checkouttemporarydata() {
        editor.remove(Key_Checkout_CustomerMobile).commit();
        editor.remove(Key_Checkout_GuestMobile).commit();
        editor.remove(Key_Checkout_GuestEmail).commit();
    }

    public void savetoken(String token) {
        editor.putString(KEY_DeviceToken, token);
        editor.commit();
    }

    public String gettoken() {
        return pref.getString(KEY_DeviceToken, null);

    }

    public void savedeviceid(String deviceid) {
        editor.putString(KEY_DeviceId, deviceid);
        editor.commit();
    }

    public String getdeviceid() {
        return pref.getString(KEY_DeviceId, null);

    }

    public void save_iffreshinstall_ondevice(Boolean value) {
        editor.putBoolean(KEY_iffreshinstall, value);
        editor.commit();
    }

    public boolean get_iffreshinstall_ondevice() {
        return pref.getBoolean(KEY_iffreshinstall, instance);

    }

    public void clear_iffreshinstall_ondevice() {
        editor.remove(KEY_iffreshinstall).commit();
    }

    public void saveupdateavailablepopup(String value) {
        editor.putString("home_updatepopup", value);
        editor.commit();
    }

    public String getupdateavailablepopup() {
        return pref.getString("home_updatepopup", null);
    }

    public void saveUserMobile(String mobile) {
        editor.putString("mobile", mobile);
        editor.commit();
    }

    public String getUserMobile() {
        return pref.getString("mobile", "");
    }

}

