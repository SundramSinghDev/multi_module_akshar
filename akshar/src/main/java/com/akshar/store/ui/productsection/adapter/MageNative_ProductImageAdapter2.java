package com.akshar.store.ui.productsection.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.akshar.store.R;

public class MageNative_ProductImageAdapter2 extends BaseAdapter {

    Drawable d;
    String urls[]=new String[10];
    //int defaultItemBackground;
    //gallery context
    private Context galleryContext;
    @Nullable
    private static LayoutInflater inflater=null;
    public MageNative_ProductImageAdapter2(Context c, String image[])  {

        //instantiate context
        galleryContext = c;
        //for exapmle 10
        urls=image;
        inflater = (LayoutInflater)galleryContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /*TypedArray a =galleryContext.obtainStyledAttributes(R.styleable.MyGallery);
        defaultItemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground,5);
        a.recycle();
*/


    }



    @Override
    public int getCount() {
        return urls.length;
    }
    @Override
    public Object getItem(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {

        //create the view
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.magenative_gallerycards_2, null);
        ImageView imageView = (ImageView) vi.findViewById(R.id.MageNative_galleryimage);
        //specify the bitmap at this position in the array
        Glide.with(galleryContext)
                .load(urls[position])  // optional
                .override(90,90)                        // optional// optional
                .into(imageView);

        //set layout options
        // imageView.setLayoutParams(new Gallery.LayoutParams(150, 150));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        vi.setPadding(10, 10, 10, 10);


        //set default gallery item background
        //imageView.setBackgroundResource(defaultItemBackground);
        //return the view
        return vi;
    }
}
