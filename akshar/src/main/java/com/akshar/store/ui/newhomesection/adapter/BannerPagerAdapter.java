package com.akshar.store.ui.newhomesection.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.akshar.store.R;
import com.akshar.store.databinding.MagenativeBannerLayoutBinding;
import com.akshar.store.ui.websection.Ced_Weblink;
import com.akshar.store.ui.productsection.activity.Ced_NewProductView;
import com.akshar.store.ui.productsection.activity.Ced_New_Product_Listing;

import org.json.JSONArray;

public class BannerPagerAdapter extends PagerAdapter {
    JSONArray stringArray;
    Context context;

    public BannerPagerAdapter(JSONArray stringArray, Context context) {
        this.stringArray = stringArray;
        this.context = context;
    }

    @Override
    public int getCount() {
        return stringArray.length();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        MagenativeBannerLayoutBinding bannerLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.magenative_banner_layout, container, false);
        ImageView MageNative_bannerimage = bannerLayoutBinding.MageNativeBannerimage;
        try {
            Glide.with(context)
                    .load(stringArray.getJSONObject(position).getString("banner_image"))
                    .placeholder(R.drawable.bannerplaceholder)
                    .error(R.drawable.bannerplaceholder)
                    .into(MageNative_bannerimage);

            MageNative_bannerimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (stringArray.getJSONObject(position).getString("link_to").equals("category")) {
                            Intent intent = new Intent(context, Ced_New_Product_Listing.class);
                            intent.putExtra("ID", stringArray.getJSONObject(position).getString("product_id"));
                            context.startActivity(intent);
                            ((Activity) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        }
                        if (stringArray.getJSONObject(position).getString("link_to").equals("product")) {
                            Intent prod_link = new Intent(context, Ced_NewProductView.class);
                            prod_link.putExtra("product_id", stringArray.getJSONObject(position).getString("product_id"));
                            context.startActivity(prod_link);
                            ((Activity) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        }
                        if (stringArray.getJSONObject(position).getString("link_to").equals("website")) {
                            Intent weblink = new Intent(context, Ced_Weblink.class);
                            weblink.putExtra("link", stringArray.getJSONObject(position).getString("product_id"));
                            context.startActivity(weblink);
                            ((Activity) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        container.addView(bannerLayoutBinding.getRoot());
        return bannerLayoutBinding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
