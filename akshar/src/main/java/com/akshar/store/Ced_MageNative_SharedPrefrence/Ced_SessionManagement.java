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
import android.content.SharedPreferences;

import java.util.Set;

public class Ced_SessionManagement {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int Private_Mode = 0;
    Context con;
    private static final String PREF_NAME = "data";
    public static final String Key_Email = "Email";
    public static final String Key_Name = "Name";
    public static final String KEY_CART_ID = "CARTID";
    public static final String Key_Is_WebCheckout = "iswebcheckoutenabled";
    public static final String Key_Currency = "currency";

    public static final String KEY_Language = "language";
    public static final String KEY_StoreLocale = "storelocale";
    public static final String KEY_STORE_ID = "storeid";
    public static final String KEY_STORED_HOMEPAGEDYNAMIC_DATA = "homepagedata";
    public static final String KEY_STORED_SIDEDRAWER_CAT_DATA = "sidedrawer_cat_data";
    public static final String KEY_STORED_GETCARTCOUNT_DATA = "getcartcountdata";


    public static Ced_SessionManagement cedSessionManagement;

    public static Ced_SessionManagement getCed_sessionManagement(Context context) {
        if (cedSessionManagement == null) {
            cedSessionManagement = new Ced_SessionManagement(context);
        }
        return cedSessionManagement;
    }

    public Ced_SessionManagement(Context context) {
        this.con = context;
        pref = con.getSharedPreferences(PREF_NAME, Private_Mode);
        editor = pref.edit();
    }

    public void savecartid(String cart_id) {
        if (!cart_id.equals("0")) {
            editor.putString(KEY_CART_ID, cart_id);
            editor.commit();
        }
    }


    public void illustration(String done) {
        editor.putString("illustration", done);
        editor.commit();
    }

    public void iswebcheckoutenabled(boolean enable) {
        editor.putBoolean(Key_Is_WebCheckout, enable);
        editor.commit();
    }

    public Boolean iswebcheckoutenabled() {
        return pref.getBoolean(Key_Is_WebCheckout, false);
    }

    public String getcategorytheme() {
        return pref.getString("theme", "1");
    }

    public String getdrawertheme() {
        return pref.getString("drawertheme", "white");
    }

    public String getillustration() {
        return pref.getString("illustration", "new");
    }

    public boolean clearcartId() {
        return editor.remove(KEY_CART_ID).commit();
    }

    public String getCartId() {
        return pref.getString(KEY_CART_ID, null);
    }


    public String getvalidity() {
        return pref.getString("valid", null);
    }

    public void savevalidity(String valid) {
        editor.putString("valid", valid);
        editor.commit();
    }

    public void savecategories(String cat) {
        editor.putString("subcategories", cat);
        editor.commit();
    }

    public String getCategories() {
        return pref.getString("subcategories", null);
    }


    public void savespecial_categories(String cat) {
        editor.putString("special_categories", cat);
        editor.commit();
    }

    public String getspecial_categories() {
        return pref.getString("special_categories", null);
    }

    public void setcurrency(String currency) {
        editor.putString(Key_Currency, currency);
        editor.commit();
    }

    public String getcurrency() {
        return pref.getString(Key_Currency, null);
    }

    public void saveLanguageToLoad(String lang) {
        editor.putString(KEY_Language, lang);
        editor.commit();
    }

    public String getLanguageToLoad() {
        return pref.getString(KEY_Language, "");
    }

    public void saveStoreId(String store_id) {
        editor.putString(KEY_STORE_ID, store_id);
        editor.commit();
    }

    public String getStoreId() {
        return pref.getString(KEY_STORE_ID, null);
    }

    public void save_homedata(String data) {
        editor.putString(KEY_STORED_HOMEPAGEDYNAMIC_DATA, data);
        editor.commit();
    }

    public String get_homedata() {
        return pref.getString(KEY_STORED_HOMEPAGEDYNAMIC_DATA, null);
    }

    public void save_getcartcountdata(String data) {
        editor.putString(KEY_STORED_GETCARTCOUNT_DATA, data);
        editor.commit();
    }

    public String get_getcartcountdata() {
        return pref.getString(KEY_STORED_GETCARTCOUNT_DATA, null);
    }

    public void save_sidedrawercategory_data(String data) {
        editor.putString(KEY_STORED_SIDEDRAWER_CAT_DATA, data);
        editor.commit();
    }

    public String get_sidedrawercategory_data() {
        return pref.getString(KEY_STORED_SIDEDRAWER_CAT_DATA, null);
    }

    public void saveStorelocale(String locale) {
        editor.putString(KEY_StoreLocale, locale);
        editor.commit();
    }

    public String getStoreLocale() {
        return pref.getString(KEY_StoreLocale, null);
    }


    public void saveSubSellerIdFromCart(String sub_seller_id) {
        editor.putString("sub_seller_id_from_cart", sub_seller_id);
        editor.commit();
    }

    public String getSubSellerIdFromCart() {
        return pref.getString("sub_seller_id_from_cart", "");
    }

    public void clearSubSellerIdFromCart() {
        editor.remove("sub_seller_id_from_cart").commit();
    }
}
