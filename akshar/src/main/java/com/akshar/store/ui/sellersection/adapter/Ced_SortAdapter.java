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

import android.annotation.SuppressLint;
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
import java.util.Iterator;
import java.util.Set;

public class Ced_SortAdapter extends BaseAdapter
{
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    String ord="";
    String seldirection="";
    public Ced_SortAdapter(Activity a, ArrayList<HashMap<String, String>> d, String order, String dir)
    {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ord=order;
        seldirection=dir;
    }
    public Ced_SortAdapter(Activity a, ArrayList<HashMap<String, String>> d)
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
    @SuppressLint("NewApi")
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.magenative_sort_components, null);
        TextView label= (TextView)vi.findViewById(R.id.MageNative_SortLabel);
        ((Ced_NavigationActivity)activity).set_regular_font_fortext(label);
        TextView direction = (TextView)vi.findViewById(R.id.MageNative_sortDirection);
        HashMap<String, String> song =new HashMap<String, String>();
        song = data.get(position);
        Set setOfKeys = song.keySet();
        Iterator iterator = setOfKeys.iterator();
        while (iterator.hasNext()) {
            String key= (String) iterator.next();
            if(key.equals(ord)&&song.get(key).equals(seldirection))
            {
                label.setText(key);
                direction.setText(song.get(key));
                label.setTextColor(activity.getResources().getColor(R.color.onwhitetextcolor));
                direction.setTextColor(activity.getResources().getColor(R.color.onwhitetextcolor));
            }
            else
            {
                label.setText(key);
                direction.setText(song.get(key));
                label.setTextColor(activity.getResources().getColor(R.color.secondory_color));
                direction.setTextColor(activity.getResources().getColor(R.color.secondory_color));
            }
        }
        return vi;
    }
}