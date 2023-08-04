package com.akshar.store.ui.schoolorder.utils;

import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.BindingAdapter;

import com.akshar.store.R;
import com.bumptech.glide.Glide;

import java.util.Objects;

public class ViewUtils {

    @BindingAdapter("imgViewSrc")
    public static void showImage(AppCompatImageView view, String url) {
        Glide.with(view.getContext())
                .load(url != null ? url : "")
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(view);
    }

    @BindingAdapter("tintCustom")
    public static void tint(AppCompatImageView view, String isInWishList) {
        try {
            if (isInWishList != null) {
                if (!isInWishList.equals("OUT"))
                    Glide.with(view.getContext())
                            .load(R.drawable.ic_heart_100)
                            .into(view);
            } else
                view.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @BindingAdapter("setSpecialPrice")
    public static void setSpecialPrice(AppCompatTextView view, String special_price) {
        try {
            if (special_price != null) {
                if (special_price.equals("no_special"))
                    view.setVisibility(View.GONE);
                else {
                    view.setText(special_price);
                    view.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        view.setVisibility(View.GONE);
    }

    @BindingAdapter("setText")
    public static void setText(AppCompatTextView view, String txt) {
        try {
            if (txt != null) {
                String text=txt.trim();
                if (text.length() == 0)
                    view.setVisibility(View.GONE);
                else {
                    view.setText(text);
                    view.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BindingAdapter("handleVisibility")
    public static void handleVisibility(LinearLayout view, String txt) {
        try {
            if (txt != null) {
                String text=txt.trim();
                if (text.length() == 0)
                    view.setVisibility(View.GONE);
                else {
                    view.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
