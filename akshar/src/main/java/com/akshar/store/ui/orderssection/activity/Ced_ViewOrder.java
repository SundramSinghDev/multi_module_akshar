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
package com.akshar.store.ui.orderssection.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.R;
import com.google.android.gms.analytics.Tracker;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.databinding.MagenativeActivityViewOrderBinding;
import com.akshar.store.ui.homesection.activity.Ced_New_home_page;
import com.akshar.store.ui.newhomesection.activity.Magenative_HomePageNewTheme;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Ced_ViewOrder extends Ced_NavigationActivity {
    MagenativeActivityViewOrderBinding viewOrderBinding;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewOrderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_activity_view_order, content, true);

       set_regular_font_fortext(viewOrderBinding.MageNativeOrderId);
       set_regular_font_forButton(viewOrderBinding.MageNativeContinuewithdefault);

        viewOrderBinding.MageNativeFinalorderid.setText("#" + getIntent().getStringExtra("order_id"));

        viewOrderBinding.MageNativeContinuewithdefault.setOnClickListener(v -> {
            Intent intent1 = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
            startActivity(intent1);
            finishAffinity();
            overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        });

        Ced_MainActivity.latestcartcount = "0";
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(getApplicationContext(), Magenative_HomePageNewTheme.class);
        startActivity(intent1);
        finishAffinity();
        overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        super.onResume();

    }

}
