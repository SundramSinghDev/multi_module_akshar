package com.akshar.store.ui.productsection.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;
import com.akshar.store.databinding.ListProductDesignBinding;
import com.akshar.store.databinding.MagenativeNewGridBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class RecyclerGridViewAdapter extends RecyclerView.Adapter<RecyclerGridViewAdapter.GridViewHolder> {
    private Context activity;
    private ArrayList<HashMap<String, String>> data;
    private boolean grid_layout;

    public RecyclerGridViewAdapter(Context a, ArrayList<HashMap<String, String>> d, boolean gridlayout) {
        activity = a;
        data = d;
        grid_layout = gridlayout;
        //setHasStableIds(true);
    }

   /* @Override
    public long getItemId(int position) {
        return position;
    }*/

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (grid_layout) {
            MagenativeNewGridBinding gridBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.magenative_new_grid, parent, false);
            return new GridViewHolder(gridBinding);
        } else {
            ListProductDesignBinding listBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.list_product_design, parent, false);
            return new GridViewHolder(listBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final GridViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        HashMap<String, String> product = data.get(position);
        if (grid_layout) {
            ((Ced_NavigationActivity)activity).set_regular_font_fortext(holder.gridBinding.productName);
            ((Ced_NavigationActivity)activity).set_regular_font_fortext(holder.gridBinding.regularPrice);
            ((Ced_NavigationActivity)activity).set_regular_font_fortext(holder.gridBinding.specialPrice);
            ((Ced_NavigationActivity)activity).set_regular_font_fortext(holder.gridBinding.percentage);
            ((Ced_NavigationActivity)activity).set_regular_font_fortext(holder.gridBinding.offertext);
            ((Ced_NavigationActivity)activity).set_regular_font_fortext(holder.gridBinding.viewdetails);



            holder.gridBinding.mainLayout.setOnClickListener(v -> {
                Intent intent = new Intent(activity, Ced_NewProductView.class);
                intent.putExtra("product_id", holder.gridBinding.productId.getText().toString());
                activity.startActivity(intent);
                ((Activity) activity).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            });


            holder.gridBinding.wishlist.setVisibility(View.VISIBLE);


            if (Objects.requireNonNull(product.get("type")).equals("configurable") ||
                    Objects.requireNonNull(product.get("type")).equals("simple") ||
                    Objects.requireNonNull(product.get("type")).equals("virtual") ||
                    Objects.requireNonNull(product.get("type")).equals("downloadable")) {
                holder.gridBinding.regularPrice.setVisibility(View.VISIBLE);
                holder.gridBinding.viewdetails.setVisibility(View.GONE);
            } else {
                holder.gridBinding.regularPrice.setVisibility(View.GONE);
                holder.gridBinding.viewdetails.setVisibility(View.VISIBLE);
            }

            if (Objects.requireNonNull(product.get("Inwishlist")).equals("IN")) {
                holder.gridBinding.wishlist.setImageResource(R.drawable.wishred);
            } else {
                holder.gridBinding.wishlist.setVisibility(View.GONE);
            }

            //-------------------------

            if(product.get("stock_status").equals("false"))
            {
                holder.gridBinding.MageNativeStocksection.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.gridBinding.MageNativeStocksection.setVisibility(View.GONE);
            }

            //----------------------------
            if (Objects.requireNonNull(product.get("special_price")).equals("no_special")) {
                holder.gridBinding.specialPrice.setVisibility(View.GONE);
                holder.gridBinding.regularPrice.setPaintFlags(holder.gridBinding.regularPrice.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.gridBinding.regularPrice.setTextColor(activity.getResources().getColor(R.color.red));
                // productPrice1.setTextSize(activity.getResources().getDimension(R.dimen.textsize));
            } else {
                holder.gridBinding.specialPrice.setVisibility(View.VISIBLE);
                holder.gridBinding.specialPrice.setText(product.get("special_price"));
                holder.gridBinding.regularPrice.setTextColor(activity.getResources().getColor(R.color.bd));
                holder.gridBinding.regularPrice.setPaintFlags(holder.gridBinding.regularPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            //  wishlistid1.setText(data.get(position).get("wishlist_item_id"));
            Glide.with(activity)
                    .load(product.get("product_image"))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(holder.gridBinding.productImage);

            holder.gridBinding.productId.setText(product.get("product_id"));
            holder.gridBinding.productName.setText(product.get("product_name"));
            holder.gridBinding.regularPrice.setText(product.get("regular_price"));

            if (Objects.requireNonNull(product.get("review")).equals("No_Review")) {
                holder.gridBinding.productRating.setVisibility(View.GONE);
            } else {
                if (Objects.requireNonNull(product.get("review")).equals("0")) {
                    holder.gridBinding.productRating.setVisibility(View.GONE);
                } else {
                    holder.gridBinding.productRating.setVisibility(View.VISIBLE);
                    float f = Float.parseFloat(Objects.requireNonNull(product.get("review")));
                    holder.gridBinding.productRating.setRating(f);
                }
            }

            if(product.get("offer") != null){
                if (Objects.requireNonNull(product.get("offer")).equals("0")) {
                    holder.gridBinding.percentagerelative.setVisibility(View.GONE);
                    holder.gridBinding.offertext.setVisibility(View.GONE);
                } else {
                    holder.gridBinding.percentagerelative.setVisibility(View.VISIBLE);
                    holder.gridBinding.offertext.setVisibility(View.VISIBLE);
                    holder.gridBinding.percentage.setText(product.get("offer") + "" + activity.getResources().getString(R.string.percent));
                    holder.gridBinding.offertext.setText("-" + product.get("offer") + "%");
                }
            }
        }
        else {

            ((Ced_NavigationActivity)activity).set_regular_font_fortext(holder.listBinding.productName);
            ((Ced_NavigationActivity)activity).set_regular_font_fortext(holder.listBinding.regularPrice);
            ((Ced_NavigationActivity)activity).set_regular_font_fortext(holder.listBinding.specialPrice);
            ((Ced_NavigationActivity)activity).set_regular_font_fortext(holder.listBinding.percentage);
            ((Ced_NavigationActivity)activity).set_regular_font_fortext(holder.listBinding.offertext);
            ((Ced_NavigationActivity)activity).set_regular_font_fortext(holder.listBinding.viewdetails);


            holder.listBinding.mainLayout.setOnClickListener(v -> {
                Intent intent = new Intent(activity, Ced_NewProductView.class);
                intent.putExtra("product_id", holder.listBinding.productId.getText().toString());
                activity.startActivity(intent);
                ((Activity) activity).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            });

            holder.listBinding.wishlist.setVisibility(View.VISIBLE);

            if (Objects.requireNonNull(product.get("type")).equals("configurable") ||
                    Objects.requireNonNull(product.get("type")).equals("simple") ||
                    Objects.requireNonNull(product.get("type")).equals("virtual") ||
                    Objects.requireNonNull(product.get("type")).equals("downloadable")) {
                holder.listBinding.regularPrice.setVisibility(View.VISIBLE);
                holder.listBinding.viewdetails.setVisibility(View.GONE);
            } else {
                holder.listBinding.regularPrice.setVisibility(View.GONE);
                holder.listBinding.viewdetails.setVisibility(View.VISIBLE);
            }

            if (Objects.requireNonNull(product.get("Inwishlist")).equals("IN")) {
                holder.listBinding.wishlist.setImageResource(R.drawable.wishred);
            } else {
                holder.listBinding.wishlist.setVisibility(View.GONE);
            }


            //-------------------------

            if(product.get("stock_status").equals("false"))
            {
                holder.listBinding.MageNativeStocksection.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.listBinding.MageNativeStocksection.setVisibility(View.GONE);
            }

            //----------------------------





            if (Objects.requireNonNull(product.get("special_price")).equals("no_special")) {
                holder.listBinding.specialPrice.setVisibility(View.GONE);
                holder.listBinding.regularPrice.setPaintFlags(holder.listBinding.regularPrice.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.listBinding.regularPrice.setTextColor(activity.getResources().getColor(R.color.red));
                // productPrice1.setTextSize(activity.getResources().getDimension(R.dimen.textsize));
            } else {
                holder.listBinding.specialPrice.setVisibility(View.VISIBLE);
                holder.listBinding.specialPrice.setText(product.get("special_price"));
                holder.listBinding.regularPrice.setTextColor(activity.getResources().getColor(R.color.bd));
                holder.listBinding.regularPrice.setPaintFlags(holder.listBinding.regularPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            //  wishlistid1.setText(data.get(position).get("wishlist_item_id"));
            Glide.with(activity)
                    .load(product.get("product_image"))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(holder.listBinding.productImage);

            holder.listBinding.productId.setText(product.get("product_id"));
            holder.listBinding.productName.setText(product.get("product_name"));
            holder.listBinding.regularPrice.setText(product.get("regular_price"));

            if (Objects.requireNonNull(product.get("review")).equals("No_Review")) {
                holder.listBinding.productRating.setVisibility(View.GONE);
            } else {
                if (Objects.requireNonNull(product.get("review")).equals("0")) {
                    holder.listBinding.productRating.setVisibility(View.GONE);
                } else {
                    holder.listBinding.productRating.setVisibility(View.VISIBLE);
                    float f = Float.parseFloat(Objects.requireNonNull(product.get("review")));
                    holder.listBinding.productRating.setRating(f);
                }
            }

            if(product.get("offer") != null){
                if (Objects.requireNonNull(product.get("offer")).equals("0")) {
                    holder.listBinding.percentagerelative.setVisibility(View.GONE);
                    holder.listBinding.offertext.setVisibility(View.GONE);
                } else {
                    holder.listBinding.percentagerelative.setVisibility(View.VISIBLE);
                    holder.listBinding.offertext.setVisibility(View.VISIBLE);
                    holder.listBinding.percentage.setText(product.get("offer") + "" + activity.getResources().getString(R.string.percent));
                    holder.listBinding.offertext.setText("-" + product.get("offer") + "%");
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        ListProductDesignBinding listBinding;
        MagenativeNewGridBinding gridBinding;

        public GridViewHolder(@NonNull ListProductDesignBinding listBinding) {
            super(listBinding.getRoot());
            this.listBinding = listBinding;
        }

        public GridViewHolder(@NonNull MagenativeNewGridBinding gridBinding) {
            super(gridBinding.getRoot());
            this.gridBinding = gridBinding;
        }
    }
}
