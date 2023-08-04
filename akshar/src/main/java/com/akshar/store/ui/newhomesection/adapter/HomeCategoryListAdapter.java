package com.akshar.store.ui.newhomesection.adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.bumptech.glide.Glide;
import com.akshar.store.R;
import com.akshar.store.databinding.HomeCategoryListItemBinding;
import com.akshar.store.ui.homesection.activity.Ced_New_home_page;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;
import com.akshar.store.ui.productsection.activity.Ced_New_Product_Listing;
import com.akshar.store.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeCategoryListAdapter extends RecyclerView.Adapter<HomeCategoryListAdapter.HomecategoryListViewHolder> {

    private Context context;
    private JSONArray jsonArray;
    private LayoutInflater layoutInflater;
    private String category_type;
    private String category_shape;

    public HomeCategoryListAdapter(Context context, JSONArray jsonArray,String category_type,String category_shape) {
        this.context = context;
        this.jsonArray = jsonArray;
        this.category_type = category_type;
        this.category_shape = category_shape;
    }

    @NonNull
    @Override
    public HomecategoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        HomeCategoryListItemBinding binding= DataBindingUtil.inflate(layoutInflater, R.layout.home_category_list_item,parent,false);
        return new HomecategoryListViewHolder(binding);

       /* View v ;
        if(category_type.equalsIgnoreCase("circular_slider"))
        {
            HomeCategoryListItemBinding binding= DataBindingUtil.inflate(layoutInflater, R.layout.home_category_list_item,parent,false);
            *//*return new HomecategoryListViewHolder(binding);*//*
            v=binding.getRoot();
        }
        else (category_type.equalsIgnoreCase("square_slider"))
        {
            HomeCategoryListItemBinding binding= DataBindingUtil.inflate(layoutInflater, R.layout.home_category_list_item,parent,false);
            *//*return new HomecategoryListViewHolder(binding);*//*
            v=binding.getRoot();
        }
        return  v;*/

    }

    @Override
    public void onBindViewHolder(@NonNull HomecategoryListViewHolder holder, int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            switch (category_shape)
            {
                case "circular":
                    if(category_type.equalsIgnoreCase("grid"))
                    {
                        holder.binding.circularImage.getLayoutParams().width= (int) ((Ced_NavigationActivity.device_width/3)-25);
                        holder.binding.circularImage.getLayoutParams().height= (int) ((Ced_NavigationActivity.device_width/3)-25);
                    }
                    holder.binding.circularImage.setVisibility(View.VISIBLE);
                    holder.binding.categoryImageCard.setVisibility(View.GONE);

                    Glide.with(context)
                            .load(jsonObject.getString("image"))
                            .error(R.drawable.placeholder)
                            .placeholder(R.drawable.placeholder)
                            .into(holder.binding.circularImage);
                    holder.binding.circularImage.setOnClickListener(new View.OnClickListener() {
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
                    break;
                case "square":
                    if(category_type.equalsIgnoreCase("grid"))
                    {
                        holder.binding.categoryImageCard.getLayoutParams().width= (int) ((Ced_NavigationActivity.device_width/3));
                        holder.binding.categoryImageCard.getLayoutParams().height= (int) ((Ced_NavigationActivity.device_width/3));
                    }
                    holder.binding.circularImage.setVisibility(View.GONE);
                    holder.binding.categoryImageCard.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(jsonObject.getString("image"))
                            .error(R.drawable.placeholder)
                            .placeholder(R.drawable.placeholder)
                            .into(holder.binding.categoryImage);
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
                    break;
                default:
                    holder.binding.circularImage.setVisibility(View.GONE);
                    holder.binding.categoryImageCard.setVisibility(View.GONE);
                    break;
            }


            holder.binding.categoryName.setText(jsonObject.getString("name"));
            Log.d(Urls.TAG, "onBindViewHolder: "+holder.binding.categoryImage.getId());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }


    public class HomecategoryListViewHolder extends RecyclerView.ViewHolder /* implements View.OnClickListener*/ {
        public HomeCategoryListItemBinding binding;
        public HomecategoryListViewHolder(HomeCategoryListItemBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
       /* @Override
        public void onClick(View view) {
            if (view.getId() == binding.categoryImage.getId()) {
                try {
                    Intent intent = new Intent(context, Ced_New_Product_Listing.class);
                    //  Intent intent = new Intent(context, ProductViewNewActivity.class);
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

