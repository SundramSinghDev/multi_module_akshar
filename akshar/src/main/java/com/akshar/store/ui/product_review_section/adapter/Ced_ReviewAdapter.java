package com.akshar.store.ui.product_review_section.adapter;/*
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
 *//*


package com.akshar.store.ui.product_review_section.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.akshar.store.R;
import com.akshar.store.databinding.MagenativeCreateLayoutForReviewBinding;
import com.akshar.store.ui.product_review_section.activity.Ced_ReviewListing;

import java.util.ArrayList;
import java.util.HashMap;

public class Ced_ReviewAdapter extends BaseAdapter {
    private Context activity;
    private ArrayList<HashMap<String, String>> data;

    public Ced_ReviewAdapter(Context cedReviewListing, ArrayList<HashMap<String, String>> reviewinfo) {
        activity = cedReviewListing;
        data = reviewinfo;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout rating5;
        LinearLayout rating4;
        LinearLayout rating3;
        LinearLayout rating2;
        LinearLayout rating1;
        TextView mageNative_reviewtext5;
        TextView mageNative_reviewtext4;
        TextView mageNative_reviewtext3;
        TextView mageNative_reviewtext2;
        TextView mageNative_reviewtext1;
        TextView mageNative_fill_rating_code5;
        TextView mageNative_fill_rating_code4;
        TextView mageNative_fill_rating_code3;
        TextView mageNative_fill_rating_code2;
        TextView mageNative_fill_rating_code1;
        TextView mageNative_review_description;
        TextView mageNative_posted_on;
        TextView mageNative_review_by;
        TextView mageNative_review_headtitle;

        if (convertView == null) {
            MagenativeCreateLayoutForReviewBinding binding1 = DataBindingUtil.inflate(inflater, R.layout.magenative_create_layout_for_review, null, false);
            vi=binding1.getRoot();
            mageNative_review_headtitle = binding1.MageNativeReviewHeadtitle;
            mageNative_review_by = binding1.MageNativeReviewBy;
            mageNative_posted_on = binding1.MageNativePostedOn;
            mageNative_review_description = binding1.MageNativeReviewDescription;
            mageNative_fill_rating_code1 = binding1.MageNativeFillRatingCode1;
            mageNative_fill_rating_code2 = binding1.MageNativeFillRatingCode2;
            mageNative_fill_rating_code3 = binding1.MageNativeFillRatingCode3;
            mageNative_fill_rating_code4 = binding1.MageNativeFillRatingCode4;
            mageNative_fill_rating_code5 = binding1.MageNativeFillRatingCode5;
            mageNative_reviewtext1 = binding1.MageNativeReviewtext1;
            mageNative_reviewtext2 = binding1.MageNativeReviewtext2;
            mageNative_reviewtext3 = binding1.MageNativeReviewtext3;
            mageNative_reviewtext4 = binding1.MageNativeReviewtext4;
            mageNative_reviewtext5 = binding1.MageNativeReviewtext5;
            rating1 = binding1.rating1;
            rating2 = binding1.rating2;
            rating3 = binding1.rating3;
            rating4 = binding1.rating4;
            rating5 = binding1.rating5;

            mageNative_review_headtitle.setText(data.get(position).get(Ced_ReviewListing.KEY_REVIEW_TITLE));
            mageNative_review_by.setText(data.get(position).get(Ced_ReviewListing.KEY_REVIEW_BY));
            mageNative_posted_on.setText(data.get(position).get(Ced_ReviewListing.KEY_POSTED_ON));
            mageNative_review_description.setText(data.get(position).get(Ced_ReviewListing.KEY_REVIEW_DESCRIPTION));

            if (data.get(position).containsKey("rating1")) {
                mageNative_fill_rating_code1.setText(data.get(position).get("rating_code1"));
                mageNative_reviewtext1.setText(data.get(position).get("rating_value1"));
                rating1.setVisibility(View.VISIBLE);
            } else {
                rating1.setVisibility(View.GONE);
            }

            if (data.get(position).containsKey("rating2")) {
                mageNative_fill_rating_code2.setText(data.get(position).get("rating_code2"));
                mageNative_reviewtext2.setText(data.get(position).get("rating_value2"));
                rating2.setVisibility(View.VISIBLE);
            } else {
                rating2.setVisibility(View.GONE);
            }

            if (data.get(position).containsKey("rating3")) {
                mageNative_fill_rating_code3.setText(data.get(position).get("rating_code3"));
                mageNative_reviewtext3.setText(data.get(position).get("rating_value3"));
                rating3.setVisibility(View.VISIBLE);
            } else {
                rating3.setVisibility(View.GONE);
            }

            if (data.get(position).containsKey("rating4")) {
                mageNative_fill_rating_code4.setText(data.get(position).get("rating_code4"));
                mageNative_reviewtext4.setText(data.get(position).get("rating_value4"));
                rating4.setVisibility(View.VISIBLE);
            } else {
                rating4.setVisibility(View.GONE);
            }

            if (data.get(position).containsKey("rating5")) {
                mageNative_fill_rating_code5.setText(data.get(position).get("rating_code5"));
                mageNative_reviewtext5.setText(data.get(position).get("rating_value5"));
                rating5.setVisibility(View.VISIBLE);
            } else {
                rating5.setVisibility(View.GONE);
            }
        }
        else {
            MagenativeCreateLayoutForReviewBinding binding1 = DataBindingUtil.inflate(inflater, R.layout.magenative_create_layout_for_review, null, false);
            vi=binding1.getRoot();
            mageNative_review_headtitle = binding1.MageNativeReviewHeadtitle;
            mageNative_review_by = binding1.MageNativeReviewBy;
            mageNative_posted_on = binding1.MageNativePostedOn;
            mageNative_review_description = binding1.MageNativeReviewDescription;
            mageNative_fill_rating_code1 = binding1.MageNativeFillRatingCode1;
            mageNative_fill_rating_code2 = binding1.MageNativeFillRatingCode2;
            mageNative_fill_rating_code3 = binding1.MageNativeFillRatingCode3;
            mageNative_fill_rating_code4 = binding1.MageNativeFillRatingCode4;
            mageNative_fill_rating_code5 = binding1.MageNativeFillRatingCode5;
            mageNative_reviewtext1 = binding1.MageNativeReviewtext1;
            mageNative_reviewtext2 = binding1.MageNativeReviewtext2;
            mageNative_reviewtext3 = binding1.MageNativeReviewtext3;
            mageNative_reviewtext4 = binding1.MageNativeReviewtext4;
            mageNative_reviewtext5 = binding1.MageNativeReviewtext5;
            rating1 = binding1.rating1;
            rating2 = binding1.rating2;
            rating3 = binding1.rating3;
            rating4 = binding1.rating4;
            rating5 = binding1.rating5;

            mageNative_review_headtitle.setText(data.get(position).get(Ced_ReviewListing.KEY_REVIEW_TITLE));
            mageNative_review_by.setText(data.get(position).get(Ced_ReviewListing.KEY_REVIEW_BY));
            mageNative_posted_on.setText(data.get(position).get(Ced_ReviewListing.KEY_POSTED_ON));
            mageNative_review_description.setText(data.get(position).get(Ced_ReviewListing.KEY_REVIEW_DESCRIPTION));

            if (data.get(position).containsKey("rating1")) {
                mageNative_fill_rating_code1.setText(data.get(position).get("rating_code1"));
                mageNative_reviewtext1.setText(data.get(position).get("rating_value1"));
                rating1.setVisibility(View.VISIBLE);
            }
            else {
                rating1.setVisibility(View.GONE);
            }

            if (data.get(position).containsKey("rating2")) {
                mageNative_fill_rating_code2.setText(data.get(position).get("rating_code2"));
                mageNative_reviewtext2.setText(data.get(position).get("rating_value2"));
                rating2.setVisibility(View.VISIBLE);
            } else {
                rating2.setVisibility(View.GONE);
            }

            if (data.get(position).containsKey("rating3")) {
                mageNative_fill_rating_code3.setText(data.get(position).get("rating_code3"));
                mageNative_reviewtext3.setText(data.get(position).get("rating_value3"));
                rating3.setVisibility(View.VISIBLE);
            } else {
                rating3.setVisibility(View.GONE);
            }

            if (data.get(position).containsKey("rating4")) {
                mageNative_fill_rating_code4.setText(data.get(position).get("rating_code4"));
                mageNative_reviewtext4.setText(data.get(position).get("rating_value4"));
                rating4.setVisibility(View.VISIBLE);
            } else {
                rating4.setVisibility(View.GONE);
            }

            if (data.get(position).containsKey("rating5")) {
                mageNative_fill_rating_code5.setText(data.get(position).get("rating_code5"));
                mageNative_reviewtext5.setText(data.get(position).get("rating_value5"));
                rating5.setVisibility(View.VISIBLE);
            } else {
                rating5.setVisibility(View.GONE);
            }
        }

        return vi;
    }

}*/
