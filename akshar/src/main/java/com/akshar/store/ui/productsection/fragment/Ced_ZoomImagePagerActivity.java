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
package com.akshar.store.ui.productsection.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;

import com.akshar.store.R;
import com.akshar.store.ui.productsection.adapter.Ced_ProductImageAdapter2;

public class Ced_ZoomImagePagerActivity extends FragmentActivity {

    String url[];
    int pos;
    Gallery productgallery;
    Ced_ProductImageAdapter2 productImageAdapter;
    private ViewPager pager;
    private MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.magenative_content_zoom_image_pager);
        Intent intent = getIntent();
        url = intent.getStringArrayExtra("IMAGEURL");
        pos = intent.getIntExtra("POS", 0);
        productgallery = (Gallery) findViewById(R.id.MageNative_productgallery);
        pager = (ViewPager) findViewById(R.id.MageNative_pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setCurrentItem(pos);
        productImageAdapter = new Ced_ProductImageAdapter2(Ced_ZoomImagePagerActivity.this, url);
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
            public void onPageSelected(int position) {
                productgallery.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return url.length;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            final Ced_ShowZoomImageFragment f = new Ced_ShowZoomImageFragment();
            final Bundle bundle = new Bundle();
            bundle.putString("current", url[position]);
            f.setArguments(bundle);
            return f;
        }
    }
}