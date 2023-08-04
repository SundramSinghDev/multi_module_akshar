package com.akshar.store.ui.newhomesection.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.HomeCategoryListItemBinding;
import com.akshar.store.databinding.HomeDealItemBinding;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.akshar.store.ui.productsection.activity.Ced_New_Product_Listing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeDealSliderAdapter extends RecyclerView.Adapter<HomeDealSliderAdapter.HomecategoryListViewHolder> {

    private Context context;
    private JSONArray jsonArray;
    private LayoutInflater layoutInflater;

    public HomeDealSliderAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public HomecategoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        HomeDealItemBinding binding= DataBindingUtil.inflate(layoutInflater, R.layout.home_deal_item,parent,false);
        return new HomecategoryListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomecategoryListViewHolder holder, int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            String link_to=jsonObject.getString("link_to");
            String product_id=jsonObject.getString("product_id");
            holder.binding.image.getLayoutParams().width= (Ced_NavigationActivity.device_width/(2));
            holder.binding.image.getLayoutParams().height= (Ced_NavigationActivity.device_width/(3));
            Glide.with(context)
                    .load(jsonObject.getString("deal_image_name"))
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.binding.image);
            if(context instanceof Magenative_HomePageNewTheme)
            {
                ((Magenative_HomePageNewTheme)context).BannerIntent(holder.binding.image,link_to,product_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }


    public class HomecategoryListViewHolder extends RecyclerView.ViewHolder /* implements View.OnClickListener*/ {
        public HomeDealItemBinding binding;
        public HomecategoryListViewHolder(HomeDealItemBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
