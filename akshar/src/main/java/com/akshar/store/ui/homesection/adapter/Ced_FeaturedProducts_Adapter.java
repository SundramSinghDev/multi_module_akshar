package com.akshar.store.ui.homesection.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.akshar.store.R;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;
import com.akshar.store.databinding.FeaturedProductLayoutBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Ced_FeaturedProducts_Adapter extends RecyclerView.Adapter<Ced_FeaturedProducts_Adapter.Ced_FeaturedProducts_ViewHolder> {
    private Context context;
    private ArrayList<HashMap<String,String>> productsJsonArray;

    public Ced_FeaturedProducts_Adapter(Context context, ArrayList<HashMap<String, String>> productsjsonArray) {
        this.context = context;
        this.productsJsonArray = productsjsonArray;
    }

    @NonNull
    @Override
    public Ced_FeaturedProducts_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        FeaturedProductLayoutBinding layoutBinding = DataBindingUtil.inflate(inflater,
                R.layout.featured_product_layout, parent, false);
        return new Ced_FeaturedProducts_ViewHolder(layoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull Ced_FeaturedProducts_ViewHolder holder, int position) {
        try {
            HashMap<String, String> object = productsJsonArray.get(position);

            holder.layoutBinding.productName.setText(object.get("product_name"));

            Glide.with(context)
                    .load(object.get("product_image"))

                    .dontTransform()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(holder.layoutBinding.productImage);
            holder.layoutBinding.productId.setText(object.get("product_id"));

            //-------------------------

            if(object.get("stock_status").equals("false"))
            {
                holder.layoutBinding.MageNativeStocksection.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.layoutBinding.MageNativeStocksection.setVisibility(View.GONE);
            }

            //----------------------------

            if (object.get("special_price").equals("no_special")) {
                holder.layoutBinding.specialPrice.setVisibility(View.GONE);
                holder.layoutBinding.normalPrice.setPaintFlags(holder.layoutBinding.normalPrice.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.layoutBinding.normalPrice.setTextColor(context.getResources().getColor(R.color.black));
                holder.layoutBinding.normalPrice.setText(object.get("regular_price"));
            } else {
                holder.layoutBinding.specialPrice.setVisibility(View.VISIBLE);
                holder.layoutBinding.specialPrice.setText(object.get("special_price"));
                holder.layoutBinding.normalPrice.setText(object.get("regular_price"));
                holder.layoutBinding.normalPrice.setTextColor(context.getResources().getColor(R.color.main_color_gray));
                holder.layoutBinding.normalPrice.setPaintFlags(holder.layoutBinding.normalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            final TextView finalProduct_id = holder.layoutBinding.productId;
            final TextView finalProduct_name1 = holder.layoutBinding.productName;

            holder.layoutBinding.productCard.setOnClickListener(v -> {
                Intent intent = new Intent(context, Ced_NewProductView.class);
                intent.putExtra("product_id", finalProduct_id.getText().toString());
                intent.putExtra("product_name", finalProduct_name1.getText().toString());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return productsJsonArray.size();
    }

    public class Ced_FeaturedProducts_ViewHolder extends RecyclerView.ViewHolder {
        FeaturedProductLayoutBinding layoutBinding;

        public Ced_FeaturedProducts_ViewHolder(@NonNull FeaturedProductLayoutBinding layoutBinding) {
            super(layoutBinding.getRoot());
            this.layoutBinding = layoutBinding;
        }
    }

}
