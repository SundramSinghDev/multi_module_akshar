package com.akshar.store.ui.sellersection.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.MagenativeSellerinfoBinding;
import com.akshar.store.ui.sellersection.activity.Ced_SellerListing;
import com.akshar.store.ui.sellersection.activity.Ced_Seller_Shop;
import com.akshar.store.ui.sellersection.viewmodel.SellerListViewModel;
import com.akshar.store.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Ced_SellerList_RecyclerApapter extends RecyclerView.Adapter<Ced_SellerList_RecyclerApapter.SellerListViewHolder> {

    private Context context;
   /* private JSONArray jsonArray;*/

    private ArrayList<HashMap<String, String>> data;
    private LayoutInflater layoutInflater;
    SellerListViewModel sellerListViewModel;

    public Ced_SellerList_RecyclerApapter(Context context, ArrayList<HashMap<String, String>> d, SellerListViewModel sellerListViewModel) {
        this.context = context;
        this.data = d;
        this.sellerListViewModel = sellerListViewModel;
    }

    @NonNull
    @Override
    public SellerListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        MagenativeSellerinfoBinding binding= DataBindingUtil.inflate(layoutInflater, R.layout.magenative_sellerinfo,parent,false);
        return new SellerListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerListViewHolder holder, int position) {
        try
        {
            ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.binding.magenativeShopName);
            ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.binding.magenativeAddress);
            ((Ced_NavigationActivity)context).set_regular_font_fortext(holder.binding.magenativeReviewcount);
            ((Ced_NavigationActivity)context).set_bold_font_fortext(holder.binding.magenativeAveragerating);
            HashMap<String, String> song = data.get(position);
            if (song.containsKey("citycountrey")) {
                holder.binding.magenativeAddress.setText(song.get("citycountrey"));
            }
            if (Objects.requireNonNull(song.get("review")).equals("no_review")) {
                holder.binding.magenativeAveragerating.setVisibility(View.INVISIBLE);
            }
            else {
                holder.binding.magenativeAveragerating.setVisibility(View.VISIBLE);
                holder.binding.magenativeAveragerating.setText(song.get("review"));
                Float aFloat = Float.valueOf(holder.binding.magenativeAveragerating.getText().toString());
                if (aFloat < 2) {
                    holder.binding.magenativeAveragerating.setBackground(context.getResources().getDrawable(R.drawable.round_corner_red));
                }
                if (aFloat >= 2 && aFloat < 4) {
                    holder.binding.magenativeAveragerating.setBackground(context.getResources().getDrawable(R.drawable.round_corner_yellow));
                    holder.binding.magenativeAveragerating.setTextColor(context.getResources().getColor(R.color.black));
                }
                if (aFloat >= 4) {
                    holder.binding.magenativeAveragerating.setBackground(context.getResources().getDrawable(R.drawable.round_corner));
                }
            }

            if (Objects.requireNonNull(song.get("reviewcount")).equals("no_count") || Objects.requireNonNull(song.get("reviewcount")).equals("")) {
                /*holder.binding.magenativeReviewcount.setVisibility(View.INVISIBLE);*/
                holder.binding.magenativeReviewcount.setText("0" + " Reviews");
            }
            else {
                holder.binding.magenativeReviewcount.setVisibility(View.VISIBLE);
                holder.binding.magenativeReviewcount.setText(song.get("reviewcount") + " Reviews");
            }

            holder.binding.magenativeShopName.setText(song.get("public_name"));
            holder.binding.magenativeShopId.setText(song.get("entity_id"));
            //holder.binding.magenativeShopUrl.setText(song.get("shop_url"));

            if (Objects.requireNonNull(song.get("company_logo")).equals("false")) {
                holder.binding.magenativeShopImage.setImageResource(R.drawable.bannerplaceholder);
            }
            else {
                Glide.with(context)
                        .load(song.get("company_logo"))
                        .placeholder(R.drawable.bannerplaceholder)
                        .error(R.drawable.bannerplaceholder)
                        .into(holder.binding.magenativeShopImage);
            }

            holder.binding.maincontainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent sellershop = new Intent(context, Ced_Seller_Shop.class);
                    if (!holder.binding.magenativeShopId.getText().toString().isEmpty()) {
                        sellershop.putExtra("ID", holder.binding.magenativeShopId.getText().toString());
                    }
                    ((Ced_SellerListing)context).startActivity(sellershop);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class SellerListViewHolder extends RecyclerView.ViewHolder /* implements View.OnClickListener*/ {
        public MagenativeSellerinfoBinding binding;
        public SellerListViewHolder(MagenativeSellerinfoBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
