package com.akshar.store.ui.loginsection.activity;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.EventLog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.base.viewmodel.NavDrawerViewModel;
import com.akshar.store.databinding.ActivityOtpactivityBinding;
import com.akshar.store.rest.ApiResponse;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.razorpay.OTP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OTPActivity extends Ced_NavigationActivity implements TextWatcher {
    NavDrawerViewModel drawerViewModel1;
    ActivityOtpactivityBinding activityOtpactivityBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    String origin;
    String mobile_no;
    //Declare timer
    CountDownTimer cTimer = null;
    String hashMap_str;
    JsonObject hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackButtonWithTitle("OTP Verification");
        hidebottombar();
        activityOtpactivityBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_otpactivity, content, true);
        drawerViewModel1 = new ViewModelProvider(OTPActivity.this, viewModelFactory).get(NavDrawerViewModel.class);
        if (getIntent().hasExtra("mobile_number")) {
            activityOtpactivityBinding.mobileTxt.setText(getIntent().getStringExtra("mobile_number"));
            mobile_no = getIntent().getStringExtra("mobile_number");
        }
        if (getIntent().hasExtra("origin")) {
            origin = getIntent().getStringExtra("origin");
        }

        if (getIntent().hasExtra("hashMap")) {
            hashMap_str = getIntent().getStringExtra("hashMap");
            hashMap = new GsonBuilder().create().fromJson(hashMap, JsonObject.class);
        }

        JsonObject hashMap = new JsonObject();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mobile", mobile_no);
        jsonObject.addProperty("login_type", "otp");
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        jsonObject.addProperty("country_id", countryID);
        hashMap.add("extension_attributes", jsonObject);
        if (cedSessionManagement.getStoreId() != null) {
            hashMap.addProperty("store_id", cedSessionManagement.getStoreId());
        }
        if (cedSessionManagement.getCartId() != null) {
            hashMap.addProperty("cart_id", cedSessionManagement.getCartId());
        }
        activityOtpactivityBinding.resendOtpTxt.setOnClickListener(view -> {
            try {
                if (activityOtpactivityBinding.otpTimerTxt.getText().equals("00")) {
                    sendOTP(mobile_no, origin, hashMap);
                    startTimer();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
//        sendOTPNEW(mobile_no, origin, hashMap);
        ValidateMobileNumberNew(origin, mobile_no, hashMap);
//        startTimer();
        activityOtpactivityBinding.one.requestFocus();
        activityOtpactivityBinding.btnRegister.setOnClickListener(view -> {
            try {
                String code = Objects.requireNonNull(activityOtpactivityBinding.one.getText()).toString().trim()
                        + Objects.requireNonNull(activityOtpactivityBinding.two.getText()).toString().trim()
                        + Objects.requireNonNull(activityOtpactivityBinding.three.getText()).toString().trim()
                        + Objects.requireNonNull(activityOtpactivityBinding.four.getText()).toString().trim();
                if (activityOtpactivityBinding.one.getText().toString().length() <= 0) {
                    activityOtpactivityBinding.one.requestFocus();
                } else if (activityOtpactivityBinding.two.getText().toString().length() <= 0) {
                    activityOtpactivityBinding.two.requestFocus();
                } else if (activityOtpactivityBinding.three.getText().toString().length() <= 0) {
                    activityOtpactivityBinding.three.requestFocus();
                } else if (activityOtpactivityBinding.four.getText().toString().length() <= 0) {
                    activityOtpactivityBinding.four.requestFocus();
                } else {
                    Log.i("REpo", "code " + code);
                    JsonObject object = new JsonObject();
                    object.addProperty("mobile", mobile_no);
                    object.addProperty("country_id", countryID);
                    object.addProperty("otp", code);
                    object.addProperty("type", origin);

                    drawerViewModel.verifyOTP(OTPActivity.this, object)
                            .observe(OTPActivity.this, apiResponse -> {
                                switch (apiResponse.status) {
                                    case SUCCESS:
                                        try {
                                            JSONObject object1 = new JSONObject(Objects.requireNonNull(apiResponse.data));
                                            JSONObject data = object1.getJSONObject("data");
                                            if (data.has("customer")) {
                                                JSONArray customer = data.getJSONArray("customer");
                                                if (customer.getJSONObject(0).getString("status").equalsIgnoreCase("true")) {
                                                    if (origin.equalsIgnoreCase("fromOTPRegister")) {
                                                        Intent intent = new Intent();
                                                        intent.putExtra("data", hashMap_str);
                                                        setResult(1007, intent);
                                                        finish();//finishing activity
                                                    } else if (origin.equalsIgnoreCase("fromOTPlogin")) {
                                                        Intent intent = new Intent();
                                                        intent.putExtra("data", hashMap_str);
                                                        setResult(1008, intent);
                                                        finish();//finishing activity
                                                    } else if (origin.equalsIgnoreCase("update")) {
                                                        Intent intent = new Intent();
                                                        intent.putExtra("data", hashMap_str);
                                                        setResult(1009, intent);
                                                        finish();//finishing activity
                                                    }
                                                } else {
                                                    if (customer.getJSONObject(0).has("message")) {
                                                        showmsg(customer.getJSONObject(0).getString("message"));
                                                        activityOtpactivityBinding.one.setText("");
                                                        activityOtpactivityBinding.two.setText("");
                                                        activityOtpactivityBinding.three.setText("");
                                                        activityOtpactivityBinding.four.setText("");
                                                    }
                                                }
                                            } else {
                                                if (data.has("message")) {
                                                    activityOtpactivityBinding.one.setText("");
                                                    activityOtpactivityBinding.two.setText("");
                                                    activityOtpactivityBinding.three.setText("");
                                                    activityOtpactivityBinding.four.setText("");
                                                    showmsg(data.getString("message"));
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        setUpOTPBoxForFocusChanges();
    }

    private void setUpOTPBoxForFocusChanges() {
        activityOtpactivityBinding.one.setOnKeyListener((View.OnKeyListener) (v, keyCode, keyEvent) -> {
            if (keyCode != KeyEvent.KEYCODE_DEL) {
                if (Objects.requireNonNull(((AppCompatEditText) v).getText()).toString().length() > 0
                        && Objects.requireNonNull(activityOtpactivityBinding.two.getText()).toString().length() == 0) {
                    activityOtpactivityBinding.two.requestFocus();
                }
            } else {
//TODO
            }
            return false;
        });
        activityOtpactivityBinding.two.setOnKeyListener((View.OnKeyListener) (v, keyCode, keyEvent) -> {
            if (keyCode != KeyEvent.KEYCODE_DEL) {
                if (Objects.requireNonNull(((AppCompatEditText) v).getText()).toString().length() > 0
                        && Objects.requireNonNull(activityOtpactivityBinding.three.getText()).toString().length() == 0)
                    activityOtpactivityBinding.three.requestFocus();
            }else if(Objects.requireNonNull(((AppCompatEditText) v).getText()).toString().length() == 0){
                activityOtpactivityBinding.one.requestFocus();
            }
            return false;
        });
        activityOtpactivityBinding.three.setOnKeyListener((View.OnKeyListener) (v, keyCode, keyEvent) -> {
            if (keyCode != KeyEvent.KEYCODE_DEL) {
                if (Objects.requireNonNull(((AppCompatEditText) v).getText()).toString().length() > 0
                        && Objects.requireNonNull(activityOtpactivityBinding.four.getText()).toString().length() == 0)
                    activityOtpactivityBinding.four.requestFocus();
            }else if(Objects.requireNonNull(((AppCompatEditText) v).getText()).toString().length() == 0){
                activityOtpactivityBinding.two.requestFocus();
            }
            return false;
        });
        activityOtpactivityBinding.four.setOnKeyListener((View.OnKeyListener) (v, keyCode, keyEvent) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL)
                activityOtpactivityBinding.three.requestFocus();
            return false;
        });
    }

    public void ValidateMobileNumberNew(String origin, String mobile, JsonObject hashMap) {
        JsonObject obj = new JsonObject();
        if (origin.equalsIgnoreCase("fromOTPRegister")) {
            obj.addProperty("type", "register");
        } else if (origin.equalsIgnoreCase("fromOTPlogin")) {
            obj.addProperty("type", "login");
        } else if (origin.equalsIgnoreCase("update")) {
            obj.addProperty("type", "update");
            obj.addProperty("customer_id", session.getCustomerid());
        }
        obj.addProperty("country_id", countryID);
        obj.addProperty("mobile", mobile);
        drawerViewModel.validateMobileNumber(OTPActivity.this, obj).observe(OTPActivity.this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                    try {
                        assert apiResponse.data != null;
                        JSONObject object = new JSONObject(apiResponse.data);
                        JSONObject data = object.getJSONObject("data");
                        if (data.has("customer")) {
                            JSONArray customer = data.getJSONArray("customer");
                            if (customer.getJSONObject(0).getString("status").equalsIgnoreCase("true")) {
                                activityOtpactivityBinding.main.setVisibility(View.VISIBLE);
                                sendOTPNEW(mobile, origin, hashMap);
                            } else {
                                if (customer.getJSONObject(0).has("message")) {
                                    showmsg(customer.getJSONObject(0).getString("message"));
                                    Intent RegistrationIntent = new Intent(getApplicationContext(), Ced_Register.class);
                                    if (getIntent().getStringExtra("Checkout") != null) {
                                        if (Objects.requireNonNull(getIntent().getStringExtra("Checkout")).equals("CheckoutModule")) {
                                            RegistrationIntent.putExtra("isfromcheckout", "true");
                                        }
                                    }
                                    startActivity(RegistrationIntent);
                                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                    finish();
                                }
                            }
                        } else {
                            if (data.has("message")) {
                                showmsg(data.getString("message"));
                            }
                        }
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
    }

    public void sendOTPNEW(String mobile, String source, JsonObject hashMap) {
        JsonObject object = new JsonObject();
        object.addProperty("mobile", mobile);
        object.addProperty("country_id", countryID);

        if (source.equalsIgnoreCase("fromOTPRegister")) {
            object.addProperty("type", "register");
        } else if (source.equalsIgnoreCase("login")) {
            object.addProperty("type", "login");
        } else if (source.equalsIgnoreCase("update")) { //this check is used when user updated profile with mobile
            object.addProperty("type", "update");
        }

        drawerViewModel1.sendOTP(OTPActivity.this, object).observe(OTPActivity.this, apiResponse -> {
            switch (apiResponse.status) {
                case SUCCESS:
                    try {
                        JSONObject object1 = new JSONObject(Objects.requireNonNull(apiResponse.data));
                        JSONObject data = object1.getJSONObject("data");
                        if (data.has("customer")) {
                            JSONArray customer = data.getJSONArray("customer");
                            if (customer.getJSONObject(0).getString("status").equalsIgnoreCase("true")) {
                                activityOtpactivityBinding.one.setEnabled(true);
                                activityOtpactivityBinding.two.setEnabled(true);
                                activityOtpactivityBinding.three.setEnabled(true);
                                activityOtpactivityBinding.four.setEnabled(true);
                                if (customer.getJSONObject(0).has("message")) {
                                    showmsg(customer.getJSONObject(0).getString("message"));
                                    startTimer();
                                }
                            } else {
                                if (customer.getJSONObject(0).has("message")) {
                                    showmsg(customer.getJSONObject(0).getString("message"));
                                }
                            }
                        } else {
                            if (data.has("message")) {
                                showmsg(data.getString("message"));
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

    //start timer function
    void startTimer() {
        cTimer = new CountDownTimer(30000, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                activityOtpactivityBinding.otpTimerTxt.setText("(" + String.format(Locale.ENGLISH, "%02d S",
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))) + ")");
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                activityOtpactivityBinding.otpTimerTxt.setText("00");
                cancelTimer();
            }
        };
        cTimer.start();
    }

    //cancel timer
    void cancelTimer() {
        if (cTimer != null)
            cTimer.cancel();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}