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
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonObject;
import com.akshar.store.ui.checkoutsection.activity.BillingShippingAddress;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import com.akshar.store.databinding.MagenativeAddCustomerAddressPageBinding;

import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.R;
import com.google.android.gms.analytics.Tracker;
import com.akshar.store.ui.addresssection.viewmodel.AddressViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Created by developer on 9/24/2015.
 */
@AndroidEntryPoint
public class Ced_AddAddress extends Ced_NavigationActivity {
    MagenativeAddCustomerAddressPageBinding addressPageBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    AddressViewModel addressViewModel;
    JSONObject jsonObj = null;
    JSONArray response = null;
    EditText edt_mail;
    EditText edt_firstname;
    EditText edt_lastname;
    EditText edt_street;
    EditText edt_city;
    EditText edt_state, state_dropdown;
    Boolean nostates = false;
    EditText edt_pincode;
    EditText edt_country;
    EditText edt_mobile;
    JsonObject hashMap;
    AppCompatButton edt_savedetail;
    TextView txt_add_button;
    String Output_string, status, address_id, message;
    String firstname, lastname, Street, City, State, Pincode, Country, Mobile;
    List<String> countrylabellist;
    List<String> statelabellist;
    List<String> countrycodelist;
    List<String> statecodelist;
    String country_code, state_code = "";
    String country_label, state_label = "";
    private Tracker mTracker;
    boolean prefixflag = false;
    boolean suffixflag = false;
    boolean dobflag = false;
    boolean taxvatflag = false;
    boolean middlenamevatflag = false;
    TextView prefixname;
    TextView suffixname;
    TextView prefixoptions;
    TextView suffixoptions;
    /*
     TextView dobname;*/
    String prefixvalue = "";
    String suffixvalue = "";
    DatePicker dob;
    boolean flag = true;
    EditText MageNative_midllename;
    EditText MageNative_taxvat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addressViewModel = new ViewModelProvider(Ced_AddAddress.this, viewModelFactory).get(AddressViewModel.class);
        addressPageBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_add_customer_address_page, content, true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        hashMap = new JsonObject();
        edt_mail = addressPageBinding.MageNativeEmail;
        edt_mail.setText(session.getUserDetails().get(session.Key_Email));
        edt_mail.setKeyListener(null);
        edt_firstname = addressPageBinding.MageNativeUserFirstname;
        edt_lastname = addressPageBinding.MageNativeUserLastName;
        edt_street = addressPageBinding.MageNativeStreet;
        edt_city = addressPageBinding.MageNativeCity;
        edt_state = addressPageBinding.MageNativeState;
        state_dropdown = addressPageBinding.MageNativeStateDropdown;
        edt_pincode = addressPageBinding.MageNativePincode;
        prefixname = addressPageBinding.prefixname;
        suffixname = addressPageBinding.suffixname;
        prefixoptions = addressPageBinding.prefixoptions;
        suffixoptions = addressPageBinding.suffixoptions;
        MageNative_midllename = addressPageBinding.MageNativeMidllename;
        MageNative_taxvat = addressPageBinding.MageNativeTaxvat;
        edt_mobile = addressPageBinding.MageNativePhone;
        edt_savedetail = addressPageBinding.MageNativeSaveAddress;
        txt_add_button = addressPageBinding.MageNativeTxtAddButton;
        edt_country = addressPageBinding.MageNativeCountry;
        getcountries();

