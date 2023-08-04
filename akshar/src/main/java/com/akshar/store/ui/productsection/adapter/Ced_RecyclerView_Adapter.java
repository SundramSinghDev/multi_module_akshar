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
import android.content.Intent;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.akshar.store.ui.productsection.model.Ced_Data_Model;
import com.akshar.store.R;
import com.akshar.store.databinding.MagenativeItemRowBinding;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;

import java.util.ArrayList;

public class Ced_RecyclerView_Adapter extends RecyclerView.Adapter<Ced_RecyclerView_Adapter.Ced_RecyclerViewHolder> {
    private ArrayList<Ced_Data_Model> arrayList;
    private ArrayList<String> ids;
    private Context context;

    public Ced_RecyclerView_Adapter(Context context, ArrayList<Ced_Data_Model> arrayList, ArrayList<String> ids) {
        this.context = context;
        this.arrayList = arrayList;
        this.ids = ids;
    }

    public Ced_RecyclerView_Adapter(Context context, ArrayList<Ced_Data_Model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    @Override
    public void onBindViewHolder(@NonNull Ced_RecyclerViewHolder holder, int position) {
        final Ced_Data_Model model = arrayList.get(position);

        Glide.with(context)
                .load(model.getImage())
                .placeholder(R.drawable.placeholder)// optional
                .error(R.drawable.placeholder)
                .override(150, 150)
                .into(holder.layoutBinding.MageNativeImage);


        if(model.getStock_status().equals("IN STOCK"))
        {
            holder.layoutBinding.MageNativeStocksection.setVisibility(View.GONE);
            Log.i("regularspecialprice","regular "+model.getRegular_price()+"special "+model.getSpecial_price());
            if(!model.getSpecial_price().equals("null") && !model.getSpecial_price().equals("no_sp") && model.getSpecial_price() != null){
                holder.layoutBinding.MageNativeRegularprice.setPaintFlags(holder.layoutBinding.MageNativeRegularprice.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.layoutBinding.MageNativeRegularprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.layoutBinding.MageNativeRegularprice.setText(model.getCurrencySymbol()+" "+model.getRegular_price());
                holder.layoutBinding.MageNativeSpecialprice.setText(model.getCurrencySymbol()+" "+model.getSpecial_price());
                holder.layoutBinding.MageNativeSpecialprice.setTextColor(context.getResources().getColor(R.color.onwhitetextcolor));
                holder.layoutBinding.MageNativeRegularprice.setTextColor(context.getResources().getColor(R.color.black));
            }
            else {
                holder.layoutBinding.MageNativeRegularprice.setTextColor(context.getResources().getColor(R.color.onwhitetextcolor));
                holder.layoutBinding.MageNativeRegularprice.setText(model.getCurrencySymbol()+" "+model.getRegular_price());
            }
        }
        else
        {
            holder.layoutBinding.MageNativeStocksection.setVisibility(View.VISIBLE);
        }

        Log.i("regularspecialprice", "regular " + model.getRegular_price() + "special " + model.getSpecial_price());
        if (!model.getSpecial_price().equals("null") && !model.getSpecial_price().equals("no_sp") && model.getSpecial_price() != null) {
            holder.layoutBinding.MageNativeRegularprice.setPaintFlags(holder.layoutBinding.MageNativeRegularprice.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.layoutBinding.MageNativeRegularprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.layoutBinding.MageNativeRegularprice.setText(model.getCurrencySymbol() + " " + model.getRegular_price());
            holder.layoutBinding.MageNativeSpecialprice.setText(model.getCurrencySymbol() + " " + model.getSpecial_price());
            holder.layoutBinding.MageNativeSpecialprice.setTextColor(context.getResources().getColor(R.color.onwhitetextcolor));
            holder.layoutBinding.MageNativeRegularprice.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            holder.layoutBinding.MageNativeRegularprice.setTextColor(context.getResources().getColor(R.color.onwhitetextcolor));
            holder.layoutBinding.MageNativeRegularprice.setText(model.getCurrencySymbol() + " " + model.getRegular_price());
        }

        holder.layoutBinding.MageNativeTitle.setText(model.getTitle());
        holder.layoutBinding.MageNativeId.setText(model.getId());
        holder.layoutBinding.MageNativeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Ced_NewProductView.class);
                intent.putExtra("product_id", holder.layoutBinding.MageNativeId.getText().toString());
                intent.putExtra("CURRENT", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public Ced_RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

       /* ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.magenative_item_row, viewGroup, false);
        return new Ced_RecyclerViewHolder(mainGroup, ids, context);*/
        MagenativeItemRowBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.magenative_item_row, viewGroup, false);
        return new Ced_RecyclerViewHolder(layoutBinding);

    }
    public class Ced_RecyclerViewHolder extends RecyclerView.ViewHolder{
        MagenativeItemRowBinding layoutBinding;

        public Ced_RecyclerViewHolder(MagenativeItemRowBinding layoutBinding) {
            super(layoutBinding.getRoot());

            this.layoutBinding = layoutBinding;
        }
    }
    /*public class Ced_RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView title;
        public TextView id;
        public ImageView imageview;
        public TextView regularprice;
        public TextView specialprice;
        ArrayList<String> ids;
        Context context;
        RelativeLayout MageNative_stocksection;

        public Ced_RecyclerViewHolder(View view, final ArrayList<String> ids, final Context context) {
            super(view);
            this.MageNative_stocksection = (RelativeLayout) view
                    .findViewById(R.id.MageNative_stocksection);
            this.title = view.findViewById(R.id.MageNative_title);
            this.id = view.findViewById(R.id.MageNative_id);
            this.imageview = view.findViewById(R.id.MageNative_image);
            this.regularprice = view.findViewById(R.id.MageNative_regularprice);
            this.specialprice = view.findViewById(R.id.MageNative_specialprice);
            this.ids = ids;
            this.context = context;

            try {
                imageview.setOnClickListener(v -> {
                    Intent intent = new Intent(context, Ced_NewProductView.class);
                    intent.putExtra("product_id", id.getText().toString());
                    intent.putExtra("CURRENT", getPosition());
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, Ced_NewProductView.class);
            intent.putExtra("product_id", ids.get(getPosition()));
            intent.putExtra("CURRENT", getPosition());
            context.startActivity(intent);
        }
    }*/

}