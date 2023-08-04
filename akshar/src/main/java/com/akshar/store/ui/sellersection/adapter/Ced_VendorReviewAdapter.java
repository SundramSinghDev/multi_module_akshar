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
 *           * @license   http://cedcommerce.com/license-agreement.txt
 *
 */
package com.akshar.store.ui.sellersection.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_NavigationActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class Ced_VendorReviewAdapter extends BaseAdapter
{
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public Ced_VendorReviewAdapter(Activity a, ArrayList<HashMap<String, String>> d)
    {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    public int getCount()
    {

        return data.size();
    }
    public Object getItem(int position)
    {

        return position;
    }
    public long getItemId(int position)
    {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.magenative_review_row_layout_for_vendor, null);
        TextView review_tittle = (TextView)vi.findViewById(R.id.Magenative_review_tittle);
        TextView review_by = (TextView)vi.findViewById(R.id.Magenative_review_by);
        TextView reviewcounttag = (TextView)vi.findViewById(R.id.Magenative_reviewcounttag);
        TextView posted_ontag = (TextView)vi.findViewById(R.id.Magenative_posted_ontag);
        TextView averagerating = (TextView)vi.findViewById(R.id.Magenative_averagerating);
        TextView review_description = (TextView)vi.findViewById(R.id.Magenative_review_description);
        TextView posted_on = (TextView)vi.findViewById(R.id.Magenative_posted_on);
        HashMap<String, String> song =new HashMap<String, String>();
        song = data.get(position);
        review_tittle.setText("#"+song.get("review-title"));
        review_by.setText(song.get("review-by"));
        averagerating.setText(song.get("v_review"));
        Float aFloat=Float.valueOf(averagerating.getText().toString());
        if(aFloat<2)
        {
            averagerating.setBackground(activity.getResources().getDrawable(R.drawable.round_corner_red));
        }
        if(aFloat>=2&&aFloat<4)
        {
            averagerating.setBackground(activity.getResources().getDrawable(R.drawable.round_corner_yellow));
            averagerating.setTextColor(activity.getResources().getColor(R.color.black));
        }
        if(aFloat>=4)
        {
            averagerating.setBackground(activity.getResources().getDrawable(R.drawable.round_corner));
        }
        review_description.setText(song.get("review-description"));
        posted_on.setText(song.get("posted_on"));
        ((Ced_NavigationActivity)activity).set_regular_font_fortext(review_tittle);
        ((Ced_NavigationActivity)activity).set_regular_font_fortext(reviewcounttag);
        ((Ced_NavigationActivity)activity).set_regular_font_fortext(posted_ontag);
        ((Ced_NavigationActivity)activity).set_regular_font_fortext(review_by);
        ((Ced_NavigationActivity)activity).set_regular_font_fortext(review_description);
        ((Ced_NavigationActivity)activity).set_regular_font_fortext(posted_on);
        ((Ced_NavigationActivity)activity).set_bold_font_fortext(averagerating);
        return vi;
    }
}