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

package com.akshar.store.ui.productsection.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.akshar.store.databinding.MagenativeAddressListItemBinding;
import com.akshar.store.databinding.MagenativeDownloadsItemsBinding;
import com.akshar.store.ui.networkhandlea_activities.Ced_DownloadFileFromURL;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_NavigationActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class Ced_DownloadsAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;

    public Ced_DownloadsAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    @SuppressLint("NewApi")
    public View getView(final int position, View convertView, ViewGroup parent) {
        MagenativeDownloadsItemsBinding downloadsItemsBinding;
        if (convertView == null) {
             downloadsItemsBinding = DataBindingUtil.inflate(inflater,R.layout.magenative_downloads_items, parent,false);
            convertView = downloadsItemsBinding.getRoot();
            ((Ced_NavigationActivity)activity).set_regular_font_fortext(downloadsItemsBinding.MageNativeLinkTitle);
            ((Ced_NavigationActivity)activity).set_regular_font_fortext(downloadsItemsBinding.MageNativeFileName);
            ((Ced_NavigationActivity)activity).set_regular_font_fortext(downloadsItemsBinding.MageNativeDate);

            HashMap<String, String> song = data.get(position);
            downloadsItemsBinding.MageNativeOrderId.setText(song.get("order_id"));
            downloadsItemsBinding.MageNativeDate.setText(song.get("date"));
            downloadsItemsBinding.MageNativeTitle.setText(song.get("title"));
            downloadsItemsBinding.MageNativeDownloadUrl.setText(song.get("download_url"));
            downloadsItemsBinding.MageNativeFileName.setText(song.get("file_name"));
            downloadsItemsBinding.MageNativeLinkTitle.setText(song.get("link_title"));
            downloadsItemsBinding.MageNativeStatus.setText(song.get("status"));
            downloadsItemsBinding.MageNativeRemainingDowload.setText(song.get("remaining_dowload"));

           MagenativeDownloadsItemsBinding finalDownloadsItemsBinding = downloadsItemsBinding;
            downloadsItemsBinding.MageNativeDownloadlink.setOnClickListener(v -> {
                Ced_DownloadFileFromURL url = new Ced_DownloadFileFromURL(activity,  finalDownloadsItemsBinding.MageNativeFileName.getText().toString(), finalDownloadsItemsBinding.MageNativeLinkTitle.getText().toString());
                url.execute(finalDownloadsItemsBinding.MageNativeDownloadUrl.getText().toString());
            });
        }
        else
        {
            downloadsItemsBinding = (MagenativeDownloadsItemsBinding) convertView.getTag();
        }
        convertView.setTag(downloadsItemsBinding);
        return convertView;
    }


}