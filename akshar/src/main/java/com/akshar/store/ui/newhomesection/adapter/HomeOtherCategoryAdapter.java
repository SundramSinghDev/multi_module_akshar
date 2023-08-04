package com.akshar.store.ui.newhomesection.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.akshar.store.R;
import com.akshar.store.databinding.OthersCategoryListItemBinding;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.akshar.store.ui.productsection.activity.Ced_New_Product_Listing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeOtherCategoryAdapter extends RecyclerView.Adapter<HomeOtherCategoryAdapter.HomeOtherCategoryViewHolder> {

    private Context context;
    private JSONArray jsonArray;
LayoutInflater layoutInflater;

    public HomeOtherCategoryAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public HomeOtherCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*View view = LayoutInflater.from(context).inflate(R.layout.others_category_list_item, parent, false);
        return new HomeOtherCategoryViewHolder(view);*/
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        OthersCategoryListItemBinding binding= DataBindingUtil.inflate(layoutInflater, R.layout.others_category_list_item,parent,false);
        return new HomeOtherCategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeOtherCategoryViewHolder holder, int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            Glide.with(context)
                    .load(jsonObject.getString("image"))
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.binding.categoryImage);
            holder.binding.categoryName.setText(jsonObject.getString("name"));
            holder.binding.categoryImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(context, Ced_New_Product_Listing.class);
                        intent.putExtra("ID", jsonArray.getJSONObject(position).getString("id"));
                        context.startActivity(intent);
                        ((Magenative_HomePageNewTheme) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class HomeOtherCategoryViewHolder extends RecyclerView.ViewHolder/* implements View.OnClickListener*/ {

        public OthersCategoryListItemBinding binding;
        public HomeOtherCategoryViewHolder(OthersCategoryListItemBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }

       /* @Override
        public void onClick(View view) {
                if (view.getId() == binding.categoryImage.getId()) {
                    try {
                        Intent intent = new Intent(context, Ced_New_Product_Listing.class);
                        intent.putExtra("ID", jsonArray.getJSONObject(getAdapterPosition()).getString("id"));
                        context.startActivity(intent);
                        ((Magenative_HomePageNewTheme) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }*/

    }
}
