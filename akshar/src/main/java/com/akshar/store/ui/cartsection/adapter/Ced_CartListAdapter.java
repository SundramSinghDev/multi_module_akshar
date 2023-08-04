/*
 * Copyright/* *
 *           * CedCommerce
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
 *           * @license   http://cedcommerce.com/license-agreement.txt
 *
 */
package com.akshar.store.ui.cartsection.adapter;

import android.app.Activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.akshar.store.databinding.NewCartProductItemBinding;
import com.akshar.store.databinding.NewMagenativeActivityCartListingBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonObject;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import com.akshar.store.databinding.MagenativeCartCompListBinding;
import com.akshar.store.ui.cartsection.activity.Ced_CartListing;
import com.akshar.store.ui.cartsection.viewmodel.CartViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

public class Ced_CartListAdapter extends RecyclerView.Adapter<Ced_CartListAdapter.CartListViewHolder> {
    @Inject
    ViewModelFactory viewModelFactory;
    CartViewModel cartViewModel;

    //Variable and Object Initializations
    private HashMap<String, HashMap<String, String>> Ced_value_config;
    private ArrayList<HashMap<String, String>> Ced_data;
    private HashMap<String, ArrayList<String>> Ced_value;
    private boolean Ced_config_flag = false;
    private Activity Ced_activity;
    private boolean Ced_flag = false;
    private int Ced_count_config = 1;
    private String Ced_labeltag = "";
    private String Ced_Jstring = "";
    private int Ced_count = 1;
    private float x1, x2;
    static final int MIN_DISTANCE = 40;
    private Boolean fromcartlisting;
    private String ced_J_string = "";

    //constructor for cartlisting
    public Ced_CartListAdapter(Activity a, ArrayList<HashMap<String, String>> d, Boolean fromcartlisting) {
        Ced_activity = a;
        Ced_data = d;
        this.fromcartlisting = fromcartlisting;
        cartViewModel = new ViewModelProvider((FragmentActivity) Ced_activity, viewModelFactory).get(CartViewModel.class);
        setHasStableIds(true);
    }

    //constructor for cartlisting
    public Ced_CartListAdapter(Activity a, ArrayList<HashMap<String, String>> d, HashMap<String, ArrayList<String>> val, String param, Boolean fromcartlisting) {
        Ced_value = val;
        Ced_activity = a;
        Ced_data = d;
        this.fromcartlisting = fromcartlisting;
        cartViewModel = new ViewModelProvider((FragmentActivity) Ced_activity, viewModelFactory).get(CartViewModel.class);
        setHasStableIds(true);
    }

    //constructor for cartlisting
    public Ced_CartListAdapter(Activity a, ArrayList<HashMap<String, String>> d, HashMap<String, HashMap<String, String>> val, Boolean fromcartlisting) {
        Ced_value_config = val;
        Ced_activity = a;
        Ced_data = d;
        this.fromcartlisting = fromcartlisting;
        cartViewModel = new ViewModelProvider((FragmentActivity) Ced_activity, viewModelFactory).get(CartViewModel.class);
        setHasStableIds(true);
    }

