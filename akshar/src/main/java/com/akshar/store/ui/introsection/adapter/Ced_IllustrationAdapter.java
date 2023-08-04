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
package com.akshar.store.ui.introsection.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.akshar.store.ui.introsection.fragment.Ced_Illustration_1;
import com.akshar.store.ui.introsection.fragment.Ced_Illustration_2;
import com.akshar.store.ui.introsection.fragment.Ced_Illustration_3;

public class Ced_IllustrationAdapter extends FragmentPagerAdapter {
    Context c;

    public Ced_IllustrationAdapter(FragmentManager fm, Context context) {
        super(fm);
        c = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Ced_Illustration_1();
            case 1:
                return new Ced_Illustration_2();
            case 2:
                return new Ced_Illustration_3();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
