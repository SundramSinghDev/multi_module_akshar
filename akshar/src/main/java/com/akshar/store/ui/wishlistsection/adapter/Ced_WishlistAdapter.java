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
package com.akshar.store.ui.wishlistsection.adapter;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonObject;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import android.app.Activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;


import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.MagenativeWishCompBinding;
import com.akshar.store.ui.wishlistsection.activity.Ced_WishListing;
import com.akshar.store.ui.wishlistsection.viewmodel.WishListViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

public class Ced_WishlistAdapter extends BaseSwipeAdapter {
    @Inject
    ViewModelFactory viewModelFactory;
    WishListViewModel wishListViewModel;

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;

    public Ced_WishlistAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        wishListViewModel = new ViewModelProvider((FragmentActivity) activity, viewModelFactory).get(WishListViewModel.class);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.MageNative_swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        MagenativeWishCompBinding wishBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.magenative_wish_comp, null, false);

        TextView productname = wishBinding.MageNativeProductName;
        TextView productid = wishBinding.MageNativeProductId;
        TextView MageNative_price = wishBinding.MageNativePrice;
        TextView productprice = wishBinding.MageNativeProductPrice;
        TextView specialprice = wishBinding.MageNativeSpecialprice;
        final TextView item_id = wishBinding.MageNativeItemId;
        ImageView productimage = wishBinding.MageNativeProductImage;
        ImageView removefromwishlist = wishBinding.MageNativeTrash;
        ((Ced_NavigationActivity)activity).set_regular_font_fortext(productname);
        ((Ced_NavigationActivity)activity).set_regular_font_fortext(MageNative_price);
        ((Ced_NavigationActivity)activity).set_regular_font_fortext(specialprice);
        ((Ced_NavigationActivity)activity).set_regular_font_fortext(productprice);

        HashMap<String, String> song;
        song = data.get(position);

        productname.setText(song.get(Ced_WishListing.KEY_NAME));
        productid.setText(song.get(Ced_WishListing.KEY_ID));
        productprice.setText(song.get(Ced_WishListing.KEY_Price));

        if (Objects.requireNonNull(song.get(Ced_WishListing.KEY_Price_special_price)).equals("no_special")) {
            specialprice.setVisibility(View.INVISIBLE);
            productprice.setPaintFlags(productprice.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        } else {
            specialprice.setVisibility(View.VISIBLE);
            specialprice.setText(song.get(Ced_WishListing.KEY_Price_special_price));
            specialprice.setTextColor(activity.getResources().getColor(R.color.red));
            productprice.setText(song.get(Ced_WishListing.KEY_Price));
            productprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        item_id.setText(song.get(Ced_WishListing.KEY_wishlist_item_id));

        Glide.with(activity)
                .load(song.get(Ced_WishListing.KEY_Image))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(productimage);

        final View finalVi = wishBinding.getRoot();
        removefromwishlist.setOnClickListener(v -> {
            try {
               new MaterialAlertDialogBuilder(activity,R.style.MaterialDialog)
                .setTitle(R.string.app_name)
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("" + activity.getResources().getString(R.string.areyousure))
                .setNegativeButton(Html.fromHtml("Cancel"),
                        (dialog, which) -> {
                        })
                .setPositiveButton(Html.fromHtml("Yes"), (dialog, which) -> {
                    JsonObject removefrom_wishlist = new JsonObject();
                    try {
                        removefrom_wishlist.addProperty("item_id", item_id.getText().toString());
                        removefrom_wishlist.addProperty("customer_id", ((Ced_NavigationActivity)activity).session.getCustomerid());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    wishListViewModel.removeFromWishList(activity, removefrom_wishlist,((Ced_NavigationActivity)activity).session.getHahkey()).observe((FragmentActivity) activity, apiResponse -> {
                        switch (apiResponse.status) {
                            case SUCCESS:
                                try {
                                        JSONObject object = new JSONObject(Objects.requireNonNull(apiResponse.data));
                                        String Status = object.getString("status");
                                        if (Status.equals("true")) {
                                            finalVi.startAnimation(outToLeftAnimation());
                                            finalVi.setVisibility(View.GONE);
                                            refreshlistview();
                                        }
                                    ((Ced_NavigationActivity)activity).showmsg(object.getString("message"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Intent main = new Intent(activity, Ced_MainActivity.class);
                                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    activity.startActivity(main);
                                }
                                break;

                            case ERROR:
                                Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                                ((Ced_NavigationActivity) activity).showmsg(activity.getResources().getString(R.string.errorString));
                                break;
                        }
                    });
                })
                .show();
            } catch (Exception e) {
                e.printStackTrace();
                Intent main = new Intent(activity, Ced_MainActivity.class);
                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(main);
            }
        });
        return wishBinding.getRoot();
    }

    private Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(500);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    public void refreshlistview() {
        Handler handle = new Handler();
        handle.postDelayed(() -> {
            Intent intent = new Intent(activity, Ced_WishListing.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
        }, 1000);
    }

    @Override
    public void fillValues(int position, View convertView) {
    }
}