//package com.akshar.store.ui.checkoutsection.activity;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import androidx.databinding.DataBindingUtil;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.akshar.store.R;
//import com.akshar.store.base.activity.Ced_MainActivity;
//import com.akshar.store.base.activity.Ced_NavigationActivity;
//import com.akshar.store.databinding.ActivityPlaceOrderPayU2Binding;
//import com.akshar.store.databinding.ActivityPlacedOrderRazorPayBinding;
//import com.akshar.store.databinding.MagenativeActivityNoModuleBinding;
//import com.akshar.store.ui.checkoutsection.viewmodel.CheckoutViewModel;
//import com.akshar.store.ui.loginsection.activity.Ced_Login;
//import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
//import com.akshar.store.ui.orderssection.activity.Ced_ViewOrder;
//import com.akshar.store.utils.Urls;
//import com.akshar.store.utils.ViewModelFactory;
//import com.google.android.material.bottomsheet.BottomSheetBehavior;
//import com.google.android.material.dialog.MaterialAlertDialogBuilder;
//import com.google.gson.JsonObject;
//import com.payumoney.core.PayUmoneyConfig;
//import com.payumoney.core.PayUmoneyConstants;
//import com.payumoney.core.entity.TransactionResponse;
//import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
//import com.payumoney.sdkui.ui.utils.ResultModel;
//import com.razorpay.Checkout;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Objects;
//
//import javax.inject.Inject;
//
//import dagger.hilt.android.AndroidEntryPoint;
//
//import static com.akshar.store.Ced_MageNative_SharedPrefrence.Ced_SessionManagement_login.Key_Email;
//import static com.payumoney.core.PayUmoneySdkInitializer.PaymentParam;
//
///*
//public class PlaceOrderPayU extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_place_order_pay_u2);
//    }
//}*/
//@AndroidEntryPoint
//public class PlaceOrderPayU extends Ced_NavigationActivity {
//    ActivityPlaceOrderPayU2Binding placeOrderBinding;
//    @Inject
//    ViewModelFactory viewModelFactory;
//    CheckoutViewModel checkoutViewModel;
//    JSONObject jsonObject;
//    HashMap<String, HashMap<String, String>> finalconfigdata;
//    HashMap<String, ArrayList<String>> bundledata;
//    HashMap<String, String> configdata;
//    ArrayList<HashMap<String, String>> ProductData;
//    String email = "";
//    String paymentcode = "";
//    String order_id = "";
//    /*String MERCHANNTid="5960507";
//    String KEY="QylhKRVd";
//    String SALT="seVTUgzrgE";*/
//    boolean load = false;
//    BottomSheetBehavior sheetBehavior;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        checkoutViewModel = new ViewModelProvider(PlaceOrderPayU.this, viewModelFactory).get(CheckoutViewModel.class);
//        placeOrderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_place_order_pay_u2, content, true);
//        sheetBehavior = BottomSheetBehavior.from(placeOrderBinding.payuverifyLayout.payuVerifySheet);
//        hidebottombar();
//        Checkout.preload(getApplicationContext());
//        //  tooltext_address.setText(cedSessionManagement.gettool_address());
//        bundledata = new HashMap<>();
//        configdata = new HashMap<>();
//        finalconfigdata = new HashMap<>();
//        ProductData = new ArrayList<>();
//        paymentcode = getIntent().getStringExtra("paymentcode");
//        jsonObject = new JSONObject();
//        JsonObject orderpostdata = new JsonObject();
//        if (getIntent().hasExtra("partialpayment")) {/*******************for wallet******/
//            orderpostdata.addProperty("check_wallet", "true");
//        }
//        if (session.isLoggedIn()) {
//            email = session.getUserDetails().get(Key_Email);
//            orderpostdata.addProperty("email", session.getUserDetails().get(Key_Email));
//        } else {
//            email = session.getCheckout_GuestEmail();
//            orderpostdata.addProperty("email", email);
//        }
//        if (cedSessionManagement.getCartId() != null) {
//            orderpostdata.addProperty("cart_id", cedSessionManagement.getCartId());
//        }
//        if (session.isLoggedIn()) {
//            orderpostdata.addProperty("customer_id", session.getCustomerid());
//        }
//        if (cedSessionManagement.getStoreId() != null) {
//            orderpostdata.addProperty("store_id", cedSessionManagement.getStoreId());
//        }
//        saveorder(orderpostdata);
//    }
//
//    public void saveorder(JsonObject data) {
//        try {
//            try {
//                checkoutViewModel.saveOrder(PlaceOrderPayU.this, data).observe(PlaceOrderPayU.this, apiResponse -> {
//                    switch (apiResponse.status) {
//                        case SUCCESS:
//                            try {
//                                Processorderdata(apiResponse.data);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            break;
//
//                        case ERROR:
//                            Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
//                            showmsg(getResources().getString(R.string.errorString));
//                            break;
//                    }
//                });
//
//            /*Ced_ClientRequestResponseRest crr = new Ced_ClientRequestResponseRest(output -> Processorderdata(output.toString()), ProcessOrder.this, "POST", data);
//            crr.execute(saveorderurl);*/
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//           /* Ced_ClientRequestResponseRest crr = new Ced_ClientRequestResponseRest(new Ced_AsyncResponse()
//            {
//                @Override
//                public void processFinish(Object output) throws JSONException
//                {
//                    Processorderdata(output.toString());
//                }
//            },PlaceOrderPayU.this,"POST",data);
//            crr.execute(saveorderurl);*/
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void Processorderdata(String s) throws JSONException {
//        JSONObject object = new JSONObject(s);
//        JSONObject jsonObject = object;
//        if (jsonObject.getString("success").equals("true")) {
//            cedSessionManagement.clearcartId();
//            load = true;
//            placeOrderBinding.warning.setVisibility(View.VISIBLE);
//            if (paymentcode.equals("apppayment")) {
//                order_id = jsonObject.getString("order_id");
//                startPayment_PayU(jsonObject.getString("currency_code"),
//                        Double.parseDouble(jsonObject.getString("grandtotal")),
//                        jsonObject.getJSONObject("payu_details").getString("merchant_key"),
//                        jsonObject.getJSONObject("payu_details").getString("merchant_id"),
//                        jsonObject.getJSONObject("payu_details").getString("merchant_salt"));
//
////                startPayment_PayU(jsonObject.getString("currency_code"),
////                        Double.parseDouble(jsonObject.getString("grandtotal")));
//            } else {
//                session.clear_checkouttemporarydata();
//                Ced_MainActivity.latestcartcount = "0";
//                cedSessionManagement.clearcartId();
//                changecount();
//                Intent intent = new Intent(PlaceOrderPayU.this, Ced_ViewOrder.class);
//                intent.putExtra("order_id", jsonObject.getString("order_id"));
//                startActivity(intent);
//                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
//            }
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        invalidateOptionsMenu();
//        super.onResume();
//    }
//
//    public void changecount() {
//        invalidateOptionsMenu();
//    }
//
//    //        public void startPayment_PayU(String currencycode, Double amount/*,String KEY,String MERCHANNTID,String SALT8*/) {
//    public void startPayment_PayU(String currencycode, Double amount, String KEY, String MERCHANNTID, String SALT) {
//        placeOrderBinding.payuverifyLayout.MageNativeInfo.setText(new StringBuilder().append(getResources().getString(R.string.amounttopay)).append(" ").append(currencycode).append(" ").append(String.valueOf(amount)).toString());
//        try {
//
//            /* --live payu credentials(not akshar client's)
//            String MERCHANNTid="6001190";
//            String key="9tpUD1z5";
//            String salt="iuFovKeeoH";
//            -- */
//
//            /* --akshar client payu credentials
//            String MERCHANNTID = "6650448";
//            String KEY = "GAyDwA3j";
//            String SALT = "pvoCoeIg8h";
//-- */
//
//            /* --test2 payu credentials
//            String MERCHANNTID = "6088969";
//            String KEY = "K5DxC7uh";
//            String SALT = "BIg2JwRf1C";
//-- */
//
//            /* -- test payu official credentials*/
//             /*String MERCHANNTid="5960507";
//             String key="QylhKRVd";
//            String  salt="seVTUgzrgE";
//*/
//            /*  --- client credentials
//            String MERCHANNTid="4825049";
//            String key="BC50nb";
//            String salt="Bwxo1cPe";
//            */
//            final String[] phone = new String[1];
//            if (session.isLoggedIn()) {
//                phone[0] = session.getCheckout_CustomerMobile();
//            } else {
//                phone[0] = session.getCheckout_GuestMobile();
//            }
//            String txnid = order_id;
//            String firstname = "test";
//            String udf1 = "";
//            String udf2 = "";
//            String udf3 = "";
//            String udf4 = "";
//            String udf5 = "";
//            boolean isDisableExitConfirmation = false;
//            boolean isdebug = true;
//            boolean isOverrideResultScreen = true;
//            String productinfo = "Android";
//                /*
//                .setsUrl("https://myakshar.in/index.php/mppayumoney/index/paymentsuccess/")
//                .setfUrl("https://myakshar.in/index.php/mppayumoney/index/paymentfail/")
//                .setsUrl("https://test.payu.in/mobileapp/payumoney/success.php")                    // Success URL (surl)
//                .setfUrl("https://test.payu.in/mobileapp/payumoney/failure.php")                   //Failure URL (furl)
//               */
//         /*   JSONArray productinfo=new JSONArray();
//            JSONObject jsonObject=new JSONObject();
//            jsonObject.put("name","Admin");
//            jsonObject.put("merchantId","4825050");
//            jsonObject.put("value",85);
//            jsonObject.put("commission",0);
//            jsonObject.put("description",143);
//            productinfo.put(jsonObject);*/
//            //  String hashSequence=key+"|"+txnid+"|"+amount+"|"+productinfo2+"|"+firstname+"|"+email+"|"+udf1+"|"+udf2+"|"+udf3+"|"+udf4+"|"+udf5+"||||||"+salt;
//            //String serverCalculatedHash= hashCal("SHA-512", hashSequence);
//
//            PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();
//
//            //Use this to set your custom text on result screen button
//            payUmoneyConfig.setDoneButtonText("Pay to AKshar School Store");
//
//            //Use this to set your custom title for the activity
//            payUmoneyConfig.setPayUmoneyActivityTitle(getResources().getString(R.string.app_name));
//
//            payUmoneyConfig.disableExitConfirmation(isDisableExitConfirmation);
//
//            //-----------verify user details -------------------------------//
//            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//            placeOrderBinding.view.setVisibility(View.VISIBLE);
//            //SHOW BOTTOM SHEET
//            setbottomsheet(sheetBehavior, placeOrderBinding.view);
//            placeOrderBinding.payuverifyLayout.MageNativeGetEmail.setText(email);
//            if (phone[0].contains("+91"))
//                placeOrderBinding.payuverifyLayout.MageNativeGetPhone.setText(phone[0].replace("+91", ""));
//            else
//                placeOrderBinding.payuverifyLayout.MageNativeGetPhone.setText(phone[0]);
//
//            placeOrderBinding.payuverifyLayout.continuetopay.setOnClickListener(view -> {
//                String p_email = placeOrderBinding.payuverifyLayout.MageNativeGetEmail.getText().toString();
//                String p_phone = "";
//                if (placeOrderBinding.payuverifyLayout.MageNativeGetPhone.getText().toString().contains("+91"))
//                    p_phone = placeOrderBinding.payuverifyLayout.MageNativeGetPhone.getText().toString().replace("+91", "");
//                else
//                    p_phone = placeOrderBinding.payuverifyLayout.MageNativeGetPhone.getText().toString();
//                if (p_email.isEmpty()) {
//                    placeOrderBinding.payuverifyLayout.MageNativeGetEmail.requestFocus();
//                    placeOrderBinding.payuverifyLayout.MageNativeGetEmail.setError("Please fill email address");
//                } else if (!isValidEmail_payu(p_email)) {
//                    placeOrderBinding.payuverifyLayout.MageNativeGetEmail.requestFocus();
//                    placeOrderBinding.payuverifyLayout.MageNativeGetEmail.setError(getResources().getString(R.string.invalid_email_id));
//                } else if (p_phone.isEmpty()) {
//                    placeOrderBinding.payuverifyLayout.MageNativeGetPhone.requestFocus();
//                    placeOrderBinding.payuverifyLayout.MageNativeGetPhone.setError("Please fill Phone Number");
//                } else if (!isValidPhone_payu(p_phone)) {
//                    placeOrderBinding.payuverifyLayout.MageNativeGetPhone.requestFocus();
//                    placeOrderBinding.payuverifyLayout.MageNativeGetPhone.setError(getResources().getString(R.string.invalidphone));
//                } else {
//                    email = p_email;
//                    phone[0] = p_phone;
//                    //-------------------------- finalpaytoPayU--------------------------
//                    PaymentParam.Builder builder = new
//                            PaymentParam.Builder();
//                    builder.setAmount(String.valueOf(amount))                          // PaymentIsDebug amount
//                            .setTxnId(txnid)                                             // Transaction ID
//                            .setPhone("+91" + phone[0])                                           // User Phone number
//                            .setProductName(String.valueOf(productinfo))
//                            .setFirstName(firstname)                              // User First name
//                            .setEmail(email)
//                            .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")                    // Success URL (surl)
//                            .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")                   //Failure URL (furl)
//                            .setUdf1(udf1)
//                            .setUdf2(udf2)
//                            .setUdf3(udf3)
//                            .setUdf4(udf4)
//                            .setUdf5(udf5)
//                            .setIsDebug(isdebug)                              // Integration environment - true (Debug)/ false(Production)
//                            .setKey(KEY)// Merchant key
//                            .setMerchantId(MERCHANNTID);             // Merchant ID
//                    //declare paymentParam object
//                    PaymentParam paymentParam = null;
//                    try {
//                        paymentParam = builder.build();
//                        //------------------------
//                        StringBuilder stringBuilder = new StringBuilder();
//                        HashMap<String, String> params = paymentParam.getParams();
//                        stringBuilder.append(params.get(PayUmoneyConstants.KEY) + "|");
//                        stringBuilder.append(params.get(PayUmoneyConstants.TXNID) + "|");
//                        stringBuilder.append(params.get(PayUmoneyConstants.AMOUNT) + "|");
//                        stringBuilder.append(params.get(PayUmoneyConstants.PRODUCT_INFO) + "|");
//                        stringBuilder.append(params.get(PayUmoneyConstants.FIRSTNAME) + "|");
//                        stringBuilder.append(params.get(PayUmoneyConstants.EMAIL) + "|");
//                        stringBuilder.append(params.get(PayUmoneyConstants.UDF1) + "|");
//                        stringBuilder.append(params.get(PayUmoneyConstants.UDF2) + "|");
//                        stringBuilder.append(params.get(PayUmoneyConstants.UDF3) + "|");
//                        stringBuilder.append(params.get(PayUmoneyConstants.UDF4) + "|");
//                        stringBuilder.append(params.get(PayUmoneyConstants.UDF5) + "||||||");
//                        stringBuilder.append(SALT);
//                        String hash = hashCal(stringBuilder.toString());
//                        //set the hash
//                        paymentParam.setMerchantHash(hash);
//                        //------------------------
//                        // Invoke the following function to open the checkout page.
//                        PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, PlaceOrderPayU.this, R.style.AppTheme, isOverrideResultScreen);
//
//                       /* final ReviewOrderBundle reviewOrderBundle = new ReviewOrderBundle();
//                        reviewOrderBundle.addOrderDetails( "Order Id", order_id );
//                        reviewOrderBundle.addOrderDetails( "firstname", firstname );
//                        reviewOrderBundle.addOrderDetails( "email", email );
//                        PayUmoneyConfig.getInstance().setReviewOrderBundle( reviewOrderBundle );*/
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Log.i("SUNDRAM", "startPayment_PayU: " + e.getMessage());
//                        showmsg(e.getMessage());
//                        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
//                            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                            placeOrderBinding.view.setVisibility(View.VISIBLE);
//                        }
//                    }
//                    //-------------------------- finalpaytoPayU--------------------------
//                }
//            });
//
//            //-----------verify user details -------------------------------//
//        } catch (Exception e) {
//            showmsg("Error in payment: " + e.getMessage());
//            Log.i("SUNDRAM00", "startPayment_PayU: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // Result Code is -1 send from Payumoney activity
//        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
//        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
//            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
//                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                placeOrderBinding.view.setVisibility(View.GONE);
//            }
//
//            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
//                    .INTENT_EXTRA_TRANSACTION_RESPONSE);
//            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);
//            // Check which object is non-null
//            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
//                try {
//
//                    String payuResponse = transactionResponse.getPayuResponse(); // Response from Payumoney
//                    JSONObject payuResponse_json = new JSONObject(transactionResponse.getPayuResponse());
//                    String merchantResponse = transactionResponse.getTransactionDetails(); // Response from SURl and FURL
//                    String msg = payuResponse_json.getString("message") + " : " + payuResponse_json.getJSONObject("result").getString("status");
//                    showmsg(msg);
//                    PayUmoneyFlowManager.logoutUser(getApplicationContext());
//                    if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
//                        //Success Transaction---------------------------------------------------
//                        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
//                            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                            placeOrderBinding.view.setVisibility(View.GONE);
//                        }
//                        Log.d(Urls.TAG, "onActivityResult: " + "\nPayu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse);
//                        JsonObject jsonObject = new JsonObject();
//                        jsonObject.addProperty("additional_info", msg);
//                        jsonObject.addProperty("order_id", order_id);
//                        jsonObject.addProperty("failure", "false");
//                        setFinalordercheck(jsonObject);
//
//                       /* new MaterialAlertDialogBuilder(this,R.style.MaterialDialog)
//                                .setCancelable(false)
//                                .setMessage("Payu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse)
//                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int whichButton) {
//                                        dialog.dismiss();
//                                    }
//                                }).show();*/
//                    } else {
//                        //Failure Transaction---------------------------------------------------------
//                        Log.d(Urls.TAG, "onActivityResult: " + "\nPayu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse);
//                        JsonObject jsonObject = new JsonObject();
//                        jsonObject.addProperty("order_id", order_id);
//                        jsonObject.addProperty("failure", "true");
//                        setFinalordercheck(jsonObject);
//                        /* new MaterialAlertDialogBuilder(this,R.style.MaterialDialog)
//                            .setCancelable(false)
//                            .setMessage("Payu's Data : " + payuResponse + "\n\n\n Merchant's Data: " + merchantResponse)
//                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    dialog.dismiss();
//                                }
//                            }).show();*/
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else if (resultModel != null && resultModel.getError() != null) {
//                Log.d(Urls.TAG, "Error response : " + resultModel.getError().getTransactionResponse());
//            } else {
//                Log.d(Urls.TAG, "Both objects are null!");
//            }
//        }
//    }
//
//    public static String hashCal(String type, String hashString) {
//        StringBuilder hash = new StringBuilder();
//        MessageDigest messageDigest = null;
//        try {
//            messageDigest = MessageDigest.getInstance(type);
//            messageDigest.update(hashString.getBytes());
//            byte[] mdbytes = messageDigest.digest();
//            for (byte hashByte : mdbytes) {
//                hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
//            }
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return hash.toString();
//    }
//
//    public static String hashCal(String str) {
//        byte[] hashseq = str.getBytes();
//        StringBuilder hexString = new StringBuilder();
//        try {
//            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
//            algorithm.reset();
//            algorithm.update(hashseq);
//            byte messageDigest[] = algorithm.digest();
//            for (byte aMessageDigest : messageDigest) {
//                String hex = Integer.toHexString(0xFF & aMessageDigest);
//                if (hex.length() == 1) {
//                    hexString.append("0");
//                }
//                hexString.append(hex);
//            }
//        } catch (NoSuchAlgorithmException ignored) {
//        }
//        return hexString.toString();
//    }
//
//    @Override
//    public void onBackPressed() {
//        //super.onBackPressed();
//        if (load) {
//
//            PayUmoneyFlowManager.logoutUser(getApplicationContext());
//            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
//                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                placeOrderBinding.view.setVisibility(View.GONE);
//            }
//            /*else
//            {*/
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("order_id", order_id);
//            jsonObject.addProperty("failure", "true");
//            setFinalordercheck(jsonObject);
//            /*}*/
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    /* public void setFinalordercheck(JSONObject object)
//     {
//         try
//         {
//             Ced_ClientRequestResponseRest crr = new Ced_ClientRequestResponseRest(new Ced_AsyncResponse()
//             {
//                 @Override
//                 public void processFinish(Object output) throws JSONException
//                 {
//                     Log.i("FinalData",""+output.toString());
//                     JSONObject jsonObject1=new JSONObject(output.toString());
//                     String success=jsonObject1.getString("success");
//                     if(success.equals("true"))
//                     {
//                         Ced_MainActivity.latestcartcount="0";
//                         cedSessionManagement.clearcartId();
//                         changecount();
//                         Intent intent=new Intent(PlaceOrderPayU.this,Ced_ViewOrder.class);
//                         intent.putExtra("order_id", order_id);
//                         startActivity(intent);
//                         overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
//                     }
//                     else
//                     {
//                         Ced_MainActivity.latestcartcount="0";
//                         cedSessionManagement.clearcartId();
//                         changecount();
//                         Intent intent1=new Intent(getApplicationContext(), Ced_New_home_page.class);
//                         startActivity(intent1);
//                         finishAffinity();
//                         overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
//                     }
//                 }
//             },PlaceOrderPayU.this,"POST",object.toString());
//             crr.execute(finalordercheck);
//         }
//         catch(Exception e)
//         {
//             e.printStackTrace();
//         }
//     }*/
//    public void setFinalordercheck(JsonObject object) {
//        try {
//            checkoutViewModel.getAdditionalInfo(PlaceOrderPayU.this, object).observe(PlaceOrderPayU.this, apiResponse -> {
//                switch (apiResponse.status) {
//                    case SUCCESS:
//                        try {
//                            JSONObject jsonObject1 = new JSONObject(Objects.requireNonNull(apiResponse.data));
//                            String success = jsonObject1.getString("success");
//                            if (success.equals("true")) {
//                                Ced_MainActivity.latestcartcount = "0";
//                                cedSessionManagement.clearcartId();
//                                changecount();
//                                session.clear_checkouttemporarydata();
//                                Intent intent = new Intent(PlaceOrderPayU.this, Ced_ViewOrder.class);
//                                intent.putExtra("order_id", order_id);
//                                startActivity(intent);
//                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
//                            } else {
//                                /*Ced_MainActivity.latestcartcount = "0";*/
//                                /*cedSessionManagement.clearcartId();*/
//                                /* changecount();*/
//                                Intent intent1 = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
//                                startActivity(intent1);
//                                finishAffinity();
//                                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        break;
//
//                    case ERROR:
//                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
//                        showmsg(getResources().getString(R.string.errorString));
//                        break;
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}