package com.akshar.store.ui.checkoutsection.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.R;
import com.akshar.store.databinding.ActivityPayMentMethodListBinding;
import com.akshar.store.ui.checkoutsection.viewmodel.CheckoutViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PayMentMethodList extends Ced_NavigationActivity {
    ActivityPayMentMethodListBinding methodListBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    CheckoutViewModel checkoutViewModel;

    HashMap<String, String> tittle_methodcode;
    HashMap<String, String> tittle_additional;
    HashMap<String, String> tittle_post;
    String payemntmenthods = "";
    String email = "";
    String shippingcode = "";
    String paymentcode = "";
    boolean custompayment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showtootltext(getResources().getString(R.string.paymentmethods));
        showbackbutton();
        checkoutViewModel = new ViewModelProvider(PayMentMethodList.this, viewModelFactory).get(CheckoutViewModel.class);
        methodListBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_pay_ment_method_list, content, true);
        methodListBinding.payuradio.setButtonDrawable(checkbox_visibility);
        methodListBinding.razorpayradio.setButtonDrawable(checkbox_visibility);
        methodListBinding.additionaldata.setVisibility(View.GONE);
        shippingcode = getIntent().getStringExtra("shipingcode");
        payemntmenthods = getIntent().getStringExtra("paymentmethods");

        tittle_methodcode = new HashMap<>();
        tittle_additional = new HashMap<>();

        try {
            if (!(payemntmenthods.equals("nopayment"))) {
                createpaymentmethods(payemntmenthods);
            }

            methodListBinding.payuradio.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    //  methodListBinding.continueshipping.setText(getResources().getString(R.string.placeorder));
                    methodListBinding.additionaldata.removeAllViews();
                    paymentcode = "PayU";
                    custompayment = true;
                }
            });
            methodListBinding.razorpayradio.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    // methodListBinding.continueshipping.setText(getResources().getString(R.string.placeorder));
                    methodListBinding.additionaldata.removeAllViews();
                    paymentcode = "Razorpay";
                    custompayment = true;
                }
            });

            methodListBinding.continueshipping.setOnClickListener(v -> {
                if (paymentcode.isEmpty()) {
                    showmsg(getResources().getString(R.string.selectpaymentfirst));
                } else {
                    if (custompayment) {
                        JsonObject json = new JsonObject();
                        try {
                            if (session.isLoggedIn()) {
                                json.addProperty("Role", "USER");
                                json.addProperty("cart_id", cedSessionManagement.getCartId());
                                json.addProperty("customer_id", session.getCustomerid());
                            } else {
                                json.addProperty("Role", "Guest");
                                json.addProperty("cart_id", cedSessionManagement.getCartId());
                            }
//                            json.addProperty("payment_method", "apppayment");
                            json.addProperty("payment_method", paymentcode);
                            json.addProperty("shipping_method", shippingcode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        postshippingpaymentinfo(json);

                    } else {
                        JsonObject json = new JsonObject();
                        try {
                            json.addProperty("Role", "USER");
                            json.addProperty("cart_id", cedSessionManagement.getCartId());
                            json.addProperty("customer_id", session.getCustomerid());
                            json.addProperty("payment_method", paymentcode);
                            json.addProperty("shipping_method", shippingcode);
                            if (methodListBinding.additionaldata.getChildCount() > 0) {
                                LinearLayout layout = (LinearLayout) methodListBinding.additionaldata.getChildAt(0);
                                if (layout.getChildCount() == 3) {
                                    TextView tag = (TextView) layout.getChildAt(1);
                                    EditText value = (EditText) layout.getChildAt(2);
                                    if (value.getText().toString().isEmpty()) {
                                        value.setError(getResources().getString(R.string.fillsomevaluefirst));
                                        value.requestFocus();
                                    } else {
                                        json.addProperty(tag.getText().toString(), value.getText().toString());
                                        postshippingpaymentinfo(json);
                                    }
                                } else {
                                    postshippingpaymentinfo(json);
                                }
                            } else {
                                postshippingpaymentinfo(json);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createpaymentmethods(String methods) throws JSONException {
        JSONObject array = new JSONObject(methods);
        JSONObject object = array.getJSONObject("methods");
        if (Objects.requireNonNull(object.names()).length() > 0) {
            RadioGroup ll = new RadioGroup(this);
            ll.setOrientation(LinearLayout.VERTICAL);

            if (tittle_methodcode.size() > 0) {
                tittle_methodcode.clear();
                tittle_additional.clear();
            }

            for (int i = 0; i < object.length(); i++) {
                JSONObject object1 = object.getJSONObject(String.valueOf(Objects.requireNonNull(object.names()).get(i)));
                final RadioButton rdbtn = new RadioButton(this);
                set_regular_font_forRadio(rdbtn);
                rdbtn.setButtonDrawable(checkbox_visibility);
                rdbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    try {
                        if (isChecked) {
                            methodListBinding.continueshipping.setText(getResources().getString(R.string.continuee));
                            custompayment = false;
                            paymentcode = tittle_methodcode.get(rdbtn.getText().toString());

                            if (tittle_additional.containsKey(rdbtn.getText().toString())) {
                                if (methodListBinding.additionaldata.getChildCount() > 0) {
                                    methodListBinding.additionaldata.removeAllViews();
                                }
                                String data = tittle_additional.get(rdbtn.getText().toString());
                                if (Objects.requireNonNull(data).startsWith("{")) {
                                    JSONObject object2 = new JSONObject(data);
                                    JSONArray jsonArray = object2.names();
                                    for (int i1 = 0; i1 < Objects.requireNonNull(jsonArray).length(); i1++) {
                                        LinearLayout layout = new LinearLayout(PayMentMethodList.this);
                                        layout.setOrientation(LinearLayout.VERTICAL);
                                        TextView heading = new TextView(PayMentMethodList.this);
                                        heading.setText(jsonArray.getString(i1) + ":");
                                        heading.setTextColor(getResources().getColor(R.color.onwhitetextcolor));
                                        TextView value = new TextView(PayMentMethodList.this);
                                        value.setText(object2.getString(jsonArray.getString(i1)));
                                        layout.addView(heading, 0);
                                        layout.addView(value, 1);
                                        methodListBinding.additionaldata.addView(layout);
                                    }
                                } else {
                                    if (data.startsWith("[")) {
                                        JSONArray jsonArray = new JSONArray(data);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        LinearLayout layout = new LinearLayout(PayMentMethodList.this);
                                        layout.setOrientation(LinearLayout.VERTICAL);
                                        TextView heading = new TextView(PayMentMethodList.this);
                                        heading.setText(jsonObject.getString("label"));
                                        heading.setTextColor(getResources().getColor(R.color.onwhitetextcolor));
                                        TextView value = new TextView(PayMentMethodList.this);
                                        value.setText(jsonObject.getString("name"));
                                        value.setVisibility(View.GONE);
                                        EditText text = new EditText(PayMentMethodList.this);
                                        layout.addView(heading, 0);
                                        layout.addView(value, 1);
                                        layout.addView(text, 2);
                                        methodListBinding.additionaldata.addView(layout);
                                    } else {
                                        LinearLayout layout = new LinearLayout(PayMentMethodList.this);
                                        layout.setOrientation(LinearLayout.VERTICAL);
                                        TextView heading = new TextView(PayMentMethodList.this);
                                        heading.setText(data);
                                        heading.setTextColor(getResources().getColor(R.color.onwhitetextcolor));
                                        layout.addView(heading, 0);
                                        methodListBinding.additionaldata.addView(layout);
                                    }
                                }
                            } else {
                                methodListBinding.additionaldata.removeAllViews();
                            }
                        } else {
                            // continueshipping.setText("Continue");
                            // paymentcode = "";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                if (tittle_methodcode.containsKey(object1.getString("label"))) {
                    if (object1.has("additional_data")) {
                        tittle_additional.put(object1.getString("label") + "_new", object1.get("additional_data").toString());
                    }
                    tittle_methodcode.put(object1.getString("label") + "_new", object1.getString("value"));
                    rdbtn.setText(object1.getString("label") + "_new");
                } else {
                    if (object1.has("additional_data")) {
                        tittle_additional.put(object1.getString("label"), object1.get("additional_data").toString());
                    }
                    tittle_methodcode.put(object1.getString("label"), object1.getString("value"));
                    rdbtn.setText(object1.getString("label"));
                }

                methodListBinding.radiogroup.addView(rdbtn);
            }

            Log.i("tittle_additional", "" + tittle_additional);
        }
    }

    public void postshippingpaymentinfo(JsonObject data) {
        try {
            checkoutViewModel.saveShippingPaymentMethods(PayMentMethodList.this, data).observe(PayMentMethodList.this, apiResponse -> {
                switch (apiResponse.status) {
                    case SUCCESS:
                        Processdata(apiResponse.data);
                        break;

                    case ERROR:
                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                        showmsg(getResources().getString(R.string.errorString));
                        break;
                }
            });

            /*Ced_ClientRequestResponseRest crr = new Ced_ClientRequestResponseRest(output -> Processdata(output.toString()), PayMentMethodList.this, "POST", data);
            crr.execute(payment_shippingurl);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Processdata(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.getString("success").equals("true")) {
                Intent intent = new Intent(PayMentMethodList.this, ReviewOrderSummary.class);
                intent.putExtra("paymentcode", paymentcode);
                startActivity(intent);
                overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            } else {
                showmsg(getResources().getString(R.string.somethingbadhappended));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
