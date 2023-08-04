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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.akshar.store.databinding.MagenativeLoginPageDesignBinding;
import com.akshar.store.ui.loginsection.viewmodel.RegisterViewModel;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import com.akshar.store.databinding.MagenativeNewLoginLayoutBinding;
import com.akshar.store.rest.ApiResponse;
import com.akshar.store.ui.loginsection.viewmodel.LoginViewModel;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ced_Login extends Ced_NavigationActivity implements View.OnClickListener {
    //    MagenativeNewLoginLayoutBinding loginBinding;
    MagenativeLoginPageDesignBinding loginBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    LoginViewModel loginViewModel;
    private BottomSheetBehavior sheetBehavior;

    TextView Forgot_Password;
    EditText edt_username;
    EditText edt_password;
    JSONObject jsonObj;
    JSONArray response = null;
    Button Login_button;
    String social_url = "";
    String outputstring;
    String status, customer_id, hash, genders, email, firstname, lastname, id, customergroup_id;
    static final String KEY_OBJECT = "data";
    static final String KEY_STATUS = "status";
    static final String KEY_Message = "message";
    static final String KEY_CUSTOMER = "customer";
    static final String KEY_CUSTOMER_ID = "customer_id";
    static final String KEY_HASH = "hash";
    static final String KEY_CART_SUMMARY = "cart_summary";
    static final String KEY_CUSTOMERGROUP_ID = "customer_group";
    String cart_summary;
    String Url = "";
    String username, password;
    CallbackManager callbackManager;
    LoginButton loginButton;
    LinearLayout social_login_linear;
    LinearLayout social_login_google;
    private SignInButton signInButton;
    private int RC_SIGN_IN = 100;
    TextView signupwithustext;
    LinearLayout orsection;
    ImageView MageNative_mage;
    Animation fade_in, fade_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectaccounttab();
        hidebottombar();
//        loginBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_new_login_layout, content, true);
        loginBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_login_page_design, content, true);
        loginViewModel = new ViewModelProvider(Ced_Login.this, viewModelFactory).get(LoginViewModel.class);
