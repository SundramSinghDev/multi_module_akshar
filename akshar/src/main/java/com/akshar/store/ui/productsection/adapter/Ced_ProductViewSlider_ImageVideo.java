package com.akshar.store.ui.productsection.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.akshar.store.ui.productsection.fragment.Ced_ProductViewImageFragment;
import com.akshar.store.ui.productsection.fragment.Ced_ProductViewImageVideoShow_Fragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Ced_ProductViewSlider_ImageVideo extends FragmentStatePagerAdapter
{
    Context c;
    CharSequence[] stringArray;
    ArrayList<String> images;
    ArrayList<LinkedHashMap<String,String>> img_params;
    public Ced_ProductViewSlider_ImageVideo(FragmentManager fm, Context context, ArrayList<String> img, ArrayList<LinkedHashMap<String,String>> img_params)
    {
        super(fm);
        c=context;
        images=img;
        this.img_params=img_params;
        stringArray = img.toArray(new String[img.size()]);
    }
    /*public Ced_ProductViewSlider_ImageVideo(FragmentManager fm, Context context, @NonNull ArrayList<String> img)
    {
        super(fm);
        c=context;
        stringArray = img.toArray(new String[img.size()]);
    }*/
    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        /*final Ced_ProductViewImageFragment f1=new Ced_ProductViewImageFragment();
        final Bundle bundle=new Bundle();
        bundle.putString("url", String.valueOf(stringArray[position]));
        bundle.putCharSequenceArray("stack", stringArray);
        bundle.putInt("position",position);
        f1.setArguments(bundle);
        return f1;*/
        final Ced_ProductViewImageVideoShow_Fragment f1=new Ced_ProductViewImageVideoShow_Fragment();
        final Bundle bundle=new Bundle();
        if(img_params.size()>0)
        {
            LinkedHashMap<String,String> img_param=img_params.get(position);
            Log.i("REpo","imgsli "+img_param.get("image"));
            if(img_param.get("image").equals("image")){
                bundle.putString("image","image");
            }else if(img_param.get("image").equals("external-video")){
                bundle.putString("image","external-video");
                bundle.putString("image_url",img_param.get("image_url"));
            }
        }
        bundle.putString("url", String.valueOf(stringArray[position]));
        bundle.putCharSequenceArray("stack", stringArray);
        bundle.putInt("position",position);
        f1.setArguments(bundle);
        return f1;
    }
    @Override
    public int getCount()
    {
        return img_params.size();
    }

}