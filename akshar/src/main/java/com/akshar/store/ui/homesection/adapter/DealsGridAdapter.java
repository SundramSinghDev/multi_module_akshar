package com.akshar.store.ui.homesection.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.akshar.store.R;
import com.akshar.store.ui.websection.Ced_Weblink;
import com.akshar.store.databinding.DealBinding;
import com.akshar.store.ui.homesection.activity.Ced_New_home_page;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;
import com.akshar.store.ui.productsection.activity.Ced_New_Product_Listing;

import org.json.JSONArray;
import org.json.JSONObject;

public class DealsGridAdapter extends RecyclerView.Adapter<DealsGridAdapter.DealsGridViewHolder> {
    private Context context;
    private JSONArray content;

    public DealsGridAdapter(Context context, JSONArray content) {
        this.context = context;
        this.content = content;
    }

    @NonNull
    @Override
    public DealsGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DealBinding dealBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.deal, parent, false);
        return new DealsGridViewHolder(dealBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DealsGridViewHolder holder, int position) {
        try{
            JSONObject deal = content.getJSONObject(position);

            if (deal.getString("deal_type").equals("1")) {
                holder.dealBinding.dealid.setText(deal.getString("product_link"));
            }
            if (deal.getString("deal_type").equals("2")) {
                holder.dealBinding.dealid.setText(deal.getString("category_link"));
            }
            if (deal.getString("deal_type").equals("3")) {
                holder.dealBinding.dealid.setText(deal.getString("static_link"));
            }

            Glide.with(context)
                    .load(deal.getString("deal_image_name"))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(holder.dealBinding.staticdeal);

            Log.i("DealText", "" + deal.getString("offer_text"));
            holder.dealBinding.dealtype.setText(deal.getString("deal_type"));
            holder.dealBinding.dealoffertext.setText(deal.getString("offer_text"));

            final ImageView finalStaticdeal = holder.dealBinding.staticdeal;
            holder.dealBinding.staticdeal.setOnClickListener(v -> {
                RelativeLayout dealparent = (RelativeLayout) finalStaticdeal.getParent();
                TextView deal_type = (TextView) dealparent.getChildAt(1);
                TextView deal_id = (TextView) dealparent.getChildAt(2);
                if (deal_type.getText().toString().equals("1")) {
                    Intent intent = new Intent(context, Ced_NewProductView.class);
                    intent.putExtra("product_id", deal_id.getText().toString());
                    context.startActivity(intent);
                    ((Ced_New_home_page) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
                if (deal_type.getText().toString().equals("2")) {
                    Intent intent = new Intent(context, Ced_New_Product_Listing.class);
                    intent.putExtra("ID", deal_id.getText().toString());
                    context.startActivity(intent);
                    ((Ced_New_home_page) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
                if (deal_type.getText().toString().equals("3")) {
                    Intent intent = new Intent(context, Ced_Weblink.class);
                    intent.putExtra("link", deal_id.getText().toString());
                    context.startActivity(intent);
                    ((Ced_New_home_page) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return content.length();
    }

    public class DealsGridViewHolder extends RecyclerView.ViewHolder {
        DealBinding dealBinding;

        public DealsGridViewHolder(@NonNull DealBinding dealBinding) {
            super(dealBinding.getRoot());

            this.dealBinding = dealBinding;
        }
    }
}