//        navBinding.MageNativeTawkSupport.setVisibility(View.GONE);
        sheetBehavior = BottomSheetBehavior.from(loginBinding.forgotPassLayout.forgotPassSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        setbottomsheet(sheetBehavior, loginBinding.view);
        FacebookSdk.setAutoInitEnabled(true);
//        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        MageNative_mage = loginBinding.headerImage;
        loginButton = loginBinding.fbLoginBtn;
        social_login_linear = loginBinding.layoutFbLogin;
        social_login_google = loginBinding.layoutGoogleLogin;
        orsection = loginBinding.orSection;
        signupwithustext = loginBinding.txtSignUp;
        signInButton = loginBinding.googleSignInBtn;
        edt_username = loginBinding.edtUserName;
        edt_password = loginBinding.edtPassword;
        Forgot_Password = loginBinding.txtForgotPass;
        Login_button = loginBinding.btnLogin;

        set_regular_font_fortext(signupwithustext);
        set_regular_font_fortext(edt_password);
        set_regular_font_fortext(edt_username);
        set_regular_font_fortext(Forgot_Password);
        set_regular_font_fortext(Login_button);


        loginViewModel.getFacebookKeyHash(Ced_Login.this);

        social_login_linear.setOnClickListener(v -> loginButton.performClick());

        social_login_google.setOnClickListener(v -> signIn());

        MageNative_mage.setOnClickListener(v -> {
            Intent homeintent = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
            startActivity(homeintent);
            finishAffinity();
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        });
        handleLoginWithMobileButton();
//        handleLoginWithEmailButton();
        fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        loginBinding.loginWithEmail.setOnClickListener(v -> {
            try {
                loginBinding.loginWithEmailLayout.startAnimation(fade_in);
                loginBinding.loginWithMobileLayout.startAnimation(fade_out);
                handleLoginWithEmailButton();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        loginBinding.loginWithMobile.setOnClickListener(v -> {
            try {
                loginBinding.loginWithMobileLayout.startAnimation(fade_in);
                loginBinding.loginWithEmailLayout.startAnimation(fade_out);
                handleLoginWithMobileButton();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        loginButton.setReadPermissions("email");
        signInButton.setSize(SignInButton.SIZE_WIDE);
        /* signInButton.setScopes(gso.getScopeArray());*/

        signInButton.setOnClickListener(this);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                if (accessToken.getCurrentAccessToken().getToken() != null) {
                    checkFacebookLogin();
                }
            }

            @Override
            public void onCancel() {
                // showmsg(getResources().getString(R.string.loginattemptcancled));
            }

            @Override
            public void onError(FacebookException exception) {
                showmsg("" + exception.toString());
            }
        });

        signupwithustext.setOnClickListener(v -> {
            jumpToRegisterPage();
        });

        Forgot_Password.setOnClickListener(v -> {
            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        loginBinding.view.setOnClickListener(view -> {
            if (loginBinding.view.getVisibility() == View.VISIBLE ||
                    sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        loginBinding.forgotPassLayout.MageNativeSend.setOnClickListener(view -> {
            String email = loginBinding.forgotPassLayout.MageNativeGetEmail.getText().toString();
            if (email.isEmpty()) {
                loginBinding.forgotPassLayout.MageNativeGetEmail.requestFocus();
                loginBinding.forgotPassLayout.MageNativeGetEmail.setError("Please fill email address");
            } else {
                if (!isValidEmail(email)) {
                    loginBinding.forgotPassLayout.MageNativeGetEmail.requestFocus();
                    loginBinding.forgotPassLayout.MageNativeGetEmail.setError("Please fill valid email address");
                } else {
                    JsonObject hashMap = new JsonObject();
                    try {
                        hashMap.addProperty("email", email);
                        if (cedSessionManagement.getStoreId() != null) {
                            hashMap.addProperty("store_id", cedSessionManagement.getStoreId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    loginViewModel.forgotPassword(Ced_Login.this, hashMap/*,session.getHahkey()*/).observe(Ced_Login.this, apiResponse -> {
                        switch (apiResponse.status) {
                            case SUCCESS:
                                applydata(apiResponse.data);
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

        Login_button.setOnClickListener(v -> {
            if (loginBinding.loginWithEmailLayout.getVisibility() == View.VISIBLE) {
                loginWithEmail();
            }
        });
        loginBinding.btnSendOtp.setOnClickListener(v -> {
            if (loginBinding.loginWithMobileLayout.getVisibility() == View.VISIBLE) {
                logInWithMobile();
            }
        });

    }

    public void jumpToRegisterPage() {
        Intent RegistrationIntent = new Intent(getApplicationContext(), Ced_Register.class);
        if (getIntent().getStringExtra("Checkout") != null) {
            if (Objects.requireNonNull(getIntent().getStringExtra("Checkout")).equals("CheckoutModule")) {
                RegistrationIntent.putExtra("isfromcheckout", "true");
            }
        }
        startActivity(RegistrationIntent);
        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
    }

    public void logInWithMobile() {
        mobileNumber = Objects.requireNonNull(loginBinding.edtMobile.getText()).toString();
        String mobile = Objects.requireNonNull(loginBinding.edtMobile.getText()).toString();
        if (mobile.length() != 10) {
            edt_username.requestFocus();
            edt_username.setError("Please enter valid mobile number");
        } else {
            JsonObject hashMap = new JsonObject();
            try {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("mobile", mobile);
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
                Intent otp_intent = new Intent(Ced_Login.this, OTPActivity.class);
                otp_intent.putExtra("mobile_number", loginBinding.edtMobile.getText().toString());
                otp_intent.putExtra("origin", "fromOTPlogin");
                otp_intent.putExtra("hashMap", hashMap.toString());
                startActivityIfNeeded(otp_intent, 1008);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
//                ValidateMobileNumber("fromOTPlogin", loginBinding.edtMobile.getText().toString(), hashMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private void loginWithEmail() {
        username = edt_username.getText().toString();
        password = edt_password.getText().toString();

        if (username.isEmpty()) {
            edt_username.requestFocus();
            edt_username.setError("Please enter Email");
        } else {
            if (!isValidEmail(username)) {
                edt_username.requestFocus();
                edt_username.setError("Please enter valid Email");
            } else {
                if (password.isEmpty()) {
                    edt_password.requestFocus();
                    edt_password.setError("Please enter password");
                } else {
                    JsonObject hashMap = new JsonObject();
                    try {
                        if (cedSessionManagement.getStoreId() != null) {
                            hashMap.addProperty("store_id", cedSessionManagement.getStoreId());
                        }
                        if (cedSessionManagement.getCartId() != null) {
                            hashMap.addProperty("cart_id", cedSessionManagement.getCartId());
                        }
                        hashMap.addProperty("email", username);
                        hashMap.addProperty("password", password);
                        JsonObject extension_attr_object = new JsonObject();
                        extension_attr_object.addProperty("login_type", "email");
                        hashMap.add("extension_attributes", extension_attr_object);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    loginViewModel.getLoginData(Ced_Login.this, hashMap).observe(Ced_Login.this, apiResponse -> {
//                        if (apiResponse.status == Status.SUCCESS) {
//                            outputstring = apiResponse.data;
//                            checklogin();
//                        } else {
//                            Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
//                            showmsg(getResources().getString(R.string.errorString));
//                        }
//                    });
                    proceedToLoginUser(hashMap);
                }
            }
        }

            /*if (!isValidateEmail(username)) {
                edt_username.setError(getResources().getString(R.string.invalidemail));
            } else {


                try {
                    //TODO
                    Ced_ClientRequestResponseRest response = new Ced_ClientRequestResponseRest(output -> {

                    }, Ced_Login.this, "POST", hashMap.toString());
                    response.execute(Url);
                } catch (Exception e) {
                    e.printStackTrace();
                    Intent intent = new Intent(getApplicationContext(), Ced_MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
            }*/
    }

    public void proceedToLoginUser(JsonObject hashMap) {
        loginViewModel.getLoginData(Ced_Login.this, hashMap).observe(Ced_Login.this, this::consumeLoginResponse);
    }

    private void handleLoginWithEmailButton() {
        loginBinding.loginWithEmail.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_selected));
        loginBinding.loginWithEmail.setTextColor(getResources().getColor(R.color.white));
        loginBinding.loginWithEmailLayout.setVisibility(View.VISIBLE);

        loginBinding.loginWithMobileLayout.setVisibility(View.GONE);
        loginBinding.loginWithMobile.setBackgroundColor(getResources().getColor(R.color.transparent));
        loginBinding.loginWithMobile.setTextColor(getResources().getColor(R.color.black));
    }

    private void handleLoginWithMobileButton() {
        loginBinding.loginWithMobile.setBackground(ContextCompat.getDrawable(this, R.drawable.tab_selected));
        loginBinding.loginWithMobile.setTextColor(getResources().getColor(R.color.white));
        loginBinding.loginWithMobileLayout.setVisibility(View.VISIBLE);

        loginBinding.loginWithEmailLayout.setVisibility(View.GONE);
        loginBinding.loginWithEmail.setBackgroundColor(getResources().getColor(R.color.transparent));
        loginBinding.loginWithEmail.setTextColor(getResources().getColor(R.color.black));
    }

    private void applydata(String data) {
        try {
            jsonObj = new JSONObject(data);
            if (jsonObj.has("header") && jsonObj.getString("header").equals("false")) {
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
                    String status, message = null;
                    status = c.getString(KEY_STATUS);
                    if (status.equals("success")) {
                        message = c.getString(KEY_Message);
                        showmsg(message);
                        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            loginBinding.view.setVisibility(View.GONE);
                        }
                        cleardataandlogout();
                        Intent home = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(home);
                        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    } else if (status.equals("error")) {
                        message = c.getString(KEY_Message);
                        showmsg(message);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        if (v == signInButton) {
            signIn();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            try {
                handleSignInResult(Objects.requireNonNull(result));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (resultCode==1008){
            if (Objects.requireNonNull(data).hasExtra("data")) {
                JsonObject hashMap = new GsonBuilder().create().fromJson(data.getStringExtra("data"), JsonObject.class);
                proceedToLoginUser(hashMap);
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result) throws JSONException {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            JsonObject hashp = new JsonObject();
            try {
                hashp.addProperty("type", "google");
                hashp.addProperty("token", acct.getId());
                hashp.addProperty("email", Objects.requireNonNull(acct).getEmail());
                firstname = acct.getDisplayName();
                lastname = acct.getFamilyName();
                if (firstname.contains(lastname)) {
                    hashp.addProperty("firstname", firstname.replace(lastname, ""));
                } else {
                    hashp.addProperty("firstname", firstname);
                }
                hashp.addProperty("lastname", lastname);
                username = acct.getEmail();
                if (cedSessionManagement.getStoreId() != null) {
                    hashp.addProperty("store_id", cedSessionManagement.getStoreId());
                }
                if (cedSessionManagement.getCartId() != null) {
                    hashp.addProperty("cart_id", cedSessionManagement.getCartId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            loginViewModel.getSocialLoginData(Ced_Login.this, hashp).observe(Ced_Login.this, Ced_Login.this::consumeLoginResponse);
        } else {
            Log.i("socialloginresult", "result " + result.toString());
            Log.i("socialloginresult", "result " + result.getStatus());
            Log.i("socialloginresult", "result " + result.getSignInAccount());
            showmsg(getResources().getString(R.string.loginfailed));
        }
    }

    private void checkFacebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), (object, response) -> {
            try {
                Log.d(Urls.TAG, "checkFacebookLogin: " + object);
                if (object != null && object.has("email")) {
                    email = object.getString("email");
                    username = email;
                    firstname = object.getString("first_name");
                    lastname = object.getString("last_name");
                    if (object.has("gender")) {
                        genders = object.getString("gender");
                    } else {
                        genders = "none";
                    }

                    JsonObject hashp = new JsonObject();

                    hashp.addProperty("type", "facebook");
                    if (object.has("id")) {
                        Log.e("REpo", "fb unique customer account id == " + object.getString("id"));
                        hashp.addProperty("token", object.getString("id"));
                    }

                    if (firstname.contains(lastname)) {
                        hashp.addProperty("firstname", firstname.replace(lastname, ""));
                    } else {
                        hashp.addProperty("firstname", firstname);
                    }
                    hashp.addProperty("lastname", lastname);
                    hashp.addProperty("email", email);
                    if (genders.equalsIgnoreCase("male")) {
                        hashp.addProperty("gender", "1");
                    } else if (genders.equalsIgnoreCase("female")) {
                        hashp.addProperty("gender", "2");
                    } else {
                        hashp.addProperty("gender", "3");
                    }
                    if (cedSessionManagement.getStoreId() != null) {
                        hashp.addProperty("store_id", cedSessionManagement.getStoreId());
                    }
                    if (cedSessionManagement.getCartId() != null) {
                        hashp.addProperty("cart_id", cedSessionManagement.getCartId());
                    }
                    loginViewModel.getSocialLoginData(Ced_Login.this, hashp).observe(Ced_Login.this, Ced_Login.this::consumeLoginResponse);
                } else {
                    showmsg(getResources().getString(R.string.noemailfound));
                    cleardataandlogout();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,email,last_name,gender");
        request.setParameters(parameters);
        request.executeAsync();
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

    private void getFbHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Intent intent = new Intent(getApplicationContext(), Ced_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Intent intent = new Intent(getApplicationContext(), Ced_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }

    /**
     * Post login submission
     */
    private void checklogin() {
        try {
            jsonObj = new JSONObject(outputstring);
            if (jsonObj.has("header") && jsonObj.getString("header").equals("false")) {
                Intent intent = new Intent(getApplicationContext(), Ced_UnAuthourizedRequestError.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                if (jsonObj == null) {
                    edt_username.setError("Incorrect");
                    edt_password.setError("Incorrect");
                    showmsg(getResources().getString(R.string.confirmnotmatch));
                } else {
                    JSONObject c = null;
                    response = jsonObj.getJSONObject(KEY_OBJECT).getJSONArray(KEY_CUSTOMER);
                    c = response.getJSONObject(0);
                    status = c.getString(KEY_STATUS);
                    if (status.equals("success")) {
                        if (c.has("isConfirmationRequired")) {
                            if (c.getString("isConfirmationRequired").equals("NO")) {
                                customer_id = c.getString(KEY_CUSTOMER_ID);
                                customergroup_id = c.getString(KEY_CUSTOMERGROUP_ID);
                                hash = c.getString(KEY_HASH);
                                session.saveCustomerId(customer_id);
                                session.set_customergroup_id(customergroup_id);
                                cart_summary = String.valueOf(c.getInt(KEY_CART_SUMMARY));
                                Ced_MainActivity.latestcartcount = cart_summary;
                                session.savegender(c.getString("gender"));
                                session.savename(c.getString("name"));
                                if (c.has("subseller_id"))
                                    session.saveSubSellerId(c.getString("subseller_id"));
                                else
                                    session.clearSellerId();
                                session.createLoginSession(hash, username);
                                session.saveCustomerId(customer_id);
                                session.set_customergroup_id(customergroup_id);
                                // showmsg(getResources().getString(R.string.succesfullLogin));
                                if (getIntent().getStringExtra("Checkout") != null) {
                                    if (Objects.requireNonNull(getIntent().getStringExtra("Checkout")).equals("CheckoutModule")) {
                                        cedhandlecheckout();
                                    }
                                } else {
                                    Intent homeintent = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                                    startActivity(homeintent);
                                    finishAffinity();
                                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                                }
                                setdevice_withusermail();
                            } else {
                                showmsg(c.getString("message"));
                                cleardataandlogout();
                                Intent account = new Intent(Ced_Login.this, Ced_Login.class);
                                account.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                account.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(account);
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            }
                        }
                        else {
                            hash = c.getString(KEY_HASH);
                            customer_id = c.getString(KEY_CUSTOMER_ID);
                            customergroup_id = c.getString(KEY_CUSTOMERGROUP_ID);
                            cart_summary = String.valueOf(c.getInt(KEY_CART_SUMMARY));
                            if (c.has("mobile")) {
                                session.saveUserMobile(c.getString("mobile"));
                            }
                            if (c.has("subseller_id"))
                                session.saveSubSellerId(c.getString("subseller_id"));
                            else
                                session.clearSellerId();
                            session.savegender(c.getString("gender"));
                            session.savename(c.getString("name"));
                            Ced_MainActivity.latestcartcount = cart_summary;
                            session.createLoginSession(hash, c.getString("email"));
                            session.saveCustomerId(customer_id);
                            session.set_customergroup_id(customergroup_id);
                            // showmsg(getResources().getString(R.string.succesfullLogin));
                            if (getIntent().getStringExtra("Checkout") != null) {
                                if (Objects.requireNonNull(getIntent().getStringExtra("Checkout")).equals("CheckoutModule")) {
                                    cedhandlecheckout();
                                }
                            } else {
                                Intent homeintent = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                                homeintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(homeintent);
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            }
                            setdevice_withusermail();
                        }
                    } else {
                        showmsg(c.getString(KEY_Message));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Intent intent = new Intent(getApplicationContext(), Ced_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }


    @Override
    public void onBackPressed() {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            loginBinding.view.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}