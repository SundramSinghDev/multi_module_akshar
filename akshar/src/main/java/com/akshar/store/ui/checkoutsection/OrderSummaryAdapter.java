package com.akshar.store.ui.checkoutsection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.NewCartProductItemBinding;
import com.akshar.store.databinding.OrderedItemViewForOrderSummaryBinding;
import com.akshar.store.ui.cartsection.activity.Ced_CartListing;
import com.akshar.store.ui.cartsection.viewmodel.CartViewModel;
import com.akshar.store.ui.networkhandlea_activities.Ced_UnAuthourizedRequestError;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.ViewHolder> {


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
    public OrderSummaryAdapter(Activity a, ArrayList<HashMap<String, String>> d, Boolean fromcartlisting) {
        Ced_activity = a;
        Ced_data = d;
        this.fromcartlisting = fromcartlisting;
        cartViewModel = new ViewModelProvider((FragmentActivity) Ced_activity, viewModelFactory).get(CartViewModel.class);
//        setHasStableIds(true);
    }

    //constructor for cartlisting
    public OrderSummaryAdapter(Activity a, ArrayList<HashMap<String, String>> d, HashMap<String, ArrayList<String>> val, String param, Boolean fromcartlisting) {
        Ced_value = val;
        Ced_activity = a;
        Ced_data = d;
        this.fromcartlisting = fromcartlisting;
        cartViewModel = new ViewModelProvider((FragmentActivity) Ced_activity, viewModelFactory).get(CartViewModel.class);
//        setHasStableIds(true);
    }

    //constructor for cartlisting
    public OrderSummaryAdapter(Activity a, ArrayList<HashMap<String, String>> d, HashMap<String, HashMap<String, String>> val, Boolean fromcartlisting) {
        Ced_value_config = val;
        Ced_activity = a;
        Ced_data = d;
        this.fromcartlisting = fromcartlisting;
        cartViewModel = new ViewModelProvider((FragmentActivity) Ced_activity, viewModelFactory).get(CartViewModel.class);
//        setHasStableIds(true);
    }

    //constructor for cartlisting
    public OrderSummaryAdapter(Activity a, ArrayList<HashMap<String, String>> d, HashMap<String, ArrayList<String>> bundle, HashMap<String, HashMap<String, String>> val, Boolean fromcartlisting) {
        Ced_value = bundle;
        Ced_value_config = val;
        Ced_activity = a;
        Ced_data = d;
        this.fromcartlisting = fromcartlisting;
        cartViewModel = new ViewModelProvider((FragmentActivity) Ced_activity, viewModelFactory).get(CartViewModel.class);
//        setHasStableIds(true);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderedItemViewForOrderSummaryBinding listBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.ordered_item_view_for_order_summary, parent, false);
        return new ViewHolder(listBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            HashMap<String, String> cartItem = Ced_data.get(position);
            ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(holder.listBinding.price);
            ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(holder.listBinding.productQuantity);
            ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(holder.listBinding.quantitylabel);
            ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(holder.listBinding.productName);
            ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(holder.listBinding.productPrice);

            holder.listBinding.productQuantity.setVisibility(View.VISIBLE);
            holder.listBinding.quantitylabel.setVisibility(View.VISIBLE);
            holder.listBinding.productImage.getLayoutParams().height = (int) Ced_activity.getResources().getDimension(R.dimen.dim_80dp);
            holder.listBinding.productImage.getLayoutParams().width = (int) Ced_activity.getResources().getDimension(R.dimen.dim_80dp);

            holder.listBinding.productImage.setOnClickListener(v -> {
                Intent listintent = new Intent(Ced_activity, Ced_NewProductView.class);
                listintent.putExtra("product_id", holder.listBinding.productId.getText().toString());
                listintent.putExtra("CURRENT", position);
                Ced_activity.startActivity(listintent);
            });

            holder.listBinding.productName.setText(cartItem.get(Ced_CartListing.KEY_NAME));
            holder.listBinding.productId.setText(cartItem.get(Ced_CartListing.KEY_ID));
            holder.listBinding.itemid.setText(cartItem.get(Ced_CartListing.ITEMID));
            holder.listBinding.productPrice.setText(cartItem.get(Ced_CartListing.KEY_Price));

            holder.listBinding.productQuantity.setText(cartItem.get(Ced_CartListing.Key_Quantity));

            Glide.with(Ced_activity)
                    .load(cartItem.get(Ced_CartListing.KEY_Image))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(holder.listBinding.productImage);

            holder.listBinding.viewDetailsTv.setOnClickListener(view -> {
                if (holder.listBinding.budleoption.getChildCount() > 0) {
                    if (holder.listBinding.budleoption.getVisibility() == View.VISIBLE) {
                        Ced_NavigationActivity.collapse(holder.listBinding.budleoption);
                    } else {
                        Ced_NavigationActivity.expand(holder.listBinding.budleoption);
                    }
                } else if (holder.listBinding.confioption.getChildCount() > 0) {
                    if (holder.listBinding.confioption.getVisibility() == View.VISIBLE) {
                        Ced_NavigationActivity.collapse(holder.listBinding.confioption);
                    } else {
                        Ced_NavigationActivity.expand(holder.listBinding.confioption);
                    }
                }
            });

            if (Objects.requireNonNull(cartItem.get("Product_type")).equals("bundle")) {
                //TODO
//                holder.listBinding.budleoption.setVisibility(View.VISIBLE);
                holder.listBinding.viewDetailsTv.setVisibility(View.VISIBLE);
//                holder.listBinding.confioption.setVisibility(View.GONE);
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
                holder.listBinding.viewDetailsTv.setVisibility(View.VISIBLE);
//                holder.listBinding.confioption.setVisibility(View.VISIBLE);
//                holder.listBinding.budleoption.setVisibility(View.GONE);
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
            if (Objects.requireNonNull(cartItem.get("Product_type")).equals("simple") || Objects.requireNonNull(cartItem.get("Product_type")).equals("downloadable")
                    || Objects.requireNonNull(cartItem.get("Product_type")).equals("virtual")) {
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
                                    holder.listBinding.errormsg.setTextColor(Color.RED);
                                    if (holder.listBinding.errormsg.getText().toString().isEmpty()) {
                                        error_text = error_text + text;
                                    } else {
                                        error_text = error_text + "\n" + text;
                                    }
                                    // errormsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.warning, 0, 0, 0);
                                }
                                if (type.equals("notice")) {
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
                        holder.listBinding.errormsg.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(holder.listBinding.viewDetailsTv);
            ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(holder.listBinding.price);
            ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(holder.listBinding.quantitylabel);
            ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(holder.listBinding.productPrice);
            ((Ced_NavigationActivity) Ced_activity).set_regular_font_fortext(holder.listBinding.productQuantity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return Ced_data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        OrderedItemViewForOrderSummaryBinding listBinding;

        public ViewHolder(@NonNull OrderedItemViewForOrderSummaryBinding itemView) {
            super(itemView.getRoot());
            listBinding = itemView;
        }
    }
}
