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

package com.akshar.store.ui.addresssection.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import com.akshar.store.databinding.MagenativeCustomerAddressPageBinding;
import com.akshar.store.ui.addresssection.adapter.Addresslisting_recycler_Adapter;
import com.akshar.store.ui.addresssection.adapter.Ced_AddressAdapter;
import com.akshar.store.ui.addresssection.viewmodel.AddressViewModel;
import com.akshar.store.ui.loginsection.activity.Ced_Login;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;

import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.R;
import com.google.android.gms.analytics.Tracker;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Created by developer on 9/24/2015.
 */
@AndroidEntryPoint
public class Ced_Addressbook extends Ced_NavigationActivity {
    MagenativeCustomerAddressPageBinding addressPageBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    AddressViewModel addressViewModel;

    TextView add;
    ListView AddressList;
    public static final String KEY_ITEM = "data";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_CITY = "city";
    public static final String KEY_STATE = "region";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_STREET = "street";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_PINCODE = "pincode";
    public static final String KEY_ID = "address_id";
    public static final String KEY_STATUS = "status";

    public static final String KEY_default_shipping="default_shipping";
    public static final String KEY_default_billing="default_billing";
    JsonObject hashMap;
    JSONObject jsonObject = null;
    ArrayList<HashMap<String, String>> addressinfo;

    Ced_AddressAdapter Ced_AddressAdapter;
    String Email;
    private Tracker mTracker;
    RelativeLayout add_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addressViewModel = new ViewModelProvider(Ced_Addressbook.this, viewModelFactory).get(AddressViewModel.class);
        addressinfo = new ArrayList<>();
        hashMap = new JsonObject();
        final HashMap<String, String> user = session.getUserDetails();
        if (session.isLoggedIn()) {
            addressPageBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_customer_address_page, content, true);
            AddressList = addressPageBinding.MageNativeAddresslist;
            add_address = addressPageBinding.addAddress;
            add = addressPageBinding.MageNativeAddAddress;
            set_bold_font_fortext(add);
            Email = user.get(session.Key_Email);
            try {
                hashMap.addProperty("customer_id", session.getCustomerid());
                if (cedSessionManagement.getStoreId() != null)
                {
                    hashMap.addProperty("store_id", cedSessionManagement.getStoreId());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            addressViewModel.getAddressListData(Ced_Addressbook.this, hashMap,session.getHahkey()).observe(this, apiResponse -> {
                switch (apiResponse.status)
                {
                    case SUCCESS:
                            applydata(Objects.requireNonNull(apiResponse.data));
                        break;

                    case ERROR:
                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                        showmsg(getResources().getString(R.string.errorString));
                        break;
                }
            });

            add.setOnClickListener(v -> {
                Intent NewAddressIntent = new Intent(Ced_Addressbook.this, Ced_AddAddress.class);
                startActivity(NewAddressIntent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            });

            add_address.setOnClickListener(v -> {
                Intent NewAddressIntent = new Intent(Ced_Addressbook.this, Ced_AddAddress.class);
                startActivity(NewAddressIntent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            });

        } else {
            Intent goto_login = new Intent(Ced_Addressbook.this, Ced_Login.class);
            goto_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            goto_login.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            finish();
            startActivity(goto_login);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    /**
     * Get address detail of customer
     */
    private void applydata(String output) {
        try {
            jsonObject = new JSONObject(output);
            if (jsonObject.has("header") && jsonObject.getString("header").equals("false")) {
                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                if (jsonObject.getJSONObject(KEY_ITEM).getString(KEY_STATUS).equals("success")) {
                    JSONArray  userdetail_r_array = jsonObject.getJSONObject(KEY_ITEM).getJSONArray(KEY_ADDRESS);
                    Addresslisting_recycler_Adapter adapter = new Addresslisting_recycler_Adapter(Ced_Addressbook.this, userdetail_r_array.toString());
                    addressPageBinding.recycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                   /* userdetail = jsonObject.getJSONObject(KEY_ITEM).getJSONArray(KEY_ADDRESS);
                    for (int i = 0; i < userdetail.length(); i++) {
                        JSONObject c = null;
                        c = userdetail.getJSONObject(i);

                        String firstname, lastname, city, state, phone, country, street, pincode, id = null;
                        id = c.getString(KEY_ID);
                        firstname = c.getString(KEY_FIRSTNAME);
                        lastname = c.getString(KEY_LASTNAME);
                        city = c.getString(KEY_CITY);
                        if(c.has(KEY_STATE))
                        {
                            state = c.getString(KEY_STATE);
                        }
                        else
                        {
                            state = "";
                        }

                        phone = c.getString(KEY_PHONE);
                        country = c.getString(KEY_COUNTRY);
                        street = c.getString(KEY_STREET);
                        pincode = c.getString(KEY_PINCODE);
                        HashMap<String, String> userdata = new HashMap<>();
                        userdata.put(KEY_ID, id);
                        userdata.put(KEY_FIRSTNAME, firstname);
                        userdata.put(KEY_LASTNAME, lastname);
                        userdata.put(KEY_CITY, city);
                        userdata.put(KEY_STATE, state);
                        userdata.put(KEY_PHONE, phone);
                        userdata.put(KEY_COUNTRY, country);
                        userdata.put(KEY_STREET, street);
                        userdata.put(KEY_PINCODE, pincode);
                        if (c.has("prefix")) {
                            userdata.put("prefix", c.getString("prefix"));
                        } else {
                            userdata.put("prefix", " ");
                        }
                        if (c.has("suffix")) {
                            userdata.put("suffix", c.getString("suffix"));
                        } else {
                            userdata.put("suffix", " ");
                        }
                        if (c.has("middlename")) {
                            userdata.put("middlename", c.getString("middlename"));
                        } else {
                            userdata.put("middlename", " ");
                        }
                        if (c.has("taxvat")) {
                            userdata.put("taxvat", c.getString("taxvat"));
                        } else {
                            userdata.put("taxvat", " ");
                        }
                        addressinfo.add(userdata);
                    }
                    Ced_AddressAdapter = new Ced_AddressAdapter(this, addressinfo);
                    AddressList.setDivider(new ColorDrawable(getResources().getColor(R.color.white)));
                    AddressList.setDividerHeight(0);
                    AddressList.setAdapter(Ced_AddressAdapter);*/
                } else if (jsonObject.getJSONObject(KEY_ITEM).getString(KEY_STATUS).equals("no_address")) {
                    add_address.setVisibility(View.VISIBLE);
                    AddressList.setVisibility(View.GONE);
                    addressPageBinding.recycler.setVisibility(View.GONE);
                    add.setVisibility(View.INVISIBLE);
                   // showmsg(getResources().getString(R.string.AddAddressOutput));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
}