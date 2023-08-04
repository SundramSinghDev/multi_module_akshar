package com.akshar.store.ui.productsection.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.akshar.store.R;
import com.akshar.store.databinding.ContentZoomImagePagerBinding;
import com.akshar.store.ui.productsection.adapter.MageNative_ProductImageAdapter2;

public class MageNative_ZoomImagePagerActivity extends FragmentActivity
{
    private ViewPager pager;
    private MyPagerAdapter adapter;
    String url[];
    int pos;
    Gallery productgallery;
    MageNative_ProductImageAdapter2 productImageAdapter;
    int width;
    int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentZoomImagePagerBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.content_zoom_image_pager, null, false);
        setContentView(binding1.getRoot());
        Intent intent = getIntent();
        url =intent.getStringArrayExtra("IMAGEURL");
        pos=intent.getIntExtra("POS", 0);
        productgallery= binding1.MageNativeProductgallery;
        pager = binding1.MageNativePager;
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setCurrentItem(pos);
        productImageAdapter = new MageNative_ProductImageAdapter2(MageNative_ZoomImagePagerActivity.this, url);
        productgallery.setAdapter(productImageAdapter);
        productgallery.setSelection(pos);
        productgallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pager.setCurrentItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position)
            {
                productgallery.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }


    public class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }



        @Override
        public int getCount()
        {
            return url.length;
        }

        @NonNull
        @Override
        public Fragment getItem(int position)
        {
            final MageNative_ShowZoomImageFragment f=new MageNative_ShowZoomImageFragment();
            final  Bundle bundle=new Bundle();
            bundle.putString("current",url[position]);
            f.setArguments(bundle);
            return  f;
        }
    }

}