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
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.akshar.store.R;
public class Ced_ProductImageAdapter2 extends BaseAdapter
{
    Drawable d;
    String urls[]=new String[10];
    private Context galleryContext;
    private static LayoutInflater inflater=null;
    public Ced_ProductImageAdapter2(Context c, String image[])
    {
        galleryContext = c;
        urls=image;
        inflater = (LayoutInflater)galleryContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount()
    {
        return urls.length;
    }
    @Override
    public Object getItem(int position)
    {
        return position;
    }
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi=convertView;
        if(convertView==null)
        {
            vi = inflater.inflate(R.layout.magenative_gallerycards_2, null);
            ImageView imageView = (ImageView) vi.findViewById(R.id.MageNative_galleryimage);
            Glide.with(galleryContext)
                    .load(urls[position])
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .override(90,90)
                    .into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            vi.setPadding(10, 10, 10, 10);
        }
        return vi;
    }
}
