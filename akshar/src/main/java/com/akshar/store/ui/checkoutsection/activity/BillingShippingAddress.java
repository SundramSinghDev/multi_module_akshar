package com.akshar.store.ui.checkoutsection.activity;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.akshar.store.ui.addresssection.activity.Ced_Addressbook;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonObject;
import com.akshar.store.ui.addresssection.activity.Ced_AddAddress;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.R;
import com.akshar.store.databinding.ActivityBillingShippingAddressBinding;
import com.akshar.store.databinding.MagenativeAddressListItemShippingBinding;
import com.akshar.store.ui.addresssection.viewmodel.AddressViewModel;
import com.akshar.store.ui.checkoutsection.viewmodel.CheckoutViewModel;
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
public class BillingShippingAddress extends Ced_NavigationActivity {
    ActivityBillingShippingAddressBinding addressBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    AddressViewModel addressViewModel;
    CheckoutViewModel checkoutViewModel;

    List<String> countrylabellist;
    List<String> statelabellist;
    HashMap<String, String> countrylabel_code;
    HashMap<String, String> statelabel_code;
    HashMap<String, String> statelabel_id;
    boolean statedropdown = false;
    JsonObject hashMap;
    static final String KEY_FIRSTNAME = "firstname";
    static final String KEY_LASTNAME = "lastname";
    static final String KEY_CITY = "city";
    static final String KEY_STATE = "region";
    static final String KEY_PHONE = "phone";
    static final String KEY_STREET = "street";
    static final String KEY_COUNTRY = "country";
    static final String KEY_PINCODE = "pincode";
    static final String KEY_ID = "address_id";
    String address_id = "";
    boolean ishavingdownloadableonly;
    boolean prefixflag = false;
    boolean suffixflag = false;
    boolean taxvatflag = false;
    boolean middlenamevatflag = false;
    String prefixvalue = "";
    String suffixvalue = "";
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showtootltext(getResources().getString(R.string.billingaddress));
        showbackbutton();
        addressViewModel = new ViewModelProvider(BillingShippingAddress.this, viewModelFactory).get(AddressViewModel.class);
        checkoutViewModel = new ViewModelProvider(BillingShippingAddress.this, viewModelFactory).get(CheckoutViewModel.class);
        addressBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_billing_shipping_address, content, true);
        countrylabellist = new ArrayList<>();
        statelabellist = new ArrayList<>();
        countrylabel_code = new HashMap<>();
        statelabel_code = new HashMap<>();
        statelabel_id = new HashMap<>();

        ishavingdownloadableonly = getIntent().getBooleanExtra("ishavingdownloadableonly", false);

        hashMap = new JsonObject();
        try {
            if (session.isLoggedIn()) {
                hashMap.addProperty("customer_id", session.getCustomerid());
            }
            if (cedSessionManagement.getCartId() != null) {
                hashMap.addProperty("cart_id", cedSessionManagement.getCartId());
            }
            if (cedSessionManagement.getStoreId() != null) {
                hashMap.addProperty("store_id", cedSessionManagement.getStoreId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        getcountries();

        addressBinding.addnewaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(session.isLoggedIn())
                {
                    Intent NewAddressIntent = new Intent(BillingShippingAddress.this, Ced_AddAddress.class);
                    {
                        NewAddressIntent.putExtra("fromcheckout",true);
                    }
                    startActivity(NewAddressIntent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
                else
                {
                    addressBinding.address.setVisibility(View.VISIBLE);
                }
            }
        });

        addressBinding.country.setOnClickListener(v -> {
            final CharSequence[] arrayOfInt2 = countrylabellist.toArray(new CharSequence[countrylabellist.size()]);
            Dialog levelDialog1 = new Dialog(BillingShippingAddress.this);
           new MaterialAlertDialogBuilder(BillingShippingAddress.this,R.style.SingleChoiceRadioStyle)
           .setTitle(getResources().getString(R.string.selectcountry))
           .setSingleChoiceItems(arrayOfInt2, -1, (dialog, postion) -> {
                addressBinding.country.setText(arrayOfInt2[postion]);
                        dialog.dismiss();
                        if (!(arrayOfInt2[postion].equals("United Kingdom"))) {
                            getState(countrylabel_code.get(arrayOfInt2[postion]));
                        }
                    }
            )
            .show();
        });

        addressBinding.regionId.setOnClickListener(v -> {
            final CharSequence[] arrayOfInt2 = statelabellist.toArray(new CharSequence[statelabellist.size()]);
            Dialog levelDialog1 = new Dialog(BillingShippingAddress.this);
            new MaterialAlertDialogBuilder(BillingShippingAddress.this,R.style.SingleChoiceRadioStyle)
            .setTitle(getResources().getString(R.string.selectstate) )
            .setSingleChoiceItems(arrayOfInt2, -1, (dialog, postion) -> {
                addressBinding.regionId.setText(arrayOfInt2[postion]);
                        dialog.dismiss();
                    })
           .show();
        });

        if (session.isLoggedIn()) {
            addressBinding.email.setText(session.getUserDetails().get("Email"));
            addressBinding.email.setEnabled(false);
        }

        addressBinding.continueaddress.setOnClickListener(v -> {
            if (address_id.isEmpty()) {
                addressBinding.address.setVisibility(View.VISIBLE);
                if (!isValidEmail(addressBinding.email.getText().toString())) {
                    addressBinding.email.setError("" + getResources().getString(R.string.pleaseentervalidemail));
                    addressBinding.email.requestFocus();
                } else {
                    session.saveCheckout_GuestEmail(addressBinding.email.getText().toString());
                    if (addressBinding.firstname.getText().toString().isEmpty()) {
                        addressBinding.firstname.setError("" + getResources().getString(R.string.fillsomevalue));
                        addressBinding.firstname.requestFocus();
                    } else {
                        if (addressBinding.lastname.getText().toString().isEmpty()) {
                            addressBinding.lastname.setError("" + getResources().getString(R.string.fillsomevalue));
                            addressBinding.lastname.requestFocus();
                        } else {
                            if (addressBinding.street.getText().toString().isEmpty()) {
                                addressBinding.street.setError("" + getResources().getString(R.string.fillsomevalue));
                                addressBinding.street.requestFocus();
                            } else {
                                if (addressBinding.city.getText().toString().isEmpty()) {
                                    addressBinding.city.setError("" + getResources().getString(R.string.fillsomevalue));
                                    addressBinding.city.requestFocus();
                                } else {
                                    if (addressBinding.zipcode.getText().toString().isEmpty()) {
                                        addressBinding.zipcode.setError("" + getResources().getString(R.string.fillsomevalue));
                                        addressBinding.zipcode.requestFocus();
                                    } else {
                                        if (addressBinding.phonenumber.getText().toString().isEmpty() || addressBinding.phonenumber.getText().toString().length()!=10) {
                                            addressBinding.phonenumber.setError("" + getResources().getString(R.string.invalidphone));
                                            addressBinding.phonenumber.requestFocus();
                                        } else {
                                            session.saveCheckout_GuestMobile(addressBinding.phonenumber.getText().toString().replace("+91",""));
                                            if (addressBinding.country.getText().toString().isEmpty()) {
                                                addressBinding.country.setError("" + getResources().getString(R.string.fillsomevalue));
                                                addressBinding.country.requestFocus();
                                            } else {
                                                if (statedropdown) {
                                                    if (addressBinding.regionId.getText().toString().isEmpty()) {
                                                        addressBinding.regionId.setError("" + getResources().getString(R.string.fillsomevalue));
                                                        addressBinding.regionId.requestFocus();
                                                    } else {

                                                        JsonObject shippingmethoddata = new JsonObject();
                                                        JsonObject object = new JsonObject();
                                                        try {
                                                            if (prefixflag) {
                                                                if (prefixvalue.isEmpty()) {
                                                                    flag = false;
                                                                   showmsg(getResources().getString(R.string.selectsomeprefixvalue));
                                                                } else {
                                                                    flag = true;
                                                                    object.addProperty(addressBinding.prefixname.getText().toString(), prefixvalue);
                                                                }
                                                            }
                                                            if (middlenamevatflag) {
                                                                if (flag) {
                                                                    if (addressBinding.middlename.getText().toString().isEmpty()) {
                                                                        flag = false;
                                                                        addressBinding.middlename.setError(getResources().getString(R.string.empty));
                                                                        addressBinding.middlename.requestFocus();
                                                                    } else {
                                                                        flag = true;
                                                                        object.addProperty(addressBinding.middlename.getTag().toString(), addressBinding.middlename.getText().toString());
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
                                                                        object.addProperty(addressBinding.suffixname.getText().toString(), suffixvalue);
                                                                    }
                                                                }

                                                            }
                                                            if (taxvatflag) {
                                                                if (flag) {
                                                                   /* if(MageNative_taxvat.getText().toString().isEmpty())
                                                                    {
                                                                        flag=false;
                                                                        MageNative_taxvat.setError(getResources().getString(R.string.empty));
                                                                        MageNative_taxvat.requestFocus();
                                                                    }
                                                                    else
                                                                    {
                                                                        flag=true;*/
                                                                    object.addProperty(addressBinding.taxvatname.getText().toString(), addressBinding.MageNativeTaxvat.getText().toString());
                                                                    /* }*/
                                                                }
                                                            }


                                                            shippingmethoddata.addProperty("email", addressBinding.email.getText().toString());
                                                            if (cedSessionManagement.getCartId() != null) {
                                                                shippingmethoddata.addProperty("cart_id", cedSessionManagement.getCartId());
                                                            }
                                                            if (session.isLoggedIn()) {
                                                                shippingmethoddata.addProperty("customer_id", session.getCustomerid());
                                                                shippingmethoddata.addProperty("Role", "USER");
                                                            }
                                                            else
                                                            {
                                                                shippingmethoddata.addProperty("Role", "Guest");
                                                            }
                                                            if (session.isLoggedIn()) {
                                                                shippingmethoddata.addProperty("save_in_address", true);
                                                            } else {
                                                                shippingmethoddata.addProperty("save_in_address", false);
                                                            }
                                                            JsonObject streetdata = new JsonObject();
                                                            streetdata.addProperty("0", addressBinding.street.getText().toString());

                                                            object.addProperty("firstname", addressBinding.firstname.getText().toString());
                                                            object.addProperty("lastname", addressBinding.lastname.getText().toString());
                                                            object.addProperty("street", streetdata.toString());
                                                            object.addProperty("city", addressBinding.city.getText().toString());
                                                            object.addProperty("region_id", statelabel_id.get(addressBinding.regionId.getText().toString()));
                                                            object.addProperty("region", " ");
                                                            object.addProperty("postcode", addressBinding.zipcode.getText().toString());
                                                            object.addProperty("country_id", countrylabel_code.get(addressBinding.country.getText().toString()));
                                                            object.addProperty("telephone", addressBinding.phonenumber.getText().toString());
                                                            object.addProperty("fax", " ");

                                                            shippingmethoddata.addProperty("billingaddress", object.toString());
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (flag) {
                                                            requestforbillingaddress(shippingmethoddata);
                                                        }
                                                    }
                                                } else {
                                                    if (addressBinding.region.getText().toString().isEmpty()) {
                                                        addressBinding.region.setError(getResources().getString(R.string.empty));
                                                        addressBinding.region.requestFocus();
                                                    } else {

                                                        JsonObject shippingmethoddata = new JsonObject();
                                                        JsonObject object = new JsonObject();
                                                        try {
                                                            if (prefixflag) {
                                                                if (prefixvalue.isEmpty()) {
                                                                    flag = false;
                                                                   showmsg(getResources().getString(R.string.selectsomeprefixvalue));
                                                                } else {
                                                                    flag = true;
                                                                    object.addProperty(addressBinding.prefixname.getText().toString(), prefixvalue);
                                                                }
                                                            }
                                                            if (middlenamevatflag) {
                                                                if (flag) {
                                                                    if (addressBinding.middlename.getText().toString().isEmpty()) {
                                                                        flag = false;
                                                                        addressBinding.middlename.setError(getResources().getString(R.string.empty));
                                                                        addressBinding.middlename.requestFocus();
                                                                    } else {
                                                                        flag = true;
                                                                        object.addProperty(addressBinding.middlename.getTag().toString(), addressBinding.middlename.getText().toString());
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
                                                                        object.addProperty(addressBinding.suffixname.getText().toString(), suffixvalue);
                                                                    }
                                                                }

                                                            }
                                                            if (taxvatflag) {
                                                                if (flag) {
                                                                        /*if(MageNative_taxvat.getText().toString().isEmpty())
                                                                        {
                                                                            flag=false;
                                                                            MageNative_taxvat.setError(getResources().getString(R.string.empty));
                                                                            MageNative_taxvat.requestFocus();
                                                                        }
                                                                        else
                                                                        {
                                                                            flag=true;*/
                                                                    object.addProperty(addressBinding.taxvatname.getText().toString(), addressBinding.MageNativeTaxvat.getText().toString());
                                                                    /* }*/
                                                                }
                                                            }
                                                            shippingmethoddata.addProperty("email", addressBinding.email.getText().toString());

                                                            if (cedSessionManagement.getCartId() != null) {
                                                                shippingmethoddata.addProperty("cart_id", cedSessionManagement.getCartId());
                                                            }
                                                            if (session.isLoggedIn()) {
                                                                shippingmethoddata.addProperty("customer_id", session.getCustomerid());
                                                                shippingmethoddata.addProperty("Role", "USER");
                                                            }
                                                            else
                                                            {
                                                                shippingmethoddata.addProperty("Role", "Guest");
                                                            }
                                                            if (session.isLoggedIn()) {
                                                                shippingmethoddata.addProperty("save_in_address", true);
                                                            } else {
                                                                shippingmethoddata.addProperty("save_in_address", false);
                                                            }
                                                            JsonObject streetdata = new JsonObject();
                                                            streetdata.addProperty("0", addressBinding.street.getText().toString());

                                                            object.addProperty("firstname", addressBinding.firstname.getText().toString());
                                                            object.addProperty("lastname", addressBinding.lastname.getText().toString());
                                                            object.addProperty("street", streetdata.toString());
                                                            object.addProperty("city", addressBinding.city.getText().toString());
                                                            // object.put("region_id"," ");
                                                            object.addProperty("region", addressBinding.region.getText().toString());
                                                            object.addProperty("postcode", addressBinding.zipcode.getText().toString());
                                                            object.addProperty("country_id", countrylabel_code.get(addressBinding.country.getText().toString()));
                                                            object.addProperty("telephone", addressBinding.phonenumber.getText().toString());
                                                            object.addProperty("email", addressBinding.email.getText().toString());
                                                            object.addProperty("fax", " ");

                                                            shippingmethoddata.addProperty("billingaddress", object.toString());
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (flag) {
                                                            requestforbillingaddress(shippingmethoddata);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else {
                if(session.getCheckout_CustomerMobile().replace("+91","").length()!=10)
                {
                    showmsg(getResources().getString(R.string.invalidphone));
                }
                else
                {
                    try {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("Role", "USER");
                        jsonObject.addProperty("cart_id", cedSessionManagement.getCartId());
                        jsonObject.addProperty("customer_id", session.getCustomerid());
                        jsonObject.addProperty("address_id", address_id);
                        requestforbillingaddress(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void get_the_fields() {
        addressViewModel.getRequiredFields(BillingShippingAddress.this).observe(BillingShippingAddress.this, apiResponse -> {
            switch (apiResponse.status){
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
                                            addressBinding.prefixsection.setVisibility(View.VISIBLE);
                                            addressBinding.prefixlabel.setText(jsonObject.getString("label"));
                                            addressBinding.prefixname.setText(jsonObject.getString("name"));
                                            RadioGroup ll = new RadioGroup(BillingShippingAddress.this);
                                            ll.setOrientation(LinearLayout.VERTICAL);
                                            JSONObject prefix_options = jsonObject.getJSONObject("prefix_options");
                                            addressBinding.prefixoptions.setText(prefix_options.toString());
                                            if (Objects.requireNonNull(prefix_options.names()).length() > 0) {
                                                for (int j = 0; j < Objects.requireNonNull(prefix_options.names()).length(); j++) {
                                                    final RadioButton rdbtn = new RadioButton(BillingShippingAddress.this);
                                                    rdbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                                        if (isChecked) {
                                                            try {
                                                                JSONObject object1 = new JSONObject(addressBinding.prefixoptions.getText().toString());
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
                                            addressBinding.prefix.addView(ll);
                                        }
                                    }
                                    if (jsonObject.has("taxvat")) {
                                        if (jsonObject.getString("taxvat").equals("true")) {
                                            taxvatflag = true;
                                            addressBinding.taxvatsection.setVisibility(View.VISIBLE);
                                            addressBinding.MageNativeTaxvat.setHint(jsonObject.getString("label"));
                                            //taxvatlabel.setText(jsonObject.getString("label"));
                                            addressBinding.taxvatname.setText(jsonObject.getString("name"));
                                        }
                                    }

                                    if (jsonObject.has("middlename")) {
                                        if (jsonObject.getString("middlename").equals("true")) {
                                            middlenamevatflag = true;
                                            addressBinding.middlenamelayout.setVisibility(View.VISIBLE);
                                            addressBinding.middlenamelayout.setHint(jsonObject.getString("label"));
                                            addressBinding.middlename.setTag(jsonObject.getString("name"));
                                        }
                                    }
                                    if (jsonObject.has("suffix")) {
                                        if (jsonObject.getString("suffix").equals("true")) {
                                            suffixflag = true;
                                            addressBinding.suffixsection.setVisibility(View.VISIBLE);
                                            addressBinding.suffixlabel.setText(jsonObject.getString("label"));
                                            addressBinding.suffixname.setText(jsonObject.getString("name"));
                                            RadioGroup ll = new RadioGroup(BillingShippingAddress.this);
                                            ll.setOrientation(LinearLayout.VERTICAL);
                                            JSONObject suffixx_options = jsonObject.getJSONObject("suffix_options");
                                            addressBinding.suffixoptions.setText(suffixx_options.toString());
                                            if (Objects.requireNonNull(suffixx_options.names()).length() > 0) {
                                                for (int j = 0; j < Objects.requireNonNull(suffixx_options.names()).length(); j++) {
                                                    final RadioButton rdbtn = new RadioButton(BillingShippingAddress.this);
                                                    rdbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                                        if (isChecked) {
                                                            try {
                                                                JSONObject object1 = new JSONObject(addressBinding.suffixoptions.getText().toString());
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
                                            addressBinding.suffix.addView(ll);
                                        }
                                    }
                                }
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;

                case ERROR:
                    Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                    showmsg(getResources().getString(R.string.errorString));
                    break;
            }
        });

        /*Ced_ClientRequestResponseRest requestResponse = new Ced_ClientRequestResponseRest(output -> {
            // start from here
            if (session.isLoggedIn()) {
                featchaddressbook();
            } else {
                defaultaddress.setVisibility(View.GONE);
                addnewaddress.setVisibility(View.GONE);
                address.setVisibility(View.VISIBLE);
            }

            String jstring = output.toString();
            JSONObject object = new JSONObject(jstring);
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
                                RadioGroup ll = new RadioGroup(BillingShippingAddress.this);
                                ll.setOrientation(LinearLayout.VERTICAL);
                                JSONObject prefix_options = jsonObject.getJSONObject("prefix_options");
                                prefixoptions.setText(prefix_options.toString());
                                if (Objects.requireNonNull(prefix_options.names()).length() > 0) {
                                    for (int j = 0; j < Objects.requireNonNull(prefix_options.names()).length(); j++) {
                                        final RadioButton rdbtn = new RadioButton(BillingShippingAddress.this);
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
                                prefix.addView(ll);
                            }
                        }
                        if (jsonObject.has("taxvat")) {
                            if (jsonObject.getString("taxvat").equals("true")) {
                                taxvatflag = true;
                                taxvatsection.setVisibility(View.VISIBLE);
                                MageNative_taxvat.setHint(jsonObject.getString("label"));
                                //taxvatlabel.setText(jsonObject.getString("label"));
                                taxvatname.setText(jsonObject.getString("name"));
                            }
                        }
                        if (jsonObject.has("middlename")) {
                            if (jsonObject.getString("middlename").equals("true")) {
                                middlenamevatflag = true;
                                middlenamesection.setVisibility(View.VISIBLE);
                                middlenamelabel.setText(jsonObject.getString("label"));
                                middlename.setText(jsonObject.getString("name"));
                            }
                        }
                        if (jsonObject.has("suffix")) {
                            if (jsonObject.getString("suffix").equals("true")) {
                                suffixflag = true;
                                suffixsection.setVisibility(View.VISIBLE);
                                suffixlabel.setText(jsonObject.getString("label"));
                                suffixname.setText(jsonObject.getString("name"));
                                RadioGroup ll = new RadioGroup(BillingShippingAddress.this);
                                ll.setOrientation(LinearLayout.VERTICAL);
                                JSONObject suffixx_options = jsonObject.getJSONObject("suffix_options");
                                suffixoptions.setText(suffixx_options.toString());
                                if (Objects.requireNonNull(suffixx_options.names()).length() > 0) {
                                    for (int j = 0; j < Objects.requireNonNull(suffixx_options.names()).length(); j++) {
                                        final RadioButton rdbtn = new RadioButton(BillingShippingAddress.this);
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
                                suffix.addView(ll);
                            }
                        }
                    }
                }
            }
        }, BillingShippingAddress.this);
        requestResponse.execute(requiredfieldurl);*/
    }

    public void requestforbillingaddress(JsonObject data) {
        try {
            checkoutViewModel.saveBillingAddress(BillingShippingAddress.this, data).observe(BillingShippingAddress.this, apiResponse -> {
                switch (apiResponse.status){
                    case SUCCESS:
                        Processdata(apiResponse.data);
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

    private void Processdata(String s){
        try {
            JSONObject object = new JSONObject(s);
            String success = object.getString("success");
            if (success.equals("true")) {
                Intent intent = new Intent(getApplicationContext(), ShippingMethodList.class);
                intent.putExtra("ishavingdownloadableonly", ishavingdownloadableonly);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                if (object.has("message")) {
                    showmsg(object.getString("message"));
                } else {
                    showmsg(getResources().getString(R.string.somethingbadhappended));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getcountries() {
        try {
            if (countrylabellist.size() > 0) {
                countrylabellist.clear();
            }

            addressViewModel.getCountriesData(BillingShippingAddress.this).observe(BillingShippingAddress.this, apiResponse -> {
                switch (apiResponse.status){
                    case SUCCESS:
                        try {
                            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(apiResponse.data));
                            JSONArray jsonArray = jsonObject.getJSONArray("country");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                countrylabel_code.put(object.getString("label"), object.getString("value"));
                                countrylabellist.add(object.getString("label"));
                            }

                            if (session.isLoggedIn()) {
                                featchaddressbook();
                            } else {
                                addressBinding.defaultaddress.setVisibility(View.GONE);
                                addressBinding.addnewaddress.setVisibility(View.GONE);
                                addressBinding.address.setVisibility(View.VISIBLE);
                                get_the_fields();
                            }

//                            get_the_fields();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;

                    case ERROR:
                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                        showmsg(getResources().getString(R.string.errorString));
                        break;
                }
            });

            /*Ced_ClientRequestResponseRest crr = new Ced_ClientRequestResponseRest(output -> {
                JSONObject jsonObject = new JSONObject(output.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("country");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    countrylabel_code.put(object.getString("label"), object.getString("value"));
                    countrylabellist.add(object.getString("label"));
                }
                get_the_fields();
            }, BillingShippingAddress.this);
            crr.execute(countryurl);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void featchaddressbook() {
        try {
            addressViewModel.getAddressListData(BillingShippingAddress.this, hashMap,session.getHahkey()).observe(BillingShippingAddress.this, apiResponse -> {
                switch (apiResponse.status){
                    case SUCCESS:
                        try {
                            applydata(apiResponse.data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    case ERROR:
                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                        showmsg(getResources().getString(R.string.errorString));
                        break;
                }
            });

            /*Ced_ClientRequestResponseRest crr = new Ced_ClientRequestResponseRest(output -> {
                try {
                    applydata(output.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, BillingShippingAddress.this, "POST", hashMap.toString());
            crr.execute(Url);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applydata(String Jstring) throws JSONException {
        JSONObject jsonObject = new JSONObject(Jstring);
        if (jsonObject.has("header") && jsonObject.getString("header").equals("false")) {
            Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        } else {
            if (jsonObject.getJSONObject("data").getString("status").equals("success")) {
                JSONArray userdetail = jsonObject.getJSONObject("data").getJSONArray("address");
                for (int i = 0; i < userdetail.length(); i++) {
                    JSONObject c = null;
                    c = userdetail.getJSONObject(i);
                    String firstname, lastname, city, state, phone, country, street="", pincode, id = null;
                    id = c.getString(KEY_ID);
                    firstname = c.getString(KEY_FIRSTNAME);
                    lastname = c.getString(KEY_LASTNAME);
                    city = c.getString(KEY_CITY);
                    state = c.getString(KEY_STATE);
                    phone = c.getString(KEY_PHONE);
                    country = c.getString(KEY_COUNTRY);
                    if (c.has(Ced_Addressbook.KEY_STREET)) {
                        if (c.getString(Ced_Addressbook.KEY_STREET).contains("{")) {
                            street = String.valueOf(new JSONObject(c.getString(Ced_Addressbook.KEY_STREET)).get("0"));
                        } else {
                            street = c.getString(Ced_Addressbook.KEY_STREET);
                        }
                    }
//                    street = c.getString(KEY_STREET);
                    pincode = c.getString(KEY_PINCODE);

                    MagenativeAddressListItemShippingBinding binding= DataBindingUtil.inflate(getLayoutInflater(),R.layout.magenative_address_list_item_shipping, content,true);

                    final TextView firstnametext = binding.MageNativeClientFirstname;
                    final CheckBox selectaddress = binding.selectaddress;
                    final TextView lastnametext = binding.MageNativeClientLastname;
                    final TextView citytext = binding.MageNativeClientCity;
                    final TextView statetext = binding.MageNativeClientState;
                    final TextView phonetext = binding.MageNativeClientPhone;
                    final TextView countrytext = binding.MageNativeClientCountry;
                    final TextView streettext = binding.MageNativeClientStreet;
                    final TextView pincodetext = binding.MageNativeClientPincode;
                    final TextView idtext = binding.MageNativeClientId;
                    final TextView MageNative_prefix = binding.MageNativePrefix;
                    final TextView MageNativemiddlename = binding.MageNativemiddlename;
                    final TextView MageNative_suffix = binding.MageNativeSuffix;
                    final TextView MageNative_taxvat = binding.MageNativeTaxvat;

                    set_regular_font_fortext(firstnametext);
                    set_regular_font_fortext(lastnametext);
                    set_regular_font_fortext(citytext);
                    set_regular_font_fortext(statetext);
                    set_regular_font_fortext(phonetext);
                    set_regular_font_fortext(countrytext);
                    set_regular_font_fortext(streettext);
                    set_regular_font_fortext(pincodetext);

                    firstnametext.setText(firstname);
                    lastnametext.setText(lastname);
                    citytext.setText(city);
                    statetext.setText(state);
                    phonetext.setText(phone);
                    countrytext.setText(country);
                    streettext.setText(street);
                    pincodetext.setText(pincode);
                    idtext.setText(id);

                    if (c.has("prefix")) {
                        if (!(c.get("prefix").equals("null"))) {
                            MageNative_prefix.setText(c.getString("prefix"));
                        }
                    }
                    if (c.has("suffix")) {
                        if (!(c.get("suffix").equals("null"))) {
                            MageNative_suffix.setText(c.getString("suffix"));
                        }
                    }
                    if (c.has("middlename")) {
                        if (!(c.get("middlename").equals("null"))) {
                            MageNativemiddlename.setText(c.getString("middlename"));
                        }
                    }
                    if (c.has("taxvat")) {
                        if (!(c.get("taxvat").equals("null"))) {
                            MageNative_taxvat.setText(c.getString("taxvat"));
                        }
                    }
                    selectaddress.setButtonDrawable(checkbox_visibility);
                    selectaddress.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        LinearLayout linearLayout = (LinearLayout) selectaddress.getParent();
                        CardView cardView = (CardView) linearLayout.getParent();
                        LinearLayout layout = (LinearLayout) cardView.getParent();
                        TextView view = (TextView) layout.getChildAt(0);
                        CardView cardView1 = (CardView) layout.getChildAt(1);
                        LinearLayout linearview = (LinearLayout) cardView1.getChildAt(0);
                        LinearLayout linearview2 = (LinearLayout) linearview.getChildAt(1);
                        TextView MageNative_client_phone = (TextView) linearview2.getChildAt(4);
                        if (isChecked) {
                            if (address_id.isEmpty())
                            {
                                address_id = view.getText().toString();
                                Log.i("address_id", "" + address_id);
                                session.saveCheckout_CustomerMobile(MageNative_client_phone.getText().toString().replace("+91",""));
                            }
                            else
                                {
                                showmsg(getResources().getString(R.string.selectonebillingaddress));
                                selectaddress.setChecked(false);
                            }

                        } else {
                            if (address_id.equals(view.getText().toString())) {
                                address_id = "";
                                selectaddress.setChecked(false);
                                Log.i("address_id", "hello" + address_id);
                            }
                            else {
                                selectaddress.setChecked(false);
                                Log.i("address_id", "hello2" + address_id);
                            }
                        }
                    });
                    if(binding.getRoot().getParent() != null) {
                        ((ViewGroup)binding.getRoot().getParent()).removeView(binding.getRoot()); // <- fix
                    }
                    addressBinding.defaultaddress.addView(binding.getRoot());
                }
            } else if (jsonObject.getJSONObject("data").getString("status").equals("no_address")) {
                addressBinding.defaultaddress.setVisibility(View.GONE);
                addressBinding.addnewaddress.setVisibility(View.GONE);
                addressBinding.address.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getState(String country_code) {
        try {
            if (statelabellist.size() > 0) {
                statelabellist.clear();
            }

           // String statesUrl = Urls.GET_COUNTRIES + country_code;

            addressViewModel.getStatesData(BillingShippingAddress.this, country_code).observe(BillingShippingAddress.this, apiResponse -> {
                switch (apiResponse.status){
                    case SUCCESS:
                        try {
                            JSONObject object = new JSONObject(Objects.requireNonNull(apiResponse.data));
                            Boolean status = object.getBoolean("success");
                            if (status.equals(true)) {
                                statedropdown = true;
                                addressBinding.regionIdLayout.setVisibility(View.VISIBLE);
                                //TODO
                                addressBinding.regionLayout.setVisibility(View.GONE);
                                addressBinding.region.setText("");
                                JSONArray jsonArray = object.getJSONArray("states");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    statelabel_id.put(c.getString("name"), c.getString("region_id"));
                                    statelabel_code.put(c.getString("name"), c.getString("code"));
                                    statelabellist.add(c.getString("name"));
                                }
                            } else {
                                statedropdown = false;
                                addressBinding.regionIdLayout.setVisibility(View.GONE);
                                addressBinding.regionLayout.setVisibility(View.VISIBLE);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;

                    case ERROR:
                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                        showmsg(getResources().getString(R.string.errorString));
                        break;
                }
            });

            /*Ced_ClientRequestResponseRest response = new Ced_ClientRequestResponseRest(output -> {
                JSONObject object = new JSONObject(output.toString());
                Boolean status = object.getBoolean("success");
                if (status.equals(true)) {
                    statedropdown = true;
                    region_id.setVisibility(View.VISIBLE);
                    //TODO
                    region.setVisibility(View.GONE);
                    region.setText("");
                    JSONArray jsonArray = object.getJSONArray("states");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        statelabel_id.put(c.getString("name"), c.getString("region_id"));
                        statelabel_code.put(c.getString("name"), c.getString("code"));
                        statelabellist.add(c.getString("name"));
                    }
                } else {
                    statedropdown = false;
                    region_id.setVisibility(View.GONE);
                    region.setVisibility(View.VISIBLE);
                }
            }, BillingShippingAddress.this);
            response.execute(countryurl + country_code);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
