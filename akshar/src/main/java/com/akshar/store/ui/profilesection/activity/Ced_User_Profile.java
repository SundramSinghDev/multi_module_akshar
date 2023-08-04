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

package com.akshar.store.ui.profilesection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.akshar.store.ui.loginsection.activity.Ced_Register;
import com.akshar.store.ui.loginsection.activity.OTPActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.akshar.store.databinding.MagenativeNewProfilePageBinding;
import com.akshar.store.ui.loginsection.activity.Ced_Login;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;

import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.R;
import com.google.android.gms.analytics.Tracker;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.akshar.store.ui.profilesection.viewmodel.ProfileViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static com.akshar.store.Ced_MageNative_SharedPrefrence.Ced_SessionManagement_login.Key_Email;

/**
 * Created by developer on 9/22/2015.
 */
@AndroidEntryPoint
public class Ced_User_Profile extends Ced_NavigationActivity {
    MagenativeNewProfilePageBinding profileBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    ProfileViewModel profileViewModel;
    EditText edt_first_name, edt_last_name, edt_email, edt_confirm_pass, edt_old_pass, edt_new_pass;
    AppCompatButton update_profile;
    boolean Male = true;
    boolean Female = false;
    JsonObject hashMap;
    static final String KEY_OBJECT = "data";
    static final String KEY_ITEM = "customer";
    static final String KEY_MESSAGE = "message";
    static final String KEY_STATUS = "status";
    private Tracker mTracker;

    ImageView male, female;
    LinearLayout prefixsection;
    TextInputLayout middlenamesection;
    LinearLayout suffixsection;
    LinearLayout dobsection;
    TextInputLayout taxvatsection;
    TextView prefixlabel;
    TextView suffixlabel;
    String requiredfieldurl = "";
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
    TextView dob;
    boolean flag = true;
    EditText MageNative_midllename;
    EditText MageNative_taxvat;
    private List<String> school_name;
    private List<String> school_code;
    private String schoolCode = "";
    private String schoolName;
    String sudSellerId = "";
    String sellerId;
    private int pos = -1;
    boolean mobileFlag = true;
    boolean isMobileChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = new ViewModelProvider(Ced_User_Profile.this, viewModelFactory).get(ProfileViewModel.class);
        hashMap = new JsonObject();
        requiredfieldurl = Urls.BASE_URL + Urls.GETREQUIREDFIELDS;
        if (session.isLoggedIn()) {
            profileBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_new_profile_page, content, true);
            profileBinding.edtMobile.setText(session.getUserMobile());
            showbackbutton();
            showtootltext(getResources().getString(R.string.EditProfile));
            if (getIntent().hasExtra("seller_id") && getIntent().getStringExtra("seller_id") != null) {
                sellerId = getIntent().getStringExtra("seller_id");
            }
            if (getIntent().hasExtra("sub_seller_id") && getIntent().getStringExtra("sub_seller_id") != null) {
                sudSellerId = getIntent().getStringExtra("sub_seller_id");
            }
            update_profile = profileBinding.btnSubmit;
            edt_first_name = profileBinding.edtFirstName;
            edt_last_name = profileBinding.edtLastName;
            edt_confirm_pass = profileBinding.edtConfirmPass;
            edt_email = profileBinding.edtEmail;
            edt_old_pass = profileBinding.edtOldPass;
            edt_new_pass = profileBinding.edtNewPass;
            male = profileBinding.imgMale;
            female = profileBinding.imgFemale;
            prefixsection = profileBinding.prefixsection;
            middlenamesection = profileBinding.middlenamesection;
            suffixsection = profileBinding.suffixsection;
            dobsection = profileBinding.dobsection;
            taxvatsection = profileBinding.taxvatsection;
            prefixlabel = profileBinding.prefixlabel;
            suffixlabel = profileBinding.suffixlabel;
            prefix = profileBinding.prefix;
            suffix = profileBinding.suffix;
            prefixname = profileBinding.prefixname;
            suffixname = profileBinding.suffixname;
            prefixoptions = profileBinding.prefixoptions;
            suffixoptions = profileBinding.suffixoptions;
            dob = profileBinding.dob;
            MageNative_midllename = profileBinding.MageNativeMidllename;
            MageNative_taxvat = profileBinding.MageNativeTaxvat;
            requiredfieldurl = requiredfieldurl + session.getCustomerid();

