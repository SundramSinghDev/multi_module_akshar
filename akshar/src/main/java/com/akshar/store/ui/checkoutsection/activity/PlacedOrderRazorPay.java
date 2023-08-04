package com.akshar.store.ui.checkoutsection.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.akshar.store.Ced_MageNative_SharedPrefrence.Ced_SessionManagement_login;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.google.gson.JsonObject;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.ui.orderssection.activity.Ced_ViewOrder;
import com.akshar.store.databinding.ActivityPlacedOrderRazorPayBinding;
import com.akshar.store.ui.checkoutsection.viewmodel.CheckoutViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlacedOrderRazorPay extends Ced_NavigationActivity implements PaymentResultListener {
    ActivityPlacedOrderRazorPayBinding placeOrderBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    CheckoutViewModel checkoutViewModel;
    JSONObject jsonObject;
    HashMap<String, HashMap<String, String>> finalconfigdata;
    HashMap<String, ArrayList<String>> bundledata;
    HashMap<String, String> configdata;
    ArrayList<HashMap<String, String>> ProductData;
    String email = "";
    String paymentcode = "";
    String order_id = "";
    boolean load = false;
    TextView warning;
    String grandTotal;
    String grand_total;
    String razorpay_order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkoutViewModel = new ViewModelProvider(PlacedOrderRazorPay.this, viewModelFactory).get(CheckoutViewModel.class);
        placeOrderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_placed_order_razor_pay, content, true);
        Checkout.preload(getApplicationContext());
        //  tooltext_address.setText(cedSessionManagement.gettool_address());
        bundledata = new HashMap<String, ArrayList<String>>();
        configdata = new HashMap<String, String>();
        finalconfigdata = new HashMap<String, HashMap<String, String>>();
        ProductData = new ArrayList<HashMap<String, String>>();
        paymentcode = getIntent().getStringExtra("paymentcode");
        warning = (TextView) findViewById(R.id.warning);
        jsonObject = new JSONObject();
        if (getIntent().hasExtra("grand_total")) {
//            if (getIntent().getStringExtra("grand_total").contains(".")) {
//                String grandTotal = getIntent().getStringExtra("grand_total").split(".")[0];
//                razorPayOrderId(grandTotal);
//            } else {
            razorPayOrderId(getIntent().getStringExtra("grand_total"));
//            }
//            razorPayOrderId(getIntent().getStringExtra("grand_total").substring(1));
        }
    }

    private void razorPayOrderId(String grandTotal) {
        JsonObject object = new JsonObject();
        try {
            if (cedSessionManagement.getCartId() != null) {
                object.addProperty("cart_id", cedSessionManagement.getCartId());
            }
            object.addProperty("customer_id", session.getCustomerid());
            if (null != cedSessionManagement.getStoreId()) {
                object.addProperty("store_id", cedSessionManagement.getStoreId());
            }
            object.addProperty("amount", Double.valueOf(Double.parseDouble(grandTotal) * 100).intValue());

            checkoutViewModel.getRazorPayOrderId(PlacedOrderRazorPay.this, object).observe(PlacedOrderRazorPay.this, apiResponse -> {
                switch (apiResponse.status) {
                    case SUCCESS:
                        try {
                            JSONObject response = new JSONObject(Objects.requireNonNull(apiResponse.data));
                            if (response.getBoolean("success")) {
                                razorpay_order_id = response.getString("razorpay_order_id");
                                String amount_paid = response.getString("amount_paid");
                                String currency = response.getString("currency");
                                JsonObject order_post_data = new JsonObject();
                                if (getIntent().hasExtra("partialpayment")) {
                                    /*******************for wallet******/
                                    order_post_data.addProperty("check_wallet", "true");
                                }
                                order_post_data.addProperty("email", email);
                                if (cedSessionManagement.getCartId() != null) {
                                    order_post_data.addProperty("cart_id", cedSessionManagement.getCartId());
                                }
                                if (session.isLoggedIn()) {
                                    order_post_data.addProperty("customer_id", session.getCustomerid());
                                }
                                if (cedSessionManagement.getStoreId() != null) {
                                    order_post_data.addProperty("store_id", cedSessionManagement.getStoreId());
                                }
                                saveorder(order_post_data);
                            } else {
                                showmsg(response.getString("message"));
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveorder(JsonObject data) {
        JsonObject extension_attributes = new JsonObject();
        extension_attributes.addProperty("device_type", "app");
        data.add("extension_attributes", extension_attributes);
        try {
            try {
                checkoutViewModel.saveOrder(PlacedOrderRazorPay.this, data).observe(PlacedOrderRazorPay.this, apiResponse -> {
                    switch (apiResponse.status) {
                        case SUCCESS:
                            try {
                                Processorderdata(apiResponse.data);
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

            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Processorderdata(String s) throws JSONException {
        JSONObject object = new JSONObject(s);
        JSONObject jsonObject = object;
        if (jsonObject.getString("success").equals("true")) {
            cedSessionManagement.clearcartId();
            load = true;
            warning.setVisibility(View.VISIBLE);
            if (paymentcode.equals("apppayment")) {
                order_id = jsonObject.getString("order_id");
                grandTotal = jsonObject.getString("grandtotal");
                String currency_code = jsonObject.getString("currency_code");
                startPayment(currency_code, grandTotal, razorpay_order_id);
            } else {
                Ced_MainActivity.latestcartcount = "0";
                cedSessionManagement.clearcartId();
                changecount();
                Intent intent = new Intent(PlacedOrderRazorPay.this, Ced_ViewOrder.class);
                intent.putExtra("order_id", jsonObject.getString("order_id"));
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            }
        }
    }

    @Override
    protected void onResume() {

        invalidateOptionsMenu();
        super.onResume();

    }

    public void changecount() {
        invalidateOptionsMenu();
    }

    public void startPayment(String currencyCode, String amount, String order_id) {
        final Activity activity = this;
        final Checkout co = new Checkout();
        co.setImage(R.drawable.placeholder);
        try {
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.app_name));
            options.put("description", "Your Total Amount is:");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", R.mipmap.ic_launcher);
            options.put("currency", currencyCode);
            options.put("order_id", order_id);
            options.put("amount", Double.valueOf(Double.parseDouble(amount) * 100).intValue());
            JSONObject preFill = new JSONObject();
            preFill.put("email", session.getUserDetails().get(Ced_SessionManagement_login.Key_Email));
            preFill.put("contact", session.getUserMobile());
            options.put("prefill", preFill);
//            options.put("theme", new JSONObject("{color: '#011a27'}"));
            options.put("theme", new JSONObject("{color: '#30B7C5'}"));
            Log.e("RAZORPAY", "startPayment Params: " + options);
            co.open(activity, options);
        } catch (Exception e) {
            showmsg("Error in payment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            showmsg("Payment Successful. Processing your order ...");
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("additional_info", razorpayPaymentID);
            jsonObject.addProperty("order_id", order_id);
            jsonObject.addProperty("failure", "false");
            setFinalOrderCheck(jsonObject, true);
        } catch (Exception e) {
            Log.e("Exception", "Exception in onPaymentSuccess :", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {

            try {
                showmsg("Payment failed::Please try again Thank you.");
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("failure", "true");
                jsonObject.addProperty("order_id", order_id);
                setFinalOrderCheck(jsonObject, false);
            } catch (Exception e) {
                Log.e("Exception", "Exception in onPaymentSuccess :", e);
            }
        } catch (Exception e) {
            Log.e("Exception", "Exception in onPaymentError :", e);
        }
    }

    @Override
    public void onBackPressed() {
        if (load) {
            try {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("order_id", order_id);
                jsonObject.addProperty("failure", "true");
                setFinalOrderCheck(jsonObject, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            super.onBackPressed();
        }
    }

    public void setFinalOrderCheck(JsonObject object, boolean isPaymentSuccess) {
        try {
            drawerViewModel.getAdditionalInfo(PlacedOrderRazorPay.this, object).observe(PlacedOrderRazorPay.this, apiResponse -> {
                switch (apiResponse.status) {
                    case SUCCESS:
                        try {
                            JSONObject jsonObject1 = new JSONObject(Objects.requireNonNull(apiResponse.data));
                            String success = jsonObject1.getString("success");
                            if (success.equals("true")) {
                                Ced_MainActivity.latestcartcount = "0";
                                cedSessionManagement.clearcartId();
                                invalidateOptionsMenu();
                                Intent intent = new Intent(PlacedOrderRazorPay.this, Ced_ViewOrder.class);
                                intent.putExtra("order_id", String.valueOf(object.get("order_id")));
                                startActivity(intent);
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                            } else {
                                Ced_MainActivity.latestcartcount = "0";
                                cedSessionManagement.clearcartId();
                                invalidateOptionsMenu();
                                Intent intent1 = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
                                startActivity(intent1);
                                finishAffinity();
                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
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
