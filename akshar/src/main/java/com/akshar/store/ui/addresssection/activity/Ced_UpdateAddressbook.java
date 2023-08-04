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


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.AppCompatButton;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import com.akshar.store.databinding.MagenativeAddCustomerAddressPageBinding;

import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.R;
import com.akshar.store.ui.addresssection.viewmodel.AddressViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ced_UpdateAddressbook extends Ced_NavigationActivity {
    MagenativeAddCustomerAddressPageBinding addressPageBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    AddressViewModel addressViewModel;
    EditText edt_mail;
    EditText edt_firstname;
    EditText edt_lastname;
    EditText edt_street;
    EditText edt_city;
    TextInputLayout state_layout, state_dropdown_layout;
    EditText edt_state, state_dropdown;
    Boolean nostates=false;
    EditText edt_pincode;
    EditText edt_country;
    EditText edt_mobile;
    AppCompatButton edt_savedetail;
    TextView txt_id;
    JSONObject jsonObj = null;
    static final String KEY_CUSTOMER = "customer";
    static final String KEY_STATUS = "status";
    JSONArray response = null;
    static final String KEY_OBJECT = "data";
    String Email, firstname, lastname, Street, City, State, Pincode, Country, Mobile, id;
    HashMap country_labelcode_list,state_labelcode_list;
    List<String> countrylabellist;
    List<String> statelabellist;
    List<String> countrycodelist;
    List<String> statecodelist;
    String country_code, state_code = "";
    String country_label, state_label = "";
    LinearLayout prefixsection;
    LinearLayout suffixsection;
    TextView prefixlabel;
    TextView suffixlabel;
    RadioGroup prefix;
    RadioGroup suffix;
    boolean prefixflag = false;
    boolean suffixflag = false;
    boolean dobflag = false;
    boolean taxvatflag = false;
    boolean middlenamevatflag = false;
    TextView prefixname;
    TextView suffixname;
    TextView prefixoptions;
    TextView suffixoptions;
    String prefixvalue = "";
    String suffixvalue = "";
    DatePicker dob;
    boolean flag = true;
    EditText MageNative_midllename;
    EditText MageNative_taxvat;
    String[] array;
    CheckBox useforbilling,useforshipping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addressViewModel = new ViewModelProvider(Ced_UpdateAddressbook.this, viewModelFactory).get(AddressViewModel.class);
        addressPageBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_add_customer_address_page, content, true);
        country_labelcode_list=new HashMap();
        state_labelcode_list=new HashMap();

        countrylabellist = new ArrayList<>();
        statelabellist = new ArrayList<>();
        countrycodelist = new ArrayList<>();
        statecodelist = new ArrayList<>();

        edt_mail = addressPageBinding.MageNativeEmail;
        final HashMap<String, String> user = session.getUserDetails();
        Email = user.get(session.Key_Email);
        edt_mail.setText(Email);
        edt_mail.setEnabled(false);
        Intent update = getIntent();
        array = update.getStringArrayExtra("update");

        useforbilling=addressPageBinding.useforbilling;
        useforshipping=addressPageBinding.useforshipping;
        txt_id = addressPageBinding.MageNativeAddressid;
        edt_firstname = addressPageBinding.MageNativeUserFirstname;
        edt_lastname = addressPageBinding.MageNativeUserLastName;
        edt_street = addressPageBinding.MageNativeStreet;
        edt_city = addressPageBinding.MageNativeCity;
        edt_state = addressPageBinding.MageNativeState;
        edt_pincode = addressPageBinding.MageNativePincode;
        edt_country = addressPageBinding.MageNativeCountry;
        edt_mobile = addressPageBinding.MageNativePhone;
        state_dropdown = addressPageBinding.MageNativeStateDropdown;
        state_layout = addressPageBinding.stateLayout;
        state_dropdown_layout = addressPageBinding.stateDropdownLayout;
        prefixsection = addressPageBinding.prefixsection;
        suffixsection = addressPageBinding.suffixsection;
        prefixlabel = addressPageBinding.prefixlabel;

        suffixlabel = addressPageBinding.suffixlabel;
        prefix = addressPageBinding.prefix;
        suffix = addressPageBinding.suffix;
        prefixname = addressPageBinding.prefixname;
        suffixname = addressPageBinding.suffixname;
        prefixoptions = addressPageBinding.prefixoptions;
        suffixoptions = addressPageBinding.suffixoptions;
        MageNative_midllename = addressPageBinding.MageNativeMidllename;
        MageNative_taxvat = addressPageBinding.MageNativeTaxvat;
        edt_savedetail = addressPageBinding.MageNativeSaveAddress;

        edt_firstname.setText(Objects.requireNonNull(array)[0]);
        edt_lastname.setText(array[1]);
        edt_city.setText(array[2]);
        edt_state.setText(array[3]);
        state_dropdown.setText(array[3]);
        edt_mobile.setText(array[4]);
        edt_street.setText(array[5]);
        edt_pincode.setText(array[6]);
        edt_country.setText(array[7]);
        txt_id.setText(array[8]);
        if (array[13].equals(getResources().getString(R.string.DefaultBilling))){
            useforbilling.setChecked(true);
        }
        if (array[14].equals(getResources().getString(R.string.DefaultShipping))){
            useforshipping.setChecked(true);
        }
        getcountries();

        state_dropdown.setOnClickListener(v -> {
            if(statecodelist.size()>0 && statelabellist.size()>0)
            {
                nostates=false;
               // state_dropdown.setEnabled(false);
                final CharSequence[] arrayOfInt = statecodelist.toArray(new CharSequence[statecodelist.size()]);
                final CharSequence[] arrayOfInt2 = statelabellist.toArray(new CharSequence[statelabellist.size()]);
                new MaterialAlertDialogBuilder(Ced_UpdateAddressbook.this,R.style.SingleChoiceRadioStyle)
                .setTitle(Html.fromHtml(getResources().getString(R.string.selectstate)))
                .setSingleChoiceItems(arrayOfInt2, -1, (dialog, postion) -> {
                            state_code = (String) arrayOfInt[postion];
                            state_label = (String) arrayOfInt2[postion];
                            state_dropdown.setText(state_label);
                            dialog.dismiss();
                            State = "";
                        })
                .show();
            }
            else
            {
                state_dropdown.setEnabled(true);
                nostates=true;

            }
        });

        edt_country.setOnClickListener(v -> {
            final CharSequence[] arrayOfInt = countrycodelist.toArray(new CharSequence[countrycodelist.size()]);
            final CharSequence[] arrayOfInt2 = countrylabellist.toArray(new CharSequence[countrylabellist.size()]);
           new MaterialAlertDialogBuilder(Ced_UpdateAddressbook.this,R.style.SingleChoiceRadioStyle)
            .setTitle(getResources().getString(R.string.selectcountry))
            .setSingleChoiceItems(arrayOfInt2, -1, (dialog, postion) -> {
                        country_code = (String) arrayOfInt[postion];
                        country_label = (String) arrayOfInt2[postion];
                        edt_country.setText(country_label);
                        dialog.dismiss();
                        getState(country_code);
                        State="";
                        state_dropdown.setText("");
                        edt_state.setText("");
                    })
            .show();
        });

        edt_savedetail.setOnClickListener(v -> {
            firstname = edt_firstname.getText().toString();
            lastname = edt_lastname.getText().toString();
            Street = edt_street.getText().toString();
            City = edt_city.getText().toString();
            State = edt_state.getText().toString();
            Pincode = edt_pincode.getText().toString();
            Country = edt_country.getText().toString();
            Mobile = edt_mobile.getText().toString();
            id = txt_id.getText().toString();
            if (firstname.length() == 0) {
                edt_firstname.setError(getResources().getString(R.string.empty));
            } else if (lastname.length() == 0) {
                edt_lastname.setError(getResources().getString(R.string.empty));
            } else if (Street.length() == 0) {
                edt_street.setError(getResources().getString(R.string.empty));
            } else if (City.length() == 0) {
                edt_city.setError(getResources().getString(R.string.empty));
            } else if (State.length() == 0 && state_dropdown.getText().toString().length() == 0) {
                edt_state.setError(getResources().getString(R.string.empty));
                state_dropdown.setError(getResources().getString(R.string.empty));
            } else if (Pincode.length() == 0) {
                edt_pincode.setError(getResources().getString(R.string.empty));
            } else if (Country.length() == 0) {
                edt_country.setError(getResources().getString(R.string.empty));
            } else if (Mobile.length() != 10) {
                edt_mobile.setError(getResources().getString(R.string.empty));
            } else {
                JsonObject hashMap = new JsonObject();
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
                                hashMap.addProperty(MageNative_midllename.getTag().toString(), MageNative_midllename.getText().toString());
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
                    if (taxvatflag) {
                        if(flag)
                        {
                            if(MageNative_taxvat.getText().toString().isEmpty())
                            {
                                flag=false;
                                MageNative_taxvat.setError(getResources().getString(R.string.empty));
                                MageNative_taxvat.requestFocus();
                            }
                            else
                            {
                                flag=true;
                        hashMap.addProperty(MageNative_taxvat.getTag().toString(), MageNative_taxvat.getText().toString());
                            }
                        }
                    }
                    if (useforshipping.isChecked())
                    {
                        hashMap.addProperty("default_shipping",true);
                    }
                    if (useforbilling.isChecked())
                    {
                        hashMap.addProperty("default_billing",true);
                    }
                    hashMap.addProperty("email", Email);
                    hashMap.addProperty("firstname", firstname);
                    hashMap.addProperty("lastname", lastname);
                    hashMap.addProperty("street", Street);
                    hashMap.addProperty("city", City);
                    if (nostates)
                    {
                        if(edt_state.getText().toString().equals(""))
                        {
                            hashMap.addProperty("region", state_dropdown.getText().toString());
                        }
                        else
                        {
                            hashMap.addProperty("region", State);
                        }

                    } else {
                        hashMap.addProperty("region_id", state_code);
                    }
                    hashMap.addProperty("postcode", Pincode);
                    hashMap.addProperty("country_id", country_code);
                    hashMap.addProperty("telephone", Mobile);
                    hashMap.addProperty("address_id", id);
                    hashMap.addProperty("customer_id", session.getCustomerid());
                    if (cedSessionManagement.getStoreId() != null) {
                        hashMap.addProperty("store_id", cedSessionManagement.getStoreId());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (flag) {
                    addressViewModel.saveAddressData(Ced_UpdateAddressbook.this, hashMap,session.getHahkey()).observe(Ced_UpdateAddressbook.this, apiResponse -> {
                        switch (apiResponse.status) {
                            case SUCCESS:
                                    update(Objects.requireNonNull(apiResponse.data));

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
        addressViewModel.getRequiredFields(Ced_UpdateAddressbook.this).observe(this, apiResponse -> {
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
                                            prefixsection.setVisibility(View.VISIBLE);
                                            prefixlabel.setText(jsonObject.getString("label"));
                                            prefixname.setText(jsonObject.getString("name"));
                                            RadioGroup ll = new RadioGroup(Ced_UpdateAddressbook.this);
                                            ll.setOrientation(LinearLayout.VERTICAL);
                                            JSONObject prefix_options = jsonObject.getJSONObject("prefix_options");
                                            prefixoptions.setText(prefix_options.toString());
                                            final JSONObject object1 = new JSONObject(prefixoptions.getText().toString());
                                            if (Objects.requireNonNull(prefix_options.names()).length() > 0) {
                                                for (int j = 0; j < Objects.requireNonNull(prefix_options.names()).length(); j++) {
                                                    final RadioButton rdbtn = new RadioButton(Ced_UpdateAddressbook.this);
                                                    rdbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                                        if (isChecked) {
                                                            try {
                                                                prefixvalue = object1.getString(rdbtn.getText().toString());
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });

                                                    rdbtn.setText((CharSequence) Objects.requireNonNull(prefix_options.names()).get(j));
                                                    if (!(array[9].equals("null"))) {
                                                        if (object1.getString(array[9]).equals(object1.getString(rdbtn.getText().toString()))) {
                                                            rdbtn.setChecked(true);
                                                        }
                                                    }
                                                    ll.addView(rdbtn);
                                                }
                                            }
                                            prefix.addView(ll);
                                        }
                                    }
                                    if (jsonObject.has("taxvat")) {
                                        if (jsonObject.getString("taxvat").equals("true")) {
                                            taxvatflag = true;
                                            addressPageBinding.taxvatsection.setVisibility(View.VISIBLE);
                                            MageNative_taxvat.setHint(jsonObject.getString("label")+"*");
                                            MageNative_taxvat.setTag(jsonObject.getString("name"));
                                            if (!(array[12].equals("null"))) {
                                                MageNative_taxvat.setText(array[12]);
                                            }
                                        }
                                    }

                                    if (jsonObject.has("middlename")) {
                                        if (jsonObject.getString("middlename").equals("true")) {
                                            middlenamevatflag = true;
                                            addressPageBinding.middlenamesection.setVisibility(View.VISIBLE);
                                            MageNative_midllename.setHint(jsonObject.getString("label")+"*");
                                            MageNative_midllename.setTag(jsonObject.getString("name"));
                                            if (!(array[11].equals("null"))) {
                                                MageNative_midllename.setText(array[11]);
                                            }
                                        }
                                    }
                                   /* if (jsonObject.has("dob")) {
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
                                            suffixsection.setVisibility(View.VISIBLE);
                                            suffixlabel.setText(jsonObject.getString("label"));
                                            suffixname.setText(jsonObject.getString("name"));
                                            RadioGroup ll = new RadioGroup(Ced_UpdateAddressbook.this);
                                            ll.setOrientation(LinearLayout.VERTICAL);
                                            JSONObject suffixx_options = jsonObject.getJSONObject("suffix_options");
                                            suffixoptions.setText(suffixx_options.toString());
                                            final JSONObject object1 = new JSONObject(suffixoptions.getText().toString());
                                            if (Objects.requireNonNull(suffixx_options.names()).length() > 0) {
                                                for (int j = 0; j < Objects.requireNonNull(suffixx_options.names()).length(); j++) {
                                                    final RadioButton rdbtn = new RadioButton(Ced_UpdateAddressbook.this);
                                                    rdbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                                        if (isChecked) {
                                                            try {
                                                                suffixvalue = object1.getString(rdbtn.getText().toString());
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });
                                                    rdbtn.setText((CharSequence) Objects.requireNonNull(suffixx_options.names()).get(j));
                                                    if (!(array[10].equals("null"))) {
                                                        if (object1.getString(array[10]).equals(object1.getString(rdbtn.getText().toString()))) {
                                                            rdbtn.setChecked(true);
                                                        }
                                                    }
                                                    ll.addView(rdbtn);
                                                }
                                            }
                                            suffix.addView(ll);
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

    private void update(String Output_string ) {
        try {
            jsonObj = new JSONObject(Output_string);
            if (jsonObj.has("header") && jsonObj.getString("header").equals("false")) {
              // showmsg(getResources().getString(R.string.url_not_found));
                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                response = jsonObj.getJSONObject(KEY_OBJECT).getJSONArray(KEY_CUSTOMER);
                for (int i = 0; i < response.length(); i++) {
                    JSONObject c = null;
                    c = response.getJSONObject(i);
                    String status = c.getString(KEY_STATUS);
                    if (status.equals("success")) {
                        Intent addresslistingintent = new Intent(getApplicationContext(), Ced_Addressbook.class);
                        addresslistingintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(addresslistingintent);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    } else {
                        showmsg(getResources().getString(R.string.pleaseTryagain));
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
     * get States
     *
     * @param country_code
     */
    private void getState(String country_code) {
       // String statesUrl = Urls.GET_COUNTRIES + country_code;
        addressViewModel.getStatesData(Ced_UpdateAddressbook.this, country_code).observe(this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                        try {
                            JSONObject object = new JSONObject(Objects.requireNonNull(apiResponse.data));
                            Boolean status = object.getBoolean("success");
                            if (status.equals(true)) {
                                state_dropdown_layout.setVisibility(View.VISIBLE);
                                state_layout.setVisibility(View.GONE);
                                edt_state.setText("");
                                JSONArray jsonArray = object.getJSONArray("states");
                                if(jsonArray.length()>0)
                                {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject c = jsonArray.getJSONObject(i);
                                        statecodelist.add(c.getString("region_id"));
                                        statelabellist.add(c.getString("default_name"));
                                        state_labelcode_list.put(c.getString("default_name"),c.getString("region_id"));
                                    }
                                    if(state_labelcode_list.containsKey(state_dropdown.getText().toString()))
                                    {
                                        state_code = String.valueOf(state_labelcode_list.get(state_dropdown.getText().toString()));
                                        state_label = state_dropdown.getText().toString();
                                        edt_state.setText(state_label);
                                        State = "";
                                    }
                                }
                                else
                                {

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

    /**
     * getCountries
     */
    private void getcountries() {
        addressViewModel.getCountriesData(Ced_UpdateAddressbook.this).observe(this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:

                        try {
                            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(apiResponse.data));
                            if (jsonObject.has("header") && jsonObject.getString("header").equals("false")) {
                                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            } else {
                                JSONArray jsonArray = jsonObject.getJSONArray("country");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    countrycodelist.add(object.getString("value"));
                                    countrylabellist.add(object.getString("label"));
                                    country_labelcode_list.put(object.getString("label"),object.getString("value"));
                                }
                                if(country_labelcode_list.containsKey(edt_country.getText().toString()))
                                {
                                    country_code = String.valueOf(country_labelcode_list.get(edt_country.getText().toString()));
                                    country_label = edt_country.getText().toString();
                                    edt_country.setText(country_label);
                                    getState(country_code);
                                }
                                else
                                {
                                    edt_country.setText("");
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

}