            edt_email.setText(session.getUserDetails().get(Key_Email));
            edt_email.setKeyListener(null);
            profileBinding.mobileLayout.setVisibility(View.VISIBLE);
            profileBinding.layoutEmail.setVisibility(View.VISIBLE);
            profileBinding.layoutConfirmPass.setVisibility(View.VISIBLE);
            profileBinding.layoutOldPass.setVisibility(View.VISIBLE);
            profileBinding.layoutNewPass.setVisibility(View.VISIBLE);

            profileBinding.changeMobileNumberCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    profileBinding.mobileLayout.setVisibility(View.VISIBLE);
                } else {
                    profileBinding.mobileLayout.setVisibility(View.GONE);
                }
            });

            profileBinding.changeEmailCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    profileBinding.layoutEmail.setVisibility(View.VISIBLE);
                    profileBinding.layoutConfirmPass.setVisibility(View.VISIBLE);
                } else {
                    profileBinding.layoutEmail.setVisibility(View.GONE);
                    profileBinding.layoutConfirmPass.setVisibility(View.GONE);
                }
            });

            profileBinding.changePassWord.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    profileBinding.layoutConfirmPass.setVisibility(View.VISIBLE);
                    profileBinding.layoutOldPass.setVisibility(View.VISIBLE);
                    profileBinding.layoutNewPass.setVisibility(View.VISIBLE);
                } else {
                    profileBinding.layoutConfirmPass.setVisibility(View.GONE);
                    profileBinding.layoutOldPass.setVisibility(View.GONE);
                    profileBinding.layoutNewPass.setVisibility(View.GONE);
                }
            });

            get_the_fields();
            profileBinding.MageNativeSchoolList.setOnClickListener(v -> {
                try {
                    final CharSequence[] arrayOfInt = school_code.toArray(new CharSequence[school_code.size()]);
                    final CharSequence[] arrayOfInt2 = school_name.toArray(new CharSequence[school_name.size()]);

                    new MaterialAlertDialogBuilder(Ced_User_Profile.this, R.style.SingleChoiceRadioStyle)
                            .setTitle(Html.fromHtml("<font color='#000000'>" + getResources().getString(R.string.select_school) + "</font>"))
                            .setSingleChoiceItems(arrayOfInt2, pos, (dialog, postion) -> {
                                schoolCode = (String) arrayOfInt[postion];
                                schoolName = (String) arrayOfInt2[postion];
                                pos = postion;
                                profileBinding.MageNativeSchoolList.setText(schoolName);
                                Log.i("School", "onClick: School_NAME " + schoolName + "\n" + " School_Code " + schoolCode);
                                dialog.dismiss();
                            }).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            dob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDatePicker_DDMMYYYY(dob);
                }
            });

            male.setOnClickListener(v -> {
                Male = true;
                Female = false;
                male.setBackground(getResources().getDrawable(R.drawable.selected));
                female.setBackground(getResources().getDrawable(R.drawable.selectedwhite));
            });

            female.setOnClickListener(v -> {
                Female = true;
                Male = false;
                female.setBackground(getResources().getDrawable(R.drawable.selected));
                male.setBackground(getResources().getDrawable(R.drawable.selectedwhite));
            });

            update_profile.setOnClickListener(v -> {
                try {
                    JsonObject extension_attributes_object_register = new JsonObject();

                    hashMap.addProperty("email", session.getUserDetails().get(Key_Email));
                    hashMap.addProperty("customer_id", session.getCustomerid());
                    if (!edt_old_pass.getText().toString().isEmpty()) {
                        hashMap.addProperty("change_password", "1");
                        hashMap.addProperty("old_password", edt_old_pass.getText().toString());
                        hashMap.addProperty("new_password", edt_new_pass.getText().toString());
                        hashMap.addProperty("confirm_password", edt_confirm_pass.getText().toString());
                    } else {
                        hashMap.addProperty("change_password", "0");
                    }
                    hashMap.addProperty("firstname", edt_first_name.getText().toString());
                    hashMap.addProperty("lastname", edt_last_name.getText().toString());
                    session.savename(edt_first_name.getText().toString());
                    if (Male) {
                        hashMap.addProperty("gender", "1");
                        session.savegender("male");
                    } else {
                        if (Female) {
                            hashMap.addProperty("gender", "2");
                            session.savegender("female");
                        } else {
                            hashMap.addProperty("gender", "3");
                        }
                    }
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
                                hashMap.addProperty(profileBinding.MageNativeMidllename.getTag().toString(), profileBinding.MageNativeMidllename.getText().toString());
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
                        if (flag) {
                            if (MageNative_taxvat.getText().toString().isEmpty()) {
                                flag = false;
                                MageNative_taxvat.setError(getResources().getString(R.string.empty));
                                MageNative_taxvat.requestFocus();
                            } else {
                                flag = true;
                                hashMap.addProperty(profileBinding.MageNativeTaxvat.getTag().toString(), profileBinding.MageNativeTaxvat.getText().toString());
                            }
                        }
                    }
                    if (mobileFlag) {
                        if (profileBinding.edtMobile.getText().toString().isEmpty()) {
                            flag = false;
                            profileBinding.edtMobile.setError(getResources().getString(R.string.empty));
                            profileBinding.edtMobile.requestFocus();
                        } else {
                            profileBinding.edtMobile.setError(null);
                            flag = true;
                            if (!session.getUserMobile().equals(profileBinding.edtMobile.getText().toString().trim())) {
                                isMobileChanged = true;
                                extension_attributes_object_register.addProperty("mobile", profileBinding.edtMobile.getText().toString());
                                Log.i("COUNTRY_ID", "country_id: " + countryID);
                                extension_attributes_object_register.addProperty("country_id", countryID);
                            }
                        }
                    }
                    if (flag) {
                        extension_attributes_object_register.addProperty("sub_seller", schoolCode);
                        hashMap.add("extension_attributes", extension_attributes_object_register);
                        if (!isMobileChanged) {
                            updateProfile(hashMap);
                        } else {
                            Intent otp_intent = new Intent(Ced_User_Profile.this, OTPActivity.class);
                            otp_intent.putExtra("mobile_number", Objects.requireNonNull(profileBinding.edtMobile.getText()).toString().trim());
                            otp_intent.putExtra("origin", "update");
                            otp_intent.putExtra("hashMap", hashMap.toString());
                            startActivityIfNeeded(otp_intent, 1009);
                            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
//                            ValidateMobileNumber("update", profileBinding.edtMobile.getText().toString().trim(), hashMap);
                        }
                        /*//TODO
                        Ced_ClientRequestResponseRest cedClientRequestResponse = new Ced_ClientRequestResponseRest(output -> {
                            result = output.toString();
                            fetchdata();
                        }, Ced_User_Profile.this, "POST", hashMap.toString());
                        cedClientRequestResponse.execute(Url);*/
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

        } else {
            Intent goto_login = new Intent(getApplicationContext(), Ced_Login.class);
            goto_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            goto_login.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            finish();
            startActivity(goto_login);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1009) {
                if (Objects.requireNonNull(data).hasExtra("data")) {
                    JsonObject hashMap = new GsonBuilder().create().fromJson(data.getStringExtra("data"), JsonObject.class);
                    updateProfile(hashMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProfile(JsonObject params) {
        try {
            profileViewModel.getProfileUpdateData(Ced_User_Profile.this, params, session.getHahkey())
                    .observe(Ced_User_Profile.this, apiResponse -> {
                        switch (apiResponse.status) {
                            case SUCCESS:
                                fetchdata(Objects.requireNonNull(apiResponse.data));
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

    private void get_the_fields() {
        profileViewModel.getProfileFieldsData(this, requiredfieldurl).observe(this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                    String jstring = apiResponse.data;
                    try {
                        JSONObject object = new JSONObject(Objects.requireNonNull(jstring));
                        String success = object.getString("success");
                        if (success.equals("true")) {
                            if (object.has("school_list")) {
                                JSONArray schoolList = object.getJSONArray("school_list");
                                school_name = new ArrayList<>();
                                school_code = new ArrayList<>();
                                for (int i = 0; i < schoolList.length(); i++) {
                                    school_name.add(schoolList.getJSONObject(i).getString("label"));
                                    school_code.add(schoolList.getJSONObject(i).getString("value"));
                                }
                            }
                            if (sudSellerId.length() > 0 && school_code.contains(sudSellerId)) {
                                pos = school_code.indexOf(sudSellerId);
                                schoolName = school_name.get(pos);
                                profileBinding.MageNativeSchoolList.setText(schoolName);
                            } else if (object.has("selected_school") && school_code.contains(schoolCode)) {
                                schoolCode = object.getString("selected_school");
                                pos = school_code.indexOf(schoolCode);
                                try {
                                    schoolName = school_name.get(pos);
                                    profileBinding.MageNativeSchoolList.setText(schoolName);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                session.saveSubSellerId(schoolCode);
                            } else {
                                session.clearSellerId();
                            }
                            if (object.has("lastname")) {
                                edt_last_name.setText(object.getString("lastname"));
                            }
                            if (object.has("firstname")) {
                                edt_first_name.setText(object.getString("firstname"));
                            }
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
                                            RadioGroup ll = new RadioGroup(Ced_User_Profile.this);
                                            ll.setOrientation(LinearLayout.VERTICAL);
                                            JSONObject prefix_options = jsonObject.getJSONObject("prefix_options");
                                            prefixoptions.setText(prefix_options.toString());
                                            final JSONObject object1 = new JSONObject(prefixoptions.getText().toString());
                                            if (Objects.requireNonNull(prefix_options.names()).length() > 0) {
                                                for (int j = 0; j < Objects.requireNonNull(prefix_options.names()).length(); j++) {
                                                    final RadioButton rdbtn = new RadioButton(Ced_User_Profile.this);
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
                                                    if (jsonObject.getString("value").equals(object1.getString(rdbtn.getText().toString()))) {
                                                        rdbtn.setChecked(true);
                                                    }
                                                    ll.addView(rdbtn);
                                                }
                                            }
                                            prefix.addView(ll);
                                        }
                                    }
                                    if (jsonObject.has("gender")) {
                                        if (jsonObject.getString("gender").equals("true")) {
                                            if (jsonObject.getString("value").equals("1")) {
                                                Male = true;
                                                Female = false;
                                                male.setBackground(getResources().getDrawable(R.drawable.selected));
                                                female.setBackground(getResources().getDrawable(R.drawable.selectedwhite));
                                            }
                                            if (jsonObject.getString("value").equals("2")) {
                                                Male = false;
                                                Female = true;
                                                male.setBackground(getResources().getDrawable(R.drawable.selectedwhite));
                                                female.setBackground(getResources().getDrawable(R.drawable.selected));
                                            }
                                            if (jsonObject.getString("value").equals("3")) {
                                                Male = false;
                                                Female = false;
                                                male.setBackground(getResources().getDrawable(R.drawable.selectedwhite));
                                                female.setBackground(getResources().getDrawable(R.drawable.selectedwhite));
                                            }
                                        }
                                    }
                                    if (jsonObject.has("dob")) {
                                        if (jsonObject.getString("dob").equals("true")) {
                                            dobflag = true;
                                            dobsection.setVisibility(View.VISIBLE);
                                            dob.setHint(jsonObject.getString("label") + "*");
                                            dob.setTag(jsonObject.getString("name"));
                                            if (!jsonObject.getString("value").isEmpty() && !jsonObject.getString("value").equalsIgnoreCase("null")) {
                                                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                                Date selecteddate = (originalFormat.parse(jsonObject.getString("value")));
                                                SimpleDateFormat newformat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                                dob.setText(newformat.format(selecteddate));
                                                /*dob.setText(jsonObject.getString("value"));*/
                                            }
                                        }
                                    }
                                    if (jsonObject.has("taxvat")) {
                                        if (jsonObject.getString("taxvat").equals("true")) {
                                            taxvatflag = true;
                                            taxvatsection.setVisibility(View.VISIBLE);
                                            MageNative_taxvat.setHint(jsonObject.getString("label") + "*");
                                            MageNative_taxvat.setTag(jsonObject.getString("name"));
                                            if (!jsonObject.getString("value").isEmpty() && !jsonObject.getString("value").equalsIgnoreCase("null")) {
                                                MageNative_taxvat.setText(jsonObject.getString("value"));
                                            }
                                        }
                                    }
                                    if (jsonObject.has("mobile")) {
                                        if (jsonObject.getString("mobile").equals("true")) {
                                            mobileFlag = true;
                                            profileBinding.mobileLayout.setVisibility(View.VISIBLE);
                                            profileBinding.edtMobile.setHint(jsonObject.getString("label") + "*");
                                            profileBinding.edtMobile.setTag(jsonObject.getString("name"));
                                        }
                                    }
                                    if (jsonObject.has("middlename")) {
                                        if (jsonObject.getString("middlename").equals("true")) {
                                            middlenamevatflag = true;
                                            middlenamesection.setVisibility(View.VISIBLE);
                                            MageNative_midllename.setHint(jsonObject.getString("label") + "*");
                                            MageNative_midllename.setTag(jsonObject.getString("name"));
                                            if (!jsonObject.getString("value").isEmpty() && !jsonObject.getString("value").equalsIgnoreCase("null")) {
                                                MageNative_midllename.setText(jsonObject.getString("value"));
                                            }
                                        }
                                    }
                                    if (jsonObject.has("suffix")) {
                                        if (jsonObject.getString("suffix").equals("true")) {
                                            suffixflag = true;
                                            suffixsection.setVisibility(View.VISIBLE);
                                            suffixlabel.setText(jsonObject.getString("label"));
                                            suffixname.setText(jsonObject.getString("name"));
                                            RadioGroup ll = new RadioGroup(Ced_User_Profile.this);
                                            ll.setOrientation(LinearLayout.VERTICAL);
                                            JSONObject suffixx_options = jsonObject.getJSONObject("suffix_options");
                                            suffixoptions.setText(suffixx_options.toString());
                                            final JSONObject object1 = new JSONObject(suffixoptions.getText().toString());
                                            if (Objects.requireNonNull(suffixx_options.names()).length() > 0) {
                                                for (int j = 0; j < Objects.requireNonNull(suffixx_options.names()).length(); j++) {
                                                    final RadioButton rdbtn = new RadioButton(Ced_User_Profile.this);
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
                                                    if (jsonObject.getString("value").equals(object1.getString(rdbtn.getText().toString()))) {
                                                        rdbtn.setChecked(true);
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

    /**
     * after profile update
     */
    private void fetchdata(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            if (jsonObj.has("header") && jsonObj.getString("header").equals("false")) {
                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                JSONArray response = jsonObj.getJSONObject(KEY_OBJECT).getJSONArray(KEY_ITEM);
                JSONObject c = response.getJSONObject(0);
                String status, message = null;
                status = c.getString(KEY_STATUS);
                if (status.equals("success")) {
                    if (schoolCode.length() > 0)
                        session.saveSubSellerId(schoolCode);
                    message = c.getString(KEY_MESSAGE);
                    showmsg(message);
                    if (message.equals("Profile Updated Successfully")) {
                        session.saveUserMobile(Objects.requireNonNull(profileBinding.edtMobile.getText()).toString().trim());
                        Intent home = new Intent(getApplicationContext(), Ced_User_Profile.class);
                        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(home);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    } else {
                        cleardataandlogout();

                        Intent home = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(home);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    }
                } else {
                    if (status.equals("error")) {
                        message = c.getString(KEY_MESSAGE);
                        showmsg(message);
                    } else {
                        if (status.equals("false")) {
                            message = c.getString(KEY_MESSAGE);
                            showmsg(message);
                        }
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

}