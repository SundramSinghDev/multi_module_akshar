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
package com.akshar.store.ui.productsection.activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.TypedValue;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.akshar.store.ui.productsection.fragment.Ced_CmsWebview;
import com.akshar.store.ui.productsection.fragment.Ced_LoadDescription;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.R;
import com.akshar.store.databinding.MagenativeProductDescriptionBinding;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class Ced_ProductDescription extends Ced_NavigationActivity {
    private final Handler handler = new Handler();
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    private int currentColor;
    String des;
    String det;
    String tab1;
    String tab2;
    String[] tittles;
    int totaltabs = 2;
    String cmsconent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MagenativeProductDescriptionBinding magenativeProductDescriptionBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_product_description, content, true);


        Intent intent = getIntent();
        des = intent.getStringExtra("1");
        det = intent.getStringExtra("2");
        tab1 = intent.getStringExtra("3");
        tab2 = intent.getStringExtra("4");

        currentColor = getResources().getColor(R.color.onwhitetextcolor);
        if (intent.getBooleanExtra("cms", false)) {
            totaltabs = 3;
        }

        tittles = new String[totaltabs];
        tittles[0] = tab1;
        tittles[1] = tab2;

        if (intent.getBooleanExtra("cms", false)) {
            String cmstitle = intent.getStringExtra("5");
            tittles[2] = cmstitle;
            cmsconent = intent.getStringExtra("6");
        }

        tabs = magenativeProductDescriptionBinding.MageNativeTabs;
        pager = magenativeProductDescriptionBinding.MageNativePager;

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tabs.setTextColor(R.color.AppTheme);
        tabs.setTextColorResource(R.color.AppTheme);
        tabs.setIndicatorColor(R.color.AppTheme);
        tabs.setIndicatorColorResource(R.color.AppTheme);
        tabs.setBackgroundColor(getResources().getColor(R.color.txtapptheme_color));
        tabs.setViewPager(pager);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentColor", currentColor);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentColor = savedInstanceState.getInt("currentColor");
    }

    private Drawable.Callback drawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(@NonNull Drawable who) {
            Objects.requireNonNull(getActionBar()).setBackgroundDrawable(who);
        }

        @Override
        public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
            handler.postAtTime(what, when);
        }

        @Override
        public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
            handler.removeCallbacks(what);
        }
    };

    public class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tittles[position];
        }

        @Override
        public int getCount() {
            return tittles.length;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Ced_LoadDescription cedLoadDescription = new Ced_LoadDescription();
                    Bundle bundle = new Bundle();
                    bundle.putString("htmldata", des);
                    cedLoadDescription.setArguments(bundle);
                    return cedLoadDescription;
                case 1:

                    Ced_LoadDescription cedLoadDescription2 = new Ced_LoadDescription();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("htmldata", det);
                    cedLoadDescription2.setArguments(bundle2);
                    return cedLoadDescription2;
                case 2:

                    Ced_CmsWebview cms = new Ced_CmsWebview();
                    Bundle cmcbundle = new Bundle();
                    cmcbundle.putString("htmldata", cmsconent);
                    cms.setArguments(cmcbundle);
                    return cms;

            }
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
