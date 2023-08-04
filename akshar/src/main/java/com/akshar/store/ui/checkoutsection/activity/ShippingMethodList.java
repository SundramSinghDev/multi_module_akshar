package com.akshar.store.ui.checkoutsection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonObject;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.R;
import com.akshar.store.databinding.ActivityShippingPaymentMethodBinding;
import com.akshar.store.ui.checkoutsection.viewmodel.CheckoutViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ShippingMethodList extends Ced_NavigationActivity {
    ActivityShippingPaymentMethodBinding methodBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    CheckoutViewModel checkoutViewModel;

    JSONObject paymentmethods = null;
    HashMap<String, String> tittle_methodcode;
    String shippingurl = "";
    String shippingcode = "";
    boolean ishavingdownloadableonly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showtootltext(getResources().getString(R.string.shippingmethods));
        showbackbutton();
        checkoutViewModel = new ViewModelProvider(ShippingMethodList.this, viewModelFactory).get(CheckoutViewModel.class);
        methodBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_shipping_payment_method, content, true);
        tittle_methodcode = new HashMap<>();
        ishavingdownloadableonly = getIntent().getBooleanExtra("ishavingdownloadableonly", false);
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("Role", "USER");
            if (cedSessionManagement.getCartId() != null) {
                jsonObject.addProperty("cart_id", cedSessionManagement.getCartId());
            }
            if (session.isLoggedIn()) {
                jsonObject.addProperty("customer_id", session.getCustomerid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (ishavingdownloadableonly) {
            Intent intent = new Intent(ShippingMethodList.this, PayMentMethodList.class);
            intent.putExtra("shipingcode", " ");
            intent.putExtra("paymentmethods", "nopayment");
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            finish();
        } else {
            postshippinginfo(jsonObject);
        }

        try {
            methodBinding.continueshipping.setOnClickListener(v -> {
                if (shippingcode.isEmpty()) {
                   showmsg(getResources().getString(R.string.selectshippinfmethodfirst));
                } else {
                    Intent intent = new Intent(ShippingMethodList.this, PayMentMethodList.class);
                    intent.putExtra("shipingcode", shippingcode);
                    if (paymentmethods != null) {
                        intent.putExtra("paymentmethods", paymentmethods.toString());
                    } else {
                        intent.putExtra("paymentmethods", "nopayment");
                    }
                    startActivity(intent);
                    overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createshipmenthods(String shippingmethods) {
        try {
            JSONObject object = new JSONObject(shippingmethods);
            String success = object.getString("success");
            if (success.equals("true")) {
                if (!(object.get("payments").equals("No Payment Method Availabile."))) {
                    paymentmethods = object.getJSONObject("payments");
                }

                if (object.get("shipping").equals("No Quotes Availabile.")) {
                    showmsg("No Quotes Availabile.");
                } else {
                    JSONObject shipping = object.getJSONObject("shipping");
                    JSONArray methods = shipping.getJSONArray("methods");
                    if (methods.length() > 0) {
                        RadioGroup ll = new RadioGroup(this);
                        ll.setOrientation(LinearLayout.VERTICAL);
                        if (tittle_methodcode.size() > 0) {
                            tittle_methodcode.clear();
                        }

                        for (int i = 0; i < methods.length(); i++) {
                            JSONObject object1 = methods.getJSONObject(i);
                            final RadioButton rdbtn = new RadioButton(this);
                            set_regular_font_forRadio(rdbtn);
                            rdbtn.setButtonDrawable(checkbox_visibility);
                            rdbtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                                try {
                                    if (isChecked)
                                    {
                                        shippingcode = tittle_methodcode.get(rdbtn.getText().toString());
                                    } else {
                                        shippingcode = " ";
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });

                            if (tittle_methodcode.containsKey(object1.getString("label"))) {
                                tittle_methodcode.put(object1.getString("label") + "_new", object1.getString("value"));
                                rdbtn.setText(object1.getString("label") + "_new");
                            } else {
                                tittle_methodcode.put(object1.getString("label"), object1.getString("value"));
                                rdbtn.setText(object1.getString("label"));
                            }

                            ll.addView(rdbtn);
                        }

                        methodBinding.radiogroup.addView(ll);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void postshippinginfo(JsonObject data) {
        try {
            checkoutViewModel.getShippingPaymentMethods(ShippingMethodList.this, data).observe(ShippingMethodList.this, apiResponse -> {
                switch (apiResponse.status){
                    case SUCCESS:
                        createshipmenthods(apiResponse.data);
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
