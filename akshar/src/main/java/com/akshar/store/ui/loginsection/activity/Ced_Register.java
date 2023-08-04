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
package com.akshar.store.ui.loginsection.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;

import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.akshar.store.databinding.NewMagenativeRegisterLayoutBinding;
import com.akshar.store.databinding.RegisterViewsLayoutBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import com.akshar.store.databinding.MagenativeNewRegistrationLayoutBinding;
import com.akshar.store.rest.ApiResponse;
import com.akshar.store.ui.loginsection.viewmodel.RegisterViewModel;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;
import com.razorpay.OTP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ced_Register extends Ced_NavigationActivity {

    @Inject
    ViewModelFactory viewModelFactory;
    RegisterViewModel registerViewModel;
    //    MagenativeNewRegistrationLayoutBinding registerBinding;
    NewMagenativeRegisterLayoutBinding registerBinding1;

    EditText edt_firstname, edt_lastname, edt_email, edt_password, edt_cnf_password;
    //    ImageView mr, mis;
    AppCompatButton register_button;
    CheckBox newsletter;
    String status, cart_summary, customer_id, hash, outputstring, isConfirmationRequired,
            message, firstname, lastname, username, email, password, cnf_password, customergroup_id = "";
    TextView account;

    static final String KEY_OBJECT = "data";
    static final String KEY_STATUS = "status";
    static final String KEY_CUSTOMER = "customer";
    static final String KEY_MESSAGE = "message";
    static final String KEY_IS_CONFIRMATION_REQUIRED = "isConfirmationRequired";
    static final String KEY_Message = "message";
    static final String KEY_CUSTOMER_ID = "customer_id";
    static final String KEY_CUSTOMERGROUP_ID = "customer_group";
    static final String KEY_CART_SUMMARY = "cart_summary";
    static final String KEY_HASH = "hash";
    LinearLayout prefixsection, suffixsection, dobsection;
    TextInputLayout middlenamesection, taxvatsection;
    TextView prefixlabel, suffixlabel;
    RadioGroup prefix;
    RadioGroup suffix;
    TextView prefixname, suffixname, prefixoptions, suffixoptions;
    String prefixvalue = "";
    String suffixvalue = "";
    TextView dob;
    //    EditText MageNative_midllename;
    EditText MageNative_taxvat;

    boolean male = true;
    boolean female = false;
    boolean prefixflag = false;
    boolean suffixflag = false;
    boolean dobflag = false;
    boolean taxvatflag = false;
    boolean middlenamevatflag = false;
    boolean flag = true;
    boolean isfromcheckout = false;
    private List<String> school_name;
    private List<String> school_code;
    private HashMap<String, String> school_code_hash_map;
    private String schoolCode, schoolName;
    private int pos = -1;
    private boolean mobileFlag = true;
    private String randomString;
    RegisterViewsLayoutBinding registerBinding;
    BottomSheetBehavior<LinearLayout> schoolListBehavior;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackButtonWithTitle("Sign Up");
        hidebottombar();
//        registerBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_new_registration_layout, content, true);
        registerBinding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.new_magenative_register_layout, content, true);
        registerViewModel = new ViewModelProvider(Ced_Register.this, viewModelFactory).get(RegisterViewModel.class);

        registerBinding = registerBinding1.registerLayout;
        navBinding.MageNativeTawkSupport.setVisibility(View.GONE);

        if (getIntent().getStringExtra("isfromcheckout") != null) {
            isfromcheckout = true;
        }

//        edt_firstname = registerBinding.edtFirstName;
//        edt_lastname = registerBinding.edtLastName;
        edt_email = registerBinding.edtEmail;
        account = registerBinding.txtAccount;
        newsletter = registerBinding.chkNewsLetter;
//        mr = registerBinding.male;
//        mis = registerBinding.female;
        prefixsection = registerBinding.prefixsection;