    //constructor for cartlisting
    public Ced_CartListAdapter(Activity a, ArrayList<HashMap<String, String>> d, HashMap<String, ArrayList<String>> bundle, HashMap<String, HashMap<String, String>> val, Boolean fromcartlisting) {
        Ced_value = bundle;
        Ced_value_config = val;
        Ced_activity = a;
        Ced_data = d;
        this.fromcartlisting = fromcartlisting;
        cartViewModel = new ViewModelProvider((FragmentActivity) Ced_activity, viewModelFactory).get(CartViewModel.class);
        setHasStableIds(true);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public CartListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        MagenativeCartCompListBinding listBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
//                R.layout.magenative_cart_comp_list, parent, false);
        NewCartProductItemBinding listBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.new_cart_product_item, parent, false);
        return new CartListViewHolder(listBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartListViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        HashMap<String, String> cartItem = Ced_data.get(position);
//        ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(holder.listBinding.MageNativeAtc);
//        ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(holder.listBinding.MageNativeAtwl);
        ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(holder.listBinding.price);
        ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(holder.listBinding.Quantity);
        ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(holder.listBinding.productQuantity);
        ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(holder.listBinding.quantitylabel);
        ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(holder.listBinding.productName);
        ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(holder.listBinding.productPrice);
        if (fromcartlisting) {
            //-cartlistingpage
            holder.listBinding.qtysection.setVisibility(View.VISIBLE);
            holder.listBinding.decrese.setVisibility(View.VISIBLE);
//            holder.listBinding.maineditsection.setVisibility(View.VISIBLE);
            holder.listBinding.deletesection.setVisibility(View.VISIBLE);
            holder.listBinding.productQuantity.setVisibility(View.GONE);
            holder.listBinding.quantitylabel.setVisibility(View.GONE);
        } else {
            //-order summary page
            holder.listBinding.qtysection.setVisibility(View.GONE);
            holder.listBinding.decrese.setVisibility(View.GONE);
//            holder.listBinding.maineditsection.setVisibility(View.GONE);
            holder.listBinding.deletesection.setVisibility(View.GONE);
            holder.listBinding.productQuantity.setVisibility(View.VISIBLE);
            holder.listBinding.quantitylabel.setVisibility(View.VISIBLE);
            holder.listBinding.productImage.getLayoutParams().height = (int) Ced_activity.getResources().getDimension(R.dimen.dim_80dp);
            holder.listBinding.productImage.getLayoutParams().width = (int) Ced_activity.getResources().getDimension(R.dimen.dim_80dp);
        }
        holder.listBinding.productImage.setOnClickListener(v -> {
            Intent listintent = new Intent(Ced_activity, Ced_NewProductView.class);
            listintent.putExtra("product_id", holder.listBinding.productId.getText().toString());
            listintent.putExtra("CURRENT", position);
            Ced_activity.startActivity(listintent);
        });

        holder.listBinding.increase.setOnClickListener(v -> {
            int qty = Integer.parseInt(holder.listBinding.Quantity.getText().toString());
//            holder.listBinding.decrese.setColorFilter(ContextCompat.getColor(Ced_activity, R.color.color_gray_DBDBDB), android.graphics.PorterDuff.Mode.MULTIPLY);
            String new_count = String.valueOf(qty + 1);
            holder.listBinding.Quantity.setText(new_count);
            try {
                holder.listBinding.increase.setEnabled(false);
                JsonObject updateCartData = new JsonObject();
                if (((Ced_NavigationActivity) Ced_activity).session.isLoggedIn()) {
                    updateCartData.addProperty("customer_id", ((Ced_NavigationActivity) Ced_activity).session.getCustomerid());
                    if (((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getCartId() != null) {
                        updateCartData.addProperty("cart_id", ((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getCartId());
                    }
                } else {
                    updateCartData.addProperty("cart_id", ((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getCartId());
                }
                updateCartData.addProperty("item_id", holder.listBinding.itemid.getText().toString());
                if (((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getStoreId() != null) {
                    updateCartData.addProperty("store_id", ((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getStoreId());
                }
                updateCartData.addProperty("qty", holder.listBinding.Quantity.getText().toString());
                cartViewModel.updateCart(Ced_activity, updateCartData).observe((FragmentActivity) Ced_activity, apiResponse -> {
                    switch (apiResponse.status) {
                        case SUCCESS:
                            ced_J_string = apiResponse.data;
                            try {
                                assert ced_J_string != null;
                                JSONObject jsonObject = new JSONObject(ced_J_string);
                                if (jsonObject.has("header") && jsonObject.getString("header").equals("false")) {
                                    Intent intent = new Intent(Ced_activity, Ced_UnAuthourizedRequestError.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    Ced_activity.startActivity(intent);
                                } else {
                                    if (jsonObject.getString("success").equals("true")) {
                                        if (Ced_activity instanceof Ced_CartListing) {
                                            ((Ced_CartListing) Ced_activity).request();
                                        }
                                    } else {
                                        holder.listBinding.Quantity.setText(holder.listBinding.Quantity.getTag().toString());
                                        holder.listBinding.productQuantity.setText(holder.listBinding.Quantity.getTag().toString());
                                        ((Ced_NavigationActivity) Ced_activity).showmsg(jsonObject.getString("message"));
                                    }
                                }
                                holder.listBinding.increase.setEnabled(true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;

                        case ERROR:
                            Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                            ((Ced_NavigationActivity) Ced_activity).showmsg(Ced_activity.getResources().getString(R.string.errorString));
                            break;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        holder.listBinding.decrese.setOnClickListener(v -> {
            int qty = Integer.parseInt(holder.listBinding.Quantity.getText().toString());
            if (qty > 1) {
                String new_count = String.valueOf(qty - 1);
                holder.listBinding.Quantity.setText(new_count);
                try {
                    holder.listBinding.decrese.setEnabled(false);
                    JsonObject updateCartData = new JsonObject();
                    if (((Ced_NavigationActivity) Ced_activity).session.isLoggedIn()) {
                        updateCartData.addProperty("customer_id", ((Ced_NavigationActivity) Ced_activity).session.getCustomerid());
                        if (((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getCartId() != null) {
                            updateCartData.addProperty("cart_id", ((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getCartId());
                        }
                    } else {
                        updateCartData.addProperty("cart_id", ((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getCartId());
                    }
                    updateCartData.addProperty("item_id", holder.listBinding.itemid.getText().toString());
                    if (((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getStoreId() != null) {
                        updateCartData.addProperty("store_id", ((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getStoreId());
                    }
                    updateCartData.addProperty("qty", holder.listBinding.Quantity.getText().toString());
                    cartViewModel.updateCart(Ced_activity, updateCartData).observe((FragmentActivity) Ced_activity, apiResponse -> {
                        switch (apiResponse.status) {
                            case SUCCESS:
                                ced_J_string = apiResponse.data;
                                try {
                                    assert ced_J_string != null;
                                    JSONObject jsonObject = new JSONObject(ced_J_string);
                                    if (jsonObject.has("header") && jsonObject.getString("header").equals("false")) {
                                        Intent intent = new Intent(Ced_activity, Ced_UnAuthourizedRequestError.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Ced_activity.startActivity(intent);
                                    } else {
                                        if (jsonObject.getString("success").equals("true")) {
                                            if (Ced_activity instanceof Ced_CartListing) {
                                                ((Ced_CartListing) Ced_activity).request();
                                            }
                                        } else {
                                            holder.listBinding.Quantity.setText(holder.listBinding.Quantity.getTag().toString());
                                            holder.listBinding.productQuantity.setText(holder.listBinding.Quantity.getTag().toString());
                                            ((Ced_NavigationActivity) Ced_activity).showmsg(jsonObject.getString("message"));
                                        }
                                    }
                                    holder.listBinding.decrese.setEnabled(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;

                            case ERROR:
                                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                                ((Ced_NavigationActivity) Ced_activity).showmsg(Ced_activity.getResources().getString(R.string.errorString));
                                break;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                holder.listBinding.decrese.setColorFilter(ContextCompat.getColor(Ced_activity, R.color.color_gray_DBDBDB), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
        });

        final View finalVi = holder.listBinding.getRoot();
        holder.listBinding.deletesection.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(Ced_activity, R.style.MaterialDialog)
                    .setTitle(Ced_activity.getResources().getString(R.string.app_name))
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage(Ced_activity.getResources().getString(R.string.confirm_first))
                    .setNegativeButton("Cancel", (dialog, which) -> {
                    })
                    .setPositiveButton("Confirm", (dialog, which) -> {
                        JsonObject deleteFromCartData = new JsonObject();
                        try {
                            if (((Ced_NavigationActivity) Ced_activity).session.isLoggedIn()) {
                                deleteFromCartData.addProperty("customer_id", ((Ced_NavigationActivity) Ced_activity).session.getCustomerid());
                                if (((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getCartId() != null) {
                                    deleteFromCartData.addProperty("cart_id", ((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getCartId());
                                }
                                deleteFromCartData.addProperty("item_id", holder.listBinding.itemid.getText().toString());

                            } else {
                                deleteFromCartData.addProperty("cart_id", ((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getCartId());
                                deleteFromCartData.addProperty("item_id", holder.listBinding.itemid.getText().toString());
                            }
                            if (((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getStoreId() != null) {
                                deleteFromCartData.addProperty("store_id", ((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getStoreId());
                            }
                            cartViewModel.deleteFromCart(Ced_activity, deleteFromCartData).observe((FragmentActivity) Ced_activity, apiResponse -> {
                                switch (apiResponse.status) {
                                    case SUCCESS:
                                        Ced_Jstring = apiResponse.data;
                                        try {
                                            JSONObject jsonObject = new JSONObject(Ced_Jstring);
                                            if (jsonObject.has("header") && jsonObject.getString("header").equals("false")) {
                                                Intent intent = new Intent(Ced_activity, Ced_UnAuthourizedRequestError.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                Ced_activity.startActivity(intent);
                                            } else {
                                                if (jsonObject.getString("success").equals("removed_successfully")) {
                                                    // Ced_MainActivity.latestcartcount = jsonObject.getString("items_count");
                                                    Ced_MainActivity.latestcartcount = String.valueOf(jsonObject.getInt("items_count"));
                                                    finalVi.startAnimation(outToLeftAnimation());
                                                    finalVi.setVisibility(View.GONE);
                                                    Ced_data.remove(position);
                                                    //refreshlistview();
                                                    if (Ced_activity instanceof Ced_CartListing) {
                                                        ((Ced_CartListing) Ced_activity).request();
                                                    }
                                                } else {

                                                    ((Ced_NavigationActivity) Ced_activity).showmsg(jsonObject.getString("message"));
                                                }
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;

                                    case ERROR:
                                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                                        ((Ced_NavigationActivity) Ced_activity).showmsg(Ced_activity.getResources().getString(R.string.errorString));
                                        break;
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    })
                    .show();
        });

        holder.listBinding.productName.setText(cartItem.get(Ced_CartListing.KEY_NAME));
        holder.listBinding.productId.setText(cartItem.get(Ced_CartListing.KEY_ID));
        holder.listBinding.itemid.setText(cartItem.get(Ced_CartListing.ITEMID));
        holder.listBinding.productPrice.setText(cartItem.get(Ced_CartListing.KEY_Price));
      /*  if(Ced_CartListing.updatedquantitywithid.size()>0)
        {
            if(Ced_CartListing.updatedquantitywithid.containsKey(holder.listBinding.productId.getText().toString()))
            {
                holder.listBinding.Quantity.setText(Ced_CartListing.updatedquantitywithid.get(holder.listBinding.productId.getText().toString()));
                holder.listBinding.productQuantity.setText(Ced_CartListing.updatedquantitywithid.get(holder.listBinding.productId.getText().toString()));
                holder.listBinding.Quantity.setTag(Ced_CartListing.updatedquantitywithid.get(holder.listBinding.productId.getText().toString()));
            }
            else
            {*/
        holder.listBinding.Quantity.setText(cartItem.get(Ced_CartListing.Key_Quantity));
        holder.listBinding.productQuantity.setText(cartItem.get(Ced_CartListing.Key_Quantity));
        holder.listBinding.Quantity.setTag(cartItem.get(Ced_CartListing.Key_Quantity));
        // Ced_CartListing.updatedquantitywithid.put(String.valueOf( holder.listBinding.productId.getText()),cartItem.get(Ced_CartListing.Key_Quantity));
     /*       }
        }
        else
        {
            holder.listBinding.Quantity.setText(cartItem.get(Ced_CartListing.Key_Quantity));
            holder.listBinding.productQuantity.setText(cartItem.get(Ced_CartListing.Key_Quantity));
        }*/
        Glide.with(Ced_activity)
                .load(cartItem.get(Ced_CartListing.KEY_Image))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.listBinding.productImage);

        if (Objects.requireNonNull(cartItem.get("Product_type")).equals("bundle")) {
            holder.listBinding.budleoption.setVisibility(View.VISIBLE);
            holder.listBinding.confioption.setVisibility(View.GONE);
            if (Ced_flag) {
                holder.listBinding.budleoption.removeAllViewsInLayout();
            }
            ArrayList<String> bundleCed_data = Ced_value.get(cartItem.get(Ced_CartListing.ITEMID));
            Iterator iterator = Objects.requireNonNull(bundleCed_data).iterator();
            LinearLayout layout = new LinearLayout(Ced_activity);
            layout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout layout2 = new LinearLayout(Ced_activity);
            layout2.setOrientation(LinearLayout.VERTICAL);
            while (iterator.hasNext()) {
                String Ced_dataoptions = (String) iterator.next();
                String[] parts = Ced_dataoptions.split("#");
                if (layout2.getChildCount() <= 0) {
                    TextView label = new TextView(Ced_activity);
                    label.setTextSize(12);
                    ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(label);
                    label.setTextColor(Ced_activity.getResources().getColor(R.color.black));
                    label.setText(parts[0]);
                    Ced_labeltag = parts[0];
                    TextView tittle = new TextView(Ced_activity);
                    tittle.setTextSize(12);
                    ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(tittle);
                    tittle.setText(parts[1]);
                    LinearLayout layout1 = new LinearLayout(Ced_activity);
                    layout1.setOrientation(LinearLayout.HORIZONTAL);
                    layout1.setPadding(0, 0, 0, 10);
                    TextView qty = new TextView(Ced_activity);
                    qty.setTextSize(12);
                    ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(qty);
                    qty.setText(Ced_activity.getResources().getString(R.string.qty) + parts[2]);
                    qty.setPadding(0, 0, 3, 0);
                    TextView optionprice = new TextView(Ced_activity);
                    optionprice.setTextSize(12);
                    optionprice.setText(Ced_activity.getResources().getString(R.string.price) + parts[3]);
                    ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(optionprice);
                    layout1.addView(qty, 0);
                    layout1.addView(optionprice, 1);
                    layout2.addView(label);
                    layout2.addView(tittle);
                    layout2.addView(layout1);
                } else {
                    if (Ced_labeltag.equals(parts[0])) {
                        TextView tittle = new TextView(Ced_activity);
                        tittle.setTextSize(12);
                        tittle.setText(parts[1]);
                        ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(tittle);
                        LinearLayout layout1 = new LinearLayout(Ced_activity);
                        layout1.setOrientation(LinearLayout.HORIZONTAL);
                        layout1.setPadding(0, 0, 0, 10);
                        TextView qty = new TextView(Ced_activity);
                        qty.setTextSize(12);
                        qty.setText(Ced_activity.getResources().getString(R.string.qty) + parts[2]);
                        ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(qty);
                        qty.setPadding(0, 0, 3, 0);
                        TextView optionprice = new TextView(Ced_activity);
                        optionprice.setTextSize(12);
                        optionprice.setText(Ced_activity.getResources().getString(R.string.price) + parts[3]);
                        ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(optionprice);
                        layout1.addView(qty, 0);
                        layout1.addView(optionprice, 1);
                        layout2.addView(tittle);
                        layout2.addView(layout1);
                    } else {
                        TextView label = new TextView(Ced_activity);
                        label.setTextSize(12);
                        label.setTextColor(Ced_activity.getResources().getColor(R.color.black));
                        label.setText(parts[0]);
                        ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(label);
                        Ced_labeltag = parts[0];
                        TextView tittle = new TextView(Ced_activity);
                        tittle.setTextSize(12);
                        tittle.setText(parts[1]);
                        ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(tittle);
                        LinearLayout layout1 = new LinearLayout(Ced_activity);
                        layout1.setPadding(0, 0, 0, 10);
                        layout1.setOrientation(LinearLayout.HORIZONTAL);
                        TextView qty = new TextView(Ced_activity);
                        qty.setTextSize(12);
                        qty.setPadding(0, 0, 3, 0);
                        ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(qty);
                        qty.setText(Ced_activity.getResources().getString(R.string.qty) + parts[2]);
                        TextView optionprice = new TextView(Ced_activity);
                        optionprice.setTextSize(12);
                        ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(optionprice);
                        optionprice.setText(Ced_activity.getResources().getString(R.string.price) + parts[3]);
                        layout1.addView(qty, 0);
                        layout1.addView(optionprice, 1);
                        layout2.addView(label);
                        layout2.addView(tittle);
                        layout2.addView(layout1);
                    }
                }
            }
            layout.addView(layout2);
            holder.listBinding.budleoption.addView(layout);
            Ced_flag = true;
            Ced_count++;
        }
        if (Objects.requireNonNull(cartItem.get("Product_type")).equals("configurable")) {
            holder.listBinding.confioption.setVisibility(View.VISIBLE);
            holder.listBinding.budleoption.setVisibility(View.GONE);
            HashMap<String, String> config_Ced_data = Ced_value_config.get(cartItem.get(Ced_CartListing.ITEMID));
            if (Ced_config_flag) {
                holder.listBinding.confioption.removeAllViewsInLayout();
            }
            for (Map.Entry<String, String> stringStringEntry : Objects.requireNonNull(config_Ced_data).entrySet()) {
                String key = String.valueOf(((Map.Entry) stringStringEntry).getKey());
                String value = (String) ((Map.Entry) stringStringEntry).getValue();

                LinearLayout layout = new LinearLayout(Ced_activity);
                layout.setOrientation(LinearLayout.HORIZONTAL);

                TextView label = new TextView(Ced_activity);
                ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(label);
                label.setTextColor(Ced_activity.getResources().getColor(R.color.black));
                label.setText(value);
                label.setTextSize(12);
                label.setPadding(0, 0, 12, 0);
                layout.addView(label, 0);

                TextView colon = new TextView(Ced_activity);
                ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(colon);
                colon.setTextColor(Ced_activity.getResources().getColor(R.color.black));
                colon.setText(":");
                colon.setTextSize(12);
                colon.setPadding(0, 0, 12, 0);
                layout.addView(colon, 1);

                TextView optionvalue = new TextView(Ced_activity);
                optionvalue.setText(key);
                optionvalue.setTextSize(12);
                ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(optionvalue);
                layout.addView(optionvalue, 2);


                layout.setPadding(0, 0, 0, 8);
                holder.listBinding.confioption.addView(layout);

                Ced_config_flag = true;
            }
            Ced_count_config++;
        }
        if (Objects.requireNonNull(cartItem.get("Product_type")).equals("simple") || Objects.requireNonNull(cartItem.get("Product_type")).equals("downloadable")) {
            holder.listBinding.confioption.setVisibility(View.GONE);
            holder.listBinding.budleoption.setVisibility(View.GONE);
        }
        if (Objects.requireNonNull(cartItem.get("Product_type")).equals("virtual")) {
            holder.listBinding.confioption.setVisibility(View.GONE);
            holder.listBinding.budleoption.setVisibility(View.GONE);
        }

        if (cartItem.containsKey("item_error")) {

            try {
                JSONArray err = new JSONArray(cartItem.get("item_error"));
                if (err.length() >= 1) {
                    String error_text = "";
                    for (int k = 0; k < err.length(); k++) {
                        JSONObject o = (JSONObject) err.get(k);
                        if (o.has("type") && o.has("text")) {
                            String type = o.getString("type");
                            String text = o.getString("text");
                            if (type.equals("error")) {
                                holder.listBinding.increase.setVisibility(View.GONE);
                                holder.listBinding.decrese.setVisibility(View.GONE);
                                holder.listBinding.errormsg.setTextColor(Color.RED);
                                if (holder.listBinding.errormsg.getText().toString().isEmpty()) {
                                    error_text = error_text + text;
                                } else {
                                    error_text = error_text + "\n" + text;
                                }
                                // errormsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.warning, 0, 0, 0);
                            }
                            if (type.equals("notice")) {
                                if (fromcartlisting) {
                                    holder.listBinding.increase.setVisibility(View.VISIBLE);
                                    holder.listBinding.decrese.setVisibility(View.VISIBLE);
                                }
                                holder.listBinding.errormsg.setTextColor(Color.parseColor("#CD9035"));
                                if (holder.listBinding.errormsg.getText().toString().isEmpty()) {
                                    error_text = error_text + text;
                                } else {
                                    error_text = error_text + "\n" + text;
                                }
                            }
                            holder.listBinding.errormsg.setVisibility(View.VISIBLE);
                            holder.listBinding.errormsg.setText(error_text);
                        }
                    }
                } else {
                    if (fromcartlisting) {
                        holder.listBinding.increase.setVisibility(View.VISIBLE);
                        holder.listBinding.decrese.setVisibility(View.VISIBLE);
                    }
                    holder.listBinding.errormsg.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        //this is code is use to reset the color of minus button when quantity is 1 or less than one
        if (Integer.parseInt(holder.listBinding.Quantity.getText().toString()) <= 1)
            holder.listBinding.decrese.performClick();
        //this is code is use to reset the color of minus button when quantity is 1 or less than one end
//        holder.listBinding.editsection.setOnClickListener(v -> {
//            JsonObject updateCartData = new JsonObject();
//            try {
//                if (((Ced_NavigationActivity) Ced_activity).session.isLoggedIn()) {
//                    updateCartData.addProperty("customer_id", ((Ced_NavigationActivity) Ced_activity).session.getCustomerid());
//                    if (((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getCartId() != null) {
//                        updateCartData.addProperty("cart_id", ((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getCartId());
//                    }
//                    updateCartData.addProperty("item_id", holder.listBinding.itemid.getText().toString());
//                    if (((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getStoreId() != null) {
//                        updateCartData.addProperty("store_id", ((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getStoreId());
//                    }
//                    updateCartData.addProperty("qty", holder.listBinding.Quantity.getText().toString());
//                } else {
//                    updateCartData.addProperty("cart_id", ((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getCartId());
//                    updateCartData.addProperty("item_id", holder.listBinding.itemid.getText().toString());
//                    if (((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getStoreId() != null) {
//                        updateCartData.addProperty("store_id", ((Ced_NavigationActivity) Ced_activity).cedSessionManagement.getStoreId());
//                    }
//                    updateCartData.addProperty("qty", holder.listBinding.Quantity.getText().toString());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            cartViewModel.updateCart(Ced_activity, updateCartData).observe((FragmentActivity) Ced_activity, apiResponse -> {
//                switch (apiResponse.status) {
//                    case SUCCESS:
//                        Ced_Jstring = apiResponse.data;
//                        try {
//
//                            JSONObject jsonObject = new JSONObject(Ced_Jstring);
//                            if (jsonObject.has("header") && jsonObject.getString("header").equals("false")) {
//                                Intent intent = new Intent(Ced_activity, Ced_UnAuthourizedRequestError.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                Ced_activity.startActivity(intent);
//                            } else {
//                                if (jsonObject.getString("success").equals("true")) {
//                                    //((Ced_NavigationActivity)Ced_activity).showmsg(jsonObject.getString("message"));
//                                    // refreshlistview();
//                                    if (Ced_activity instanceof Ced_CartListing) {
//                                        ((Ced_CartListing) Ced_activity).request();
//                                    }
//                                } else {
//                                    holder.listBinding.Quantity.setText(holder.listBinding.Quantity.getTag().toString());
//                                    holder.listBinding.productQuantity.setText(holder.listBinding.Quantity.getTag().toString());
//                                      /*  if(Ced_CartListing.updatedquantitywithid.containsKey(holder.listBinding.productId.getText()))
//                                        {
//                                            Ced_CartListing.updatedquantitywithid.remove(String.valueOf( holder.listBinding.productId.getText()));
//                                        }
//                                        Ced_CartListing.updatedquantitywithid.put(String.valueOf( holder.listBinding.productId.getText()),holder.listBinding.Quantity.getTag().toString());
//                                       */
//                                    ((Ced_NavigationActivity) Ced_activity).showmsg(jsonObject.getString("message"));
//                                }
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        break;
//
//                    case ERROR:
//                        Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
//                        ((Ced_NavigationActivity) Ced_activity).showmsg(Ced_activity.getResources().getString(R.string.errorString));
//                        break;
//                }
//            });
//
//        });
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return Ced_data.size();
    }

    private Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(500);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    private void refreshlistview() {
        Handler handle = new Handler();
        handle.postDelayed(() -> {
            Intent intent = new Intent(Ced_activity, Ced_CartListing.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Ced_activity.startActivity(intent);
            Ced_activity.overridePendingTransition(0, 0);
        }, 1000);
    }

    private Animation outToRightAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(500);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }

    public class CartListViewHolder extends RecyclerView.ViewHolder {
        //        MagenativeCartCompListBinding listBinding;
        NewCartProductItemBinding listBinding;

        //        public CartListViewHolder(@NonNull MagenativeCartCompListBinding listBinding) {
        public CartListViewHolder(@NonNull NewCartProductItemBinding listBinding) {
//            super(listBinding.getRoot());
            super(listBinding.getRoot());

            this.listBinding = listBinding;
        }
    }
}