        addressPageBinding.dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker_DDMMYYYY(addressPageBinding.dob);
            }
        });
        state_dropdown.setOnClickListener(v -> {
            if (statecodelist.size() > 0 && statelabellist.size() > 0) {
                nostates = false;
                state_dropdown.setEnabled(false);
                final CharSequence[] arrayOfInt = statecodelist.toArray(new CharSequence[statecodelist.size()]);
                final CharSequence[] arrayOfInt2 = statelabellist.toArray(new CharSequence[statelabellist.size()]);

               /* Dialog levelDialog1 = new Dialog(this);
                final AlertDialog.Builder builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
                builder.setTitle(Html.fromHtml("<font color='#000000'>" + getResources().getString(R.string.selectstate) + "</font>"));

                builder.setSingleChoiceItems(arrayOfInt2, -1, (dialog, postion) -> {
                            state_code = (String) arrayOfInt[postion];
                            state_label = (String) arrayOfInt2[postion];
                            state_dropdown.setText(state_label);
                            dialog.dismiss();
                            State = "";
                        }
                );
                levelDialog1 = builder.create();
                levelDialog1.show();
                  );*/
                new MaterialAlertDialogBuilder(this, R.style.SingleChoiceRadioStyle)
                        .setTitle(Html.fromHtml("<font color='#000000'>" + getResources().getString(R.string.selectstate) + "</font>"))
                        .setSingleChoiceItems(arrayOfInt2, -1, (dialog, postion) -> {
                            state_code = (String) arrayOfInt[postion];
                            state_label = (String) arrayOfInt2[postion];
                            state_dropdown.setText(state_label);
                            dialog.dismiss();
                            State = "";
                        })
                        .show();

            } else {
                state_dropdown.setEnabled(true);
                nostates = true;

            }
        });

        edt_country.setOnClickListener(v -> {
            final CharSequence[] arrayOfInt = countrycodelist.toArray(new CharSequence[countrycodelist.size()]);
            final CharSequence[] arrayOfInt2 = countrylabellist.toArray(new CharSequence[countrylabellist.size()]);

           /* Dialog levelDialog1 = new Dialog(Ced_AddAddress.this);
            final AlertDialog.Builder builder = new AlertDialog.Builder(Ced_AddAddress.this,AlertDialog.THEME_HOLO_LIGHT);
            builder.setTitle(Html.fromHtml("<font color='#000000'>" + getResources().getString(R.string.selectcountry) + "</font>"));
            builder.setSingleChoiceItems(arrayOfInt2, -1, (dialog, postion) -> {
                        country_code = (String) arrayOfInt[postion];
                        country_label = (String) arrayOfInt2[postion];
                        edt_country.setText(country_label);
                        dialog.dismiss();
                        if(country_code !=null)
                        {
                            getState(country_code);
                        }
                    }
            );
            levelDialog1 = builder.create();
            levelDialog1.show();*/
            new MaterialAlertDialogBuilder(Ced_AddAddress.this, R.style.SingleChoiceRadioStyle)
                    .setTitle(Html.fromHtml("<font color='#000000'>" + getResources().getString(R.string.selectcountry) + "</font>"))
                    .setSingleChoiceItems(arrayOfInt2, -1, (dialog, postion) -> {
                        country_code = (String) arrayOfInt[postion];
                        country_label = (String) arrayOfInt2[postion];
                        edt_country.setText(country_label);
                        dialog.dismiss();
                        if (country_code != null) {
                            getState(country_code);
                        }

                    })
                    .show();

        });
        countrylabellist = new ArrayList<>();
        statelabellist = new ArrayList<>();
        countrycodelist = new ArrayList<>();
        statecodelist = new ArrayList<>();

        edt_savedetail.setOnClickListener(v -> {
            firstname = edt_firstname.getText().toString();
            lastname = edt_lastname.getText().toString();
            Street = edt_street.getText().toString();
            City = edt_city.getText().toString();
            State = edt_state.getText().toString();
            Pincode = edt_pincode.getText().toString();
            Country = edt_country.getText().toString();
            Mobile = edt_mobile.getText().toString();

            if (firstname.length() == 0) {
                edt_firstname.setError(getResources().getString(R.string.empty));
            } else if (lastname.length() == 0) {
                edt_lastname.setError(getResources().getString(R.string.empty));
            } else if (Country.length() == 0) {
                edt_country.setError(getResources().getString(R.string.empty));
            } else if (State.length() == 0 && state_dropdown.getText().toString().length() == 0) {
                edt_state.setError(getResources().getString(R.string.empty));
                state_dropdown.setError(getResources().getString(R.string.empty));
            } else if (City.length() == 0) {
                edt_city.setError(getResources().getString(R.string.empty));
            } else if (Street.length() == 0) {
                edt_street.setError(getResources().getString(R.string.empty));
            } else if (Pincode.length() == 0) {
                edt_pincode.setError(getResources().getString(R.string.empty));
            } else if (Mobile.length() != 10) {
                edt_mobile.setError(getResources().getString(R.string.invalidphone));
            } else {
                try {
                    if (prefixflag) {
                        if (prefixvalue.isEmpty()) {
                            flag = false;
                            showmsg(getResources().getString(R.string.selectsomeprefixvalue));

                        } else {
                            flag = true;
                            hashMap.addProperty(prefixname.getText().toString(), prefixvalue);
                        }
                    }
                    if (middlenamevatflag) {
                        if (flag) {
                            if (MageNative_midllename.getText().toString().isEmpty()) {
                                flag = false;
                                MageNative_midllename.setError(getResources().getString(R.string.empty));
                                MageNative_midllename.requestFocus();
                            } else {
                                flag = true;
                                hashMap.addProperty(addressPageBinding.MageNativeMidllename.getTag().toString(), MageNative_midllename.getText().toString());
                            }
                        }
                    }
                    if (suffixflag) {
                        if (flag) {
                            if (suffixvalue.isEmpty()) {
                                flag = false;
                                showmsg(getResources().getString(R.string.selectsomesuffixvalue));
                            } else {
                                flag = true;
                                hashMap.addProperty(suffixname.getText().toString(), suffixvalue);
                            }
                        }
                    }
                  /*  if (dobflag) {
                        if (addressPageBinding.dob.getText().toString().isEmpty())
                        {
                            flag = false;
                            addressPageBinding.dob.setError(getResources().getString(R.string.selectdob));
                            addressPageBinding.dob.requestFocus();
                        }
                        else
                        {
                            addressPageBinding.dob.setError(null);
                            flag = true;
                            String[] parts = addressPageBinding.dob.getText().toString().split("/");
                            String year = String.valueOf(Integer.parseInt(parts[2]));
                            String month = String.valueOf(Integer.parseInt(parts[1]));
                            String day = String.valueOf(Integer.parseInt(parts[0]));
                            if (month.length() < 2) {
                                month = "0" + month;
                            }
                            if (day.length() < 2) {
                                day = "0" + day;
                            }
                            hashMap.addProperty(addressPageBinding.dob.getTag().toString(), month + "/" + day + "/" + year);
                        }

                    }*/
                    if (taxvatflag) {
                        if (flag) {
                            if (MageNative_taxvat.getText().toString().isEmpty()) {
                                flag = false;
                                MageNative_taxvat.setError(getResources().getString(R.string.empty));
                                MageNative_taxvat.requestFocus();
                            } else {
                                MageNative_taxvat.setError(null);
                                flag = true;
                                hashMap.addProperty(addressPageBinding.MageNativeTaxvat.getTag().toString(), MageNative_taxvat.getText().toString());
                            }
                        }
                    }
                    hashMap.addProperty("email", session.getUserDetails().get(session.Key_Email));
                    hashMap.addProperty("firstname", firstname);
                    hashMap.addProperty("lastname", lastname);
                    hashMap.addProperty("street", Street);
                    hashMap.addProperty("city", City);
                    if (nostates) {
                        if (edt_state.getText().toString().isEmpty()) {
                            hashMap.addProperty("region", state_dropdown.getText().toString());
                        } else {
                            hashMap.addProperty("region", State);
                        }
                    } else {
                        hashMap.addProperty("region_id", state_code);
                    }
                    hashMap.addProperty("postcode", Pincode);
                    hashMap.addProperty("country_id", country_code);
                    hashMap.addProperty("telephone", Mobile);
                    hashMap.addProperty("customer_id", session.getCustomerid());
                    if (addressPageBinding.useforshipping.isChecked()) {
                        hashMap.addProperty("default_shipping", true);
                    }
                    if (addressPageBinding.useforbilling.isChecked()) {
                        hashMap.addProperty("default_billing", true);
                    }
                    if (cedSessionManagement.getStoreId() != null) {
                        hashMap.addProperty("store_id", cedSessionManagement.getStoreId());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (flag) {
                    addressViewModel.saveAddressData(Ced_AddAddress.this, hashMap, session.getHahkey()).observe(this, apiResponse -> {
                        switch (apiResponse.status) {
                            case SUCCESS:
                                Output_string = apiResponse.data;
                                addAddress();

                                break;

                            case ERROR:
                                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                                showmsg(getResources().getString(R.string.errorString));
                                break;
                        }
                    });
                }
            }
        });

        get_the_fields();
    }

    private void get_the_fields() {
        addressViewModel.getRequiredFields(Ced_AddAddress.this).observe(this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(Objects.requireNonNull(apiResponse.data));
                        String success = object.getString("success");
                        if (success.equals("true")) {
                            JSONArray data = object.getJSONArray("data");
                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject jsonObject = data.getJSONObject(i);
                                    if (jsonObject.has("prefix")) {
                                        if (jsonObject.getString("prefix").equals("true")) {
                                            prefixflag = true;
                                            addressPageBinding.prefixsection.setVisibility(View.VISIBLE);
                                            addressPageBinding.prefixlabel.setText(jsonObject.getString("label"));
                                            prefixname.setText(jsonObject.getString("name"));
                                            RadioGroup ll = new RadioGroup(Ced_AddAddress.this);
                                            ll.setOrientation(LinearLayout.VERTICAL);
                                            JSONObject prefix_options = jsonObject.getJSONObject("prefix_options");
                                            prefixoptions.setText(prefix_options.toString());
                                            if (Objects.requireNonNull(prefix_options.names()).length() > 0) {
                                                for (int j = 0; j < Objects.requireNonNull(prefix_options.names()).length(); j++) {
                                                    final RadioButton rdbtn = new RadioButton(Ced_AddAddress.this);
                                                    rdbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                                        if (isChecked) {
                                                            try {
                                                                JSONObject object1 = new JSONObject(prefixoptions.getText().toString());
                                                                prefixvalue = object1.getString(rdbtn.getText().toString());
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });
                                                    rdbtn.setText((CharSequence) Objects.requireNonNull(prefix_options.names()).get(j));
                                                    ll.addView(rdbtn);
                                                }
                                            }
                                            addressPageBinding.prefix.addView(ll);
                                        }
                                    }
                                    if (jsonObject.has("taxvat")) {
                                        if (jsonObject.getString("taxvat").equals("true")) {
                                            taxvatflag = true;
                                            addressPageBinding.taxvatsection.setVisibility(View.VISIBLE);
                                            addressPageBinding.MageNativeTaxvat.setHint(jsonObject.getString("label") + "*");
                                            addressPageBinding.MageNativeTaxvat.setTag(jsonObject.getString("name"));
                                        }
                                    }
                                    if (jsonObject.has("middlename")) {
                                        if (jsonObject.getString("middlename").equals("true")) {
                                            middlenamevatflag = true;
                                            addressPageBinding.middlenamesection.setVisibility(View.VISIBLE);
                                            addressPageBinding.MageNativeMidllename.setHint(jsonObject.getString("label") + "*");
                                            addressPageBinding.MageNativeMidllename.setTag(jsonObject.getString("name"));
                                        }
                                    }
                                  /*  if (jsonObject.has("dob")) {
                                        if (jsonObject.getString("dob").equals("true"))
                                        {
                                            dobflag = true;
                                            addressPageBinding.dobsection.setVisibility(View.VISIBLE);
                                            addressPageBinding.dob.setHint(jsonObject.getString("label")+"*");
                                            addressPageBinding.dob.setTag(jsonObject.getString("name"));
                                        }
                                    }*/
                                    if (jsonObject.has("suffix")) {
                                        if (jsonObject.getString("suffix").equals("true")) {
                                            suffixflag = true;
                                            addressPageBinding.suffixsection.setVisibility(View.VISIBLE);
                                            addressPageBinding.suffixlabel.setText(jsonObject.getString("label"));
                                            suffixname.setText(jsonObject.getString("name"));
                                            RadioGroup ll = new RadioGroup(Ced_AddAddress.this);
                                            ll.setOrientation(LinearLayout.VERTICAL);
                                            JSONObject suffixx_options = jsonObject.getJSONObject("suffix_options");
                                            suffixoptions.setText(suffixx_options.toString());
                                            if (Objects.requireNonNull(suffixx_options.names()).length() > 0) {
                                                for (int j = 0; j < Objects.requireNonNull(suffixx_options.names()).length(); j++) {
                                                    final RadioButton rdbtn = new RadioButton(Ced_AddAddress.this);
                                                    rdbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                                        if (isChecked) {
                                                            try {
                                                                JSONObject object1 = new JSONObject(suffixoptions.getText().toString());
                                                                suffixvalue = object1.getString(rdbtn.getText().toString());
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });
                                                    rdbtn.setText((CharSequence) Objects.requireNonNull(suffixx_options.names()).get(j));
                                                    ll.addView(rdbtn);
                                                }
                                            }
                                            addressPageBinding.suffix.addView(ll);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case ERROR:
                    Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                    showmsg(getResources().getString(R.string.errorString));
                    break;
            }
        });
    }

    private void getState(String country_code) {
        addressViewModel.getStatesData(Ced_AddAddress.this, country_code).observe(this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:

                    try {
                        JSONObject object = new JSONObject(Objects.requireNonNull(apiResponse.data));
                        Boolean status = object.getBoolean("success");
                        if (status.equals(true)) {
                            addressPageBinding.stateDropdownLayout.setVisibility(View.VISIBLE);
                            addressPageBinding.stateLayout.setVisibility(View.GONE);
                            edt_state.setText("");
                            if (object.has("states") && object.getJSONArray("states").length() > 0) {

                                state_dropdown.requestFocus();
                                edt_state.setVisibility(View.GONE);
                                state_dropdown.setVisibility(View.VISIBLE);
                                JSONArray jsonArray = object.getJSONArray("states");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    statecodelist.add(c.getString("region_id"));
                                    statelabellist.add(c.getString("name"));
                                }
                            } else {
                                state_dropdown.setText("");
                                edt_state.setText("");
                                state_dropdown.setVisibility(View.GONE);
                                edt_state.setVisibility(View.VISIBLE);
                                edt_state.requestFocus();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case ERROR:
                    Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                    showmsg(getResources().getString(R.string.errorString));
                    break;
            }
        });
    }

    private void addAddress() {
        try {
            jsonObj = new JSONObject(Output_string);
            if (jsonObj.has("header") && jsonObj.getString("header").equals("false")) {
                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                response = jsonObj.getJSONObject("data").getJSONArray("customer");
                for (int i = 0; i < response.length(); i++) {
                    JSONObject c = null;
                    c = response.getJSONObject(i);
                    status = c.getString("status");
                    if (status.equals("error")) {
                        message = c.getString("message");
                        showmsg(String.valueOf(Html.fromHtml(message)));
                    } else if (status.equals("success")) {
                        address_id = c.getString("address_id");
                    }
                }
                if (status.equals("success")) {
//                    Intent addresslistingintent;
                    if (getIntent().hasExtra("fromcheckout") && getIntent().getBooleanExtra("fromcheckout", false)) {
//                        addresslistingintent = new Intent(getApplicationContext(), BillingShippingAddress.class);
                       //for custom order summary page
                        Intent address = new Intent();
                        address.putExtra("MESSAGE", getResources().getString(R.string.saved));
                        setResult(FROM_CUSTOM_ORDER_SUMMARY,address);
                        finish();
                    } else {
                        Intent addresslistingintent = new Intent(getApplicationContext(), Ced_Addressbook.class);
                        addresslistingintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(addresslistingintent);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        showmsg(getResources().getString(R.string.saved));
                    }
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

    /**
     * Get countries
     */
    private void getcountries() {
        try {
            addressViewModel.getCountriesData(Ced_AddAddress.this).observe(this, apiResponse -> {
                switch (apiResponse.status) {
                    case SUCCESS:
                        try {
                            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(apiResponse.data));
                            JSONArray jsonArray = jsonObject.getJSONArray("country");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                countrycodelist.add(object.getString("value"));
                                countrylabellist.add(object.getString("label"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;

                    case ERROR:
                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                        showmsg(getResources().getString(R.string.errorString));
                        break;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}