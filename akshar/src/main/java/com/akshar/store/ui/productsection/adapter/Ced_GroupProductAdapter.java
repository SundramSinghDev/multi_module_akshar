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
package com.akshar.store.ui.productsection.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.MagenativeGroupProductBinding;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Ced_GroupProductAdapter extends RecyclerView.Adapter<Ced_GroupProductAdapter.GroupViewHolder> {
    //Variable and Object Initializations
    private Context activity;
    private ArrayList<HashMap<String, String>> data;

    public Ced_GroupProductAdapter(Context a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MagenativeGroupProductBinding productBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.magenative_group_product, parent, false);
        return new GroupViewHolder(productBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        HashMap<String, String> product = data.get(position);

        ((Ced_NavigationActivity)activity).set_regular_font_fortext(holder.productBinding.productName);
        ((Ced_NavigationActivity)activity).set_regular_font_fortext(holder.productBinding.productPrice);
        ((Ced_NavigationActivity)activity).set_regular_font_fortext(holder.productBinding.price);
        ((Ced_NavigationActivity)activity).set_regular_font_fortext(holder.productBinding.productSpecialPrice);
        ((Ced_NavigationActivity)activity).set_regular_font_fortext(holder.productBinding.stockgroup);
        ((Ced_NavigationActivity)activity).set_regular_font_fortext(holder.productBinding.quantity);

        holder.productBinding.productName.setText(product.get("group-product-name"));
        holder.productBinding.productId.setText(product.get("group-product-id"));
        holder.productBinding.stockgroup.setText(product.get("stock"));

        if (Objects.requireNonNull(product.get("special_price")).equals("null")) {
            holder.productBinding.productPrice.setPaintFlags(holder.productBinding.productPrice.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.productBinding.productPrice.setText(product.get("regular_price"));
        } else {
            holder.productBinding.productSpecialPrice.setVisibility(View.VISIBLE);
            holder.productBinding.productSpecialPrice.setText(product.get("special_price"));
            holder.productBinding.productPrice.setText(product.get("regular_price"));
            holder.productBinding.productPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.productBinding.productSpecialPrice.setTextColor(activity.getResources().getColor(R.color.red));
        }

        if (Objects.requireNonNull(product.get("stock")).equals("IN STOCK")) {
            holder.productBinding.stockgroup.setTextColor(activity.getResources().getColor(R.color.green));
            holder.productBinding.quantity.setVisibility(View.VISIBLE);
        } else {
            holder.productBinding.stockgroup.setTextColor(activity.getResources().getColor(R.color.red));
        }

        Glide.with(activity)
                .load(product.get("group-prod-img"))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .override(75, 75)
                .into(holder.productBinding.productImage);

        holder.productBinding.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ((Ced_NewProductView)activity).groupdatatoaddtocart.put(holder.productBinding.productId.getText().toString(), holder.productBinding.quantity.getText().toString());
            }
        });
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        MagenativeGroupProductBinding productBinding;

        public GroupViewHolder(@NonNull MagenativeGroupProductBinding productBinding) {
            super(productBinding.getRoot());

            this.productBinding = productBinding;
        }
    }
}