//        middlenamesection = registerBinding.middlenamesection;
        suffixsection = registerBinding.suffixsection;
        dobsection = registerBinding.dobsection;
        taxvatsection = registerBinding.taxvatsection;
        prefixlabel = registerBinding.prefixlabel;
        suffixlabel = registerBinding.suffixlabel;
      /*  middlenamelabel = registerBinding.middlenamelabel;
        taxvatlabel = registerBinding.taxvatlabel;*/
        prefix = registerBinding.prefix;
        suffix = registerBinding.suffix;
        prefixname = registerBinding.prefixname;
        suffixname = registerBinding.suffixname;
      /*  taxvatname = registerBinding.taxvatname;
        middlename = registerBinding.midllenamename;*/
        prefixoptions = registerBinding.prefixoptions;
        suffixoptions = registerBinding.suffixoptions;
        dob = registerBinding.dob;
//        MageNative_midllename = registerBinding.MageNativeMidllename;
        MageNative_taxvat = registerBinding.MageNativeTaxvat;
        /*
         * generating random alphanumeric string and set it to the confirm password and password field
         *
         * */
        randomString = generateAlphaNumericString(8);
        edt_password = registerBinding.edtPassword;
        edt_cnf_password = registerBinding.edtConfirmPass;

        edt_password.setText(randomString);
        edt_cnf_password.setText(randomString);

        register_button = registerBinding.btnRegister;
        schoolListBehavior = BottomSheetBehavior.from(registerBinding1.schoolListBottomSheet.schoolListParent);
        //school list drop down click listener end
        setbottomsheet(schoolListBehavior, registerBinding1.registerLayout.view);
        schoolListBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        //school list drop down click listener
        registerBinding.MageNativeSchoolList.setOnClickListener(v -> {
            try {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(registerBinding.MageNativeSchoolList.getWindowToken(), 0);
                if (school_code_hash_map != null && school_code_hash_map.size() > 0) {
                    createViewAndBindDataForSchoolList();
                    if (schoolListBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        schoolListBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    } else {
                        schoolListBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //TODO will handle the close button of the school bottom sheet
        registerBinding1.schoolListBottomSheet.closeIv.setOnClickListener(view -> {
            if (schoolListBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                schoolListBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                schoolListBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

/*        mr.setOnClickListener(v -> {
            mr.setBackground(getResources().getDrawable(R.drawable.selected));
            male = true;
            female = false;
            mis.setBackground(getResources().getDrawable(R.drawable.selectedwhite));
        });*/
/*        mis.setOnClickListener(v -> {
            mis.setBackground(getResources().getDrawable(R.drawable.selected));
            male = false;
            female = true;
            mr.setBackground(getResources().getDrawable(R.drawable.selectedwhite));
        });*/
        //TODO handle gender radio button
        registerBinding1.registerLayout.radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            // get selected radio button from radioGroup
            int selectedId = radioGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton radioButton = (RadioButton) findViewById(selectedId);
            if (radioButton.getText().toString().equalsIgnoreCase(getResources().getString(R.string.male))) {
                male = true;
                female = false;
            } else {
                male = false;
                female = true;
            }
        });
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker_DDMMYYYY(dob);
            }
        });


        final JsonObject hashMap = new JsonObject();
        register_button.setOnClickListener(v -> {
            try {
                JsonObject extension_attributes_object_register = new JsonObject();
//                firstname = edt_firstname.getText().toString();
//                lastname = edt_lastname.getText().toString();
                username = Objects.requireNonNull(registerBinding1.registerLayout.edtUserName.getText()).toString();
                email = edt_email.getText().toString();
                password = edt_password.getText().toString();
                cnf_password = edt_cnf_password.getText().toString();

//                if (firstname.isEmpty()) {
//                    edt_firstname.requestFocus();
//                    edt_firstname.setError(getResources().getString(R.string.empty));
//                } else {
//                    if (lastname.isEmpty()) {
//                        edt_lastname.requestFocus();
//                        edt_lastname.setError(getResources().getString(R.string.empty));
//                    } else {
                if (username.isEmpty()) {
                    registerBinding1.registerLayout.edtUserName.requestFocus();
                    registerBinding1.registerLayout.edtUserName.setError(getResources().getString(R.string.empty));
                } else {
                    if (email.isEmpty()) {
                        edt_email.requestFocus();
                        edt_email.setError("Please fill email");
                    } else {
                        if (!isValidEmail(email)) {
                            edt_email.requestFocus();
                            edt_email.setError(getResources().getString(R.string.invalidemail));
                        } else {
                            if (password.isEmpty() || password.length() < 8) {
                                edt_password.requestFocus();
                                edt_password.setError(getResources().getString(R.string.minimum_8_character));
                            } else {
                                if (cnf_password.isEmpty() || !cnf_password.equals(password)) {
                                    edt_cnf_password.requestFocus();
                                    edt_cnf_password.setError(getResources().getString(R.string.confirmnotmatch));
                                } else {
                                    if (prefixflag) {
                                        if (prefixvalue.isEmpty()) {
                                            flag = false;
                                            showmsg(getResources().getString(R.string.selectsomeprefixvalue));
                                        } else {
                                            flag = true;
                                            hashMap.addProperty(prefixname.getText().toString(), prefixvalue);
                                        }
                                    }
                                    //TODO middle name comment as per client wants only user name field on the register page
                                  /*  if (middlenamevatflag) {
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
                                    }*/
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
                                    if (dobflag) {
                                        if (dob.getText().toString().isEmpty()) {
                                            flag = false;
                                            dob.setError(getResources().getString(R.string.selectdob));
                                            dob.requestFocus();
                                        } else {
                                            dob.setError(null);
                                            flag = true;
                                            String[] parts = dob.getText().toString().split("/");
                                            String year = String.valueOf(Integer.parseInt(parts[2]));
                                            String month = String.valueOf(Integer.parseInt(parts[1]));
                                            String day = String.valueOf(Integer.parseInt(parts[0]));
                                            if (month.length() < 2) {
                                                month = "0" + month;
                                            }
                                            if (day.length() < 2) {
                                                day = "0" + day;
                                            }
                                            hashMap.addProperty(dob.getTag().toString(), month + "/" + day + "/" + year);
                                        }

                                    }
                                    if (taxvatflag) {
                                            /*if (flag) {
                                                hashMap.addProperty(taxvatname.getText().toString(), MageNative_taxvat.getText().toString());
                                            }*/
                                        if (MageNative_taxvat.getText().toString().isEmpty()) {
                                            flag = false;
                                            MageNative_taxvat.setError(getResources().getString(R.string.empty));
                                            MageNative_taxvat.requestFocus();
                                        } else {
                                            MageNative_taxvat.setError(null);
                                            flag = true;
                                            hashMap.addProperty(MageNative_taxvat.getTag().toString(), MageNative_taxvat.getText().toString());
                                        }
                                    }
                                    if (mobileFlag) {
                                        if (registerBinding.edtMobile.getText().toString().isEmpty()) {
                                            flag = false;
                                            registerBinding.edtMobile.setError(getResources().getString(R.string.empty));
                                            registerBinding.edtMobile.requestFocus();
                                        } else {
                                            registerBinding.edtMobile.setError(null);
                                            flag = true;
                                            mobileNumber = Objects.requireNonNull(registerBinding.edtMobile.getText()).toString();
                                            extension_attributes_object_register.addProperty("mobile", registerBinding.edtMobile.getText().toString());
                                            Log.i("COUNTRY_ID", "country_id: " + countryID);
                                            extension_attributes_object_register.addProperty("country_id", countryID);
                                        }
                                    }
                                    //TODO there we are getting username and split it and make first name and last name
                                    String[] nameArray = registerBinding1.registerLayout.edtUserName.getText().toString().trim().split(" ");
                                    firstname = nameArray[0];
                                    try {
                                        if (nameArray[1] != null) {
                                            lastname = nameArray[1];
                                            if (nameArray[2] != null) {
                                                lastname = lastname + " " + nameArray[2];
                                            }
                                        }
                                    } catch (Exception e) {
//                                       e.printStackTrace();
                                    }
                                    hashMap.addProperty("firstname", firstname);
                                    hashMap.addProperty("lastname", lastname);

                                    hashMap.addProperty("email", email);
                                    hashMap.addProperty("password", password);
                                    if (male) {
                                        hashMap.addProperty("gender", "1");
                                        session.savegender("Male");
                                    } else if (female) {
                                        hashMap.addProperty("gender", "2");
                                        session.savegender("Female");
                                    } else  {
                                        hashMap.addProperty("gender", "3");
                                        session.savegender("Guest");
                                    }

                                    if (cedSessionManagement.getStoreId() != null) {
                                        hashMap.addProperty("store_id", cedSessionManagement.getStoreId());
                                    }
                                        /* if (management.getCartId() != null)
                    {
                        hashMap.put("cart_id", management.getCartId());
                    }*/
                                    if (newsletter.isChecked()) {
                                        hashMap.addProperty("is_subscribed", "1");
                                    } else {
                                        hashMap.addProperty("is_subscribed", "0");
                                    }
                                    if (flag) {
                                        extension_attributes_object_register.addProperty("sub_seller", schoolCode);
                                        hashMap.add("extension_attributes", extension_attributes_object_register);
                                        Log.e("SUNDRAM", "onCreate: " + hashMap);
//                                            registerViewModel.getRegisterData(Ced_Register.this, hashMap)
//                                                    .observe(Ced_Register.this, Ced_Register.this::consumeRegisterResponse);
                                        if (mobileFlag) {
                                            Intent otp_intent = new Intent(Ced_Register.this, OTPActivity.class);
                                            otp_intent.putExtra("mobile_number", Objects.requireNonNull(registerBinding.edtMobile.getText()).toString().trim());
                                            otp_intent.putExtra("origin", "fromOTPRegister");
                                            otp_intent.putExtra("hashMap", hashMap.toString());
                                            startActivityIfNeeded(otp_intent, 1007);
                                            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
//                                            ValidateMobileNumber("fromOTPRegister", registerBinding.edtMobile.getText().toString().trim(), hashMap);
                                        } else {
                                            finalRegisterUserToBackend(hashMap);
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        account.setOnClickListener(v -> {
            Intent Loginpage = new Intent(getApplicationContext(), Ced_Login.class);
            startActivity(Loginpage);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        });

        get_the_fields();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1007) {
                if (Objects.requireNonNull(data).hasExtra("data")) {
                    JsonObject hashMap = new GsonBuilder().create().fromJson(data.getStringExtra("data"), JsonObject.class);
                    finalRegisterUserToBackend(hashMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createViewAndBindDataForSchoolList() {
        try {
            registerBinding1.schoolListBottomSheet.parentViewForSchoolData.removeAllViews();
            // using for-each loop for iteration over Map.entrySet()
            for (Map.Entry<String, String> entry : school_code_hash_map.entrySet()) {
                TextView textView = new TextView(this);
                textView.setTag(entry.getKey());
                textView.setText(entry.getValue());
                textView.setPadding(8, 15, 8, 15);
                textView.setTextColor(getResources().getColor(R.color.black));
                if (schoolCode != null && schoolCode.length() > 0) {
                    if (entry.getKey().equalsIgnoreCase(schoolCode)) {
                        set_bold_font_fortext(textView);
                        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.ic_baseline_check_24), null);
                        textView.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_variant_figma)));
                        registerBinding.MageNativeSchoolList.setText(entry.getValue());
                    } else {
                        set_regular_font_fortext(textView);
                    }
                } else {
                    set_regular_font_fortext(textView);
                }

                textView.setOnClickListener(view -> {
                    registerBinding.MageNativeSchoolList.setText(entry.getValue());
                    textView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.ic_baseline_check_24), null);
                    textView.setCompoundDrawableTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_variant_figma)));
                    Log.e("SUNDRAM", "SCHOOL ID: " + textView.getTag().toString());
                    Log.e("SUNDRAM", "SCHOOL NAME: " + textView.getText().toString());
                    schoolCode = textView.getTag().toString();
                    schoolName = textView.getText().toString();
                    createViewAndBindDataForSchoolList();
                });
                registerBinding1.schoolListBottomSheet.parentViewForSchoolData.addView(textView);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void finalRegisterUserToBackend(JsonObject hashMap) {
        registerViewModel.getRegisterData(Ced_Register.this, hashMap)
                .observe(Ced_Register.this, Ced_Register.this::consumeRegisterResponse);
    }


    private void consumeRegisterResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                register(apiResponse.data);
                break;

            case ERROR:
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }

    private void checklogin() {
        try {
            JSONObject jsonObj = new JSONObject(outputstring);
            if (jsonObj.has("header") && jsonObj.getString("header").equals("false")) {
                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                JSONArray response = jsonObj.getJSONObject(KEY_OBJECT).getJSONArray(KEY_CUSTOMER);
                for (int i = 0; i < response.length(); i++) {
                    JSONObject c = null;
                    c = response.getJSONObject(i);
                    status = c.getString(KEY_STATUS);
                    if (status.equals("success")) {
                        customer_id = c.getString(KEY_CUSTOMER_ID);
                        customergroup_id = c.getString(KEY_CUSTOMERGROUP_ID);
                        hash = c.getString(KEY_HASH);
                        session.savegender(c.getString("gender"));
                        session.savename(c.getString("name"));
                        if (c.has("mobile")) {
                            session.saveUserMobile(c.getString("mobile"));
                        }
                        cart_summary = String.valueOf(c.getInt(KEY_CART_SUMMARY));
                        Ced_MainActivity.latestcartcount = cart_summary;
                        showmsg(getResources().getString(R.string.succesfullLogin));
                        session.createLoginSession(hash, email);
                        session.saveCustomerId(customer_id);
                        session.set_customergroup_id(customergroup_id);
                        if (isfromcheckout) {
                            cedhandlecheckout();
                        } else {
                            Intent homeintent = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                            homeintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(homeintent);
                            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        }
                        setdevice_withusermail();
                    } else if (status.equals("exception")) {
                        message = c.getString(KEY_Message);
                        showmsg(message);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    private void consumeLoginResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {
            case SUCCESS:
                outputstring = apiResponse.data;
                checklogin();
                break;

            case ERROR:
                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                showmsg(getResources().getString(R.string.errorString));
                break;
        }
    }

    private void get_the_fields() {
        registerViewModel.getFieldsData(Ced_Register.this).observe(Ced_Register.this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                    try {
                        JSONObject object = new JSONObject(Objects.requireNonNull(apiResponse.data));
                        String success = object.getString("success");
                        if (success.equals("true")) {
                            JSONArray data = object.getJSONArray("data");
                            if (object.has("school_list")) {
                                JSONArray schoolList = object.getJSONArray("school_list");
                                school_name = new ArrayList<>();
                                school_code = new ArrayList<>();
                                school_code_hash_map = new LinkedHashMap<>();
                                for (int i = 0; i < schoolList.length(); i++) {
                                    school_name.add(schoolList.getJSONObject(i).getString("label"));
                                    school_code.add(schoolList.getJSONObject(i).getString("value"));
                                    //TODO school id as a value and school name as a label
                                    school_code_hash_map.put(schoolList.getJSONObject(i).getString("value"), schoolList.getJSONObject(i).getString("label"));
                                }
                            }
                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject jsonObject = data.getJSONObject(i);
                                    if (jsonObject.has("prefix")) {
                                        if (jsonObject.getString("prefix").equals("true")) {
                                            prefixflag = true;
                                            prefixsection.setVisibility(View.VISIBLE);
                                            prefixlabel.setText(jsonObject.getString("label"));
                                            prefixname.setText(jsonObject.getString("name"));
                                            RadioGroup ll = new RadioGroup(Ced_Register.this);
                                            ll.setOrientation(LinearLayout.VERTICAL);
                                            if (jsonObject.get("prefix_options") instanceof JSONObject) {
                                                JSONObject prefix_options = jsonObject.getJSONObject("prefix_options");
                                                prefixoptions.setText(prefix_options.toString());
                                                if (Objects.requireNonNull(prefix_options.names()).length() > 0) {
                                                    for (int j = 0; j < Objects.requireNonNull(prefix_options.names()).length(); j++) {
                                                        final RadioButton rdbtn = new RadioButton(Ced_Register.this);
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
                                            }
                                            prefix.addView(ll);
                                        }
                                    }
                                    if (jsonObject.has("dob")) {
                                        if (jsonObject.getString("dob").equals("true")) {
                                            dobflag = true;
                                            dobsection.setVisibility(View.VISIBLE);
                                            dob.setHint(jsonObject.getString("label") + "*");
                                            dob.setTag(jsonObject.getString("name"));
                                        }
                                    }
                                    if (jsonObject.has("taxvat")) {
                                        if (jsonObject.getString("taxvat").equals("true")) {
                                            taxvatflag = true;
                                            taxvatsection.setVisibility(View.VISIBLE);
                                            MageNative_taxvat.setHint(jsonObject.getString("label") + "*");
                                            MageNative_taxvat.setTag(jsonObject.getString("name"));
                                        }
                                    }
                                    if (jsonObject.has("mobile")) {
                                        if (jsonObject.getString("mobile").equals("true")) {
                                            mobileFlag = true;

                                            registerBinding.mobileLayout.setVisibility(View.VISIBLE);
                                            registerBinding.edtMobile.setHint(jsonObject.getString("label") + "*");
                                            registerBinding.edtMobile.setTag(jsonObject.getString("name"));
                                        }
                                    }
                                    //TODO middle name code commented as per the client do not want the first , middle and last name on the register page
//                                    if (jsonObject.has("middlename")) {
//                                        if (jsonObject.getString("middlename").equals("true")) {
//                                            middlenamevatflag = true;
////                                            middlenamesection.setVisibility(View.VISIBLE);
//                                            MageNative_midllename.setHint(jsonObject.getString("label") + "*");
//                                            MageNative_midllename.setTag(jsonObject.getString("name"));
//                                        }
//                                    }
                                    if (jsonObject.has("suffix")) {
                                        if (jsonObject.getString("suffix").equals("true")) {
                                            suffixflag = true;
                                            suffixsection.setVisibility(View.VISIBLE);
                                            suffixlabel.setText(jsonObject.getString("label"));
                                            suffixname.setText(jsonObject.getString("name"));
                                            RadioGroup ll = new RadioGroup(Ced_Register.this);
                                            ll.setOrientation(LinearLayout.VERTICAL);
                                            JSONObject suffixx_options = jsonObject.getJSONObject("suffix_options");
                                            suffixoptions.setText(suffixx_options.toString());
                                            if (Objects.requireNonNull(suffixx_options.names()).length() > 0) {
                                                for (int j = 0; j < Objects.requireNonNull(suffixx_options.names()).length(); j++) {
                                                    final RadioButton rdbtn = new RadioButton(Ced_Register.this);
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

        /*//TODO
        Ced_ClientRequestResponseRest requestResponse = new Ced_ClientRequestResponseRest(output -> {

        }, Ced_Register.this);
        requestResponse.execute(requiredfieldurl);*/
    }

    private void register(String output) {
        try {
            JSONObject jsonObj = new JSONObject(output);
            if (jsonObj.has("header") && jsonObj.getString("header").equals("false")) {
                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                JSONArray response = jsonObj.getJSONObject(KEY_OBJECT).getJSONArray(KEY_CUSTOMER);
                for (int i = 0; i < response.length(); i++) {
                    JSONObject c = response.getJSONObject(i);
                    status = c.getString(KEY_STATUS);
                    message = c.getString(KEY_MESSAGE);
                    if (status.equals("success")) {
                        isConfirmationRequired = c.getString(KEY_IS_CONFIRMATION_REQUIRED);
                        if (isConfirmationRequired.equals("NO")) {
                            JsonObject hashMap = new JsonObject();
                            hashMap.addProperty("email", email);

                            JsonObject extension_attributes_object_logintype = new JsonObject();
                            extension_attributes_object_logintype.addProperty("login_type", "email");

                            hashMap.add("extension_attributes", extension_attributes_object_logintype);
                            hashMap.addProperty("password", password);
                            if (cedSessionManagement.getCartId() != null) {
                                hashMap.addProperty("cart_id", cedSessionManagement.getCartId());
                            }
                            try {
                                registerViewModel.getLoginData(Ced_Register.this, hashMap)
                                        .observe(Ced_Register.this, this::consumeLoginResponse);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
                                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(main);
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            }
                        } else {
                            showmsg(message);
                            Intent popActivity = new Intent(getApplicationContext(), Ced_Login.class);
                            popActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            popActivity.putExtra("isHavingdownloadable", true);
                            popActivity.putExtra("Checkout", "CheckoutModule");
                            startActivity(popActivity);
                            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        }
                    } else {
                        showmsg(message);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Intent main = new Intent(getApplicationContext(), Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(main);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    @Override
    public void onBackPressed() {
        invalidateOptionsMenu();
        super.onBackPressed();
    }

    private String generateAlphaNumericString(int length) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, length);
    }
}
