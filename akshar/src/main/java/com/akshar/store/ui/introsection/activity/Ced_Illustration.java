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
package com.akshar.store.ui.introsection.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.R;
import com.akshar.store.databinding.MagenativeProductViewFagmentBinding;
import com.akshar.store.ui.introsection.adapter.Ced_IllustrationAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ced_Illustration extends FragmentActivity {
    MagenativeProductViewFagmentBinding fragmentBinding;

    Ced_IllustrationAdapter pageAdapter2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentBinding = DataBindingUtil.setContentView(Ced_Illustration.this, R.layout.magenative_product_view_fagment);


        //---------default--------------//
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.sp_1_color));
        }
        fragmentBinding.MageNativeNextandskipsection.setBackgroundColor(getResources().getColor(R.color.sp_1_color));
        fragmentBinding.main.setBackgroundColor(getResources().getColor(R.color.sp_1_color));
        fragmentBinding.MageNativeSkip.setBackgroundColor(getResources().getColor(R.color.sp_1_color));
        fragmentBinding.MageNativeNext.setBackgroundColor(getResources().getColor(R.color.sp_1_color));
        //---------default--------------//
        fragmentBinding.MageNativeSkip.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Ced_MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            finish();
        });

        fragmentBinding.MageNativeNext.setOnClickListener(v -> {
            int current = fragmentBinding.MageNativeViewpager.getCurrentItem();
            if (current >= 0 && current < 3) {
                fragmentBinding.MageNativeViewpager.setCurrentItem(current + 1);
            }
        });

        fragmentBinding.MageNativeGetstarted.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Ced_MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            finish();
        });

        pageAdapter2 = new Ced_IllustrationAdapter(getSupportFragmentManager(), this);
        fragmentBinding.MageNativeViewpager.setAdapter(pageAdapter2);
        fragmentBinding.MageNativeViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Window window = getWindow();
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.setStatusBarColor(getResources().getColor(R.color.sp_1_color));
                        }
                       fragmentBinding.MageNativeNextandskipsection.setBackgroundColor(getResources().getColor(R.color.sp_1_color));
                        fragmentBinding.MageNativeSkip.setBackgroundColor(getResources().getColor(R.color.sp_1_color));
                        fragmentBinding.MageNativeNext.setBackgroundColor(getResources().getColor(R.color.sp_1_color));
                        fragmentBinding.main.setBackgroundColor(getResources().getColor(R.color.sp_1_color));

                        /*fragmentBinding.first.setBackgroundColor(getResources().getColor(R.color.onwhitetextcolor));
                        fragmentBinding.second.setBackgroundColor(getResources().getColor(R.color.gray_light));
                        fragmentBinding.third.setBackgroundColor(getResources().getColor(R.color.gray_light));*/
                        fragmentBinding.MageNativeGetstarted.setVisibility(View.INVISIBLE);
                        fragmentBinding.MageNativeNextandskipsection.setVisibility(View.VISIBLE);
                        break;

                    case 1:

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Window window = getWindow();
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.setStatusBarColor(getResources().getColor(R.color.sp_2_color));
                        }
                        fragmentBinding.MageNativeNextandskipsection.setBackgroundColor(getResources().getColor(R.color.sp_2_color));
                        fragmentBinding.MageNativeSkip.setBackgroundColor(getResources().getColor(R.color.sp_2_color));
                        fragmentBinding.MageNativeNext.setBackgroundColor(getResources().getColor(R.color.sp_2_color));
                        fragmentBinding.main.setBackgroundColor(getResources().getColor(R.color.sp_2_color));
                        /*fragmentBinding.first.setBackgroundColor(getResources().getColor(R.color.gray_light));
                        fragmentBinding.second.setBackgroundColor(getResources().getColor(R.color.onwhitetextcolor));
                        fragmentBinding.third.setBackgroundColor(getResources().getColor(R.color.gray_light));*/
                        fragmentBinding.MageNativeGetstarted.setVisibility(View.INVISIBLE);
                        fragmentBinding.MageNativeNextandskipsection.setVisibility(View.VISIBLE);
                        break;

                    case 2:

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Window window = getWindow();
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.setStatusBarColor(getResources().getColor(R.color.sp_3_color));
                        }
                        fragmentBinding.MageNativeNextandskipsection.setBackgroundColor(getResources().getColor(R.color.sp_3_color));
                        fragmentBinding.MageNativeSkip.setBackgroundColor(getResources().getColor(R.color.sp_3_color));
                        fragmentBinding.MageNativeNext.setBackgroundColor(getResources().getColor(R.color.sp_3_color));
                        fragmentBinding.main.setBackgroundColor(getResources().getColor(R.color.sp_3_color));
                       /* fragmentBinding.first.setBackgroundColor(getResources().getColor(R.color.gray_light));
                        fragmentBinding.second.setBackgroundColor(getResources().getColor(R.color.gray_light));
                        fragmentBinding.third.setBackgroundColor(getResources().getColor(R.color.onwhitetextcolor));*/
                        fragmentBinding.MageNativeGetstarted.setVisibility(View.VISIBLE);
                        fragmentBinding.MageNativeNextandskipsection.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}