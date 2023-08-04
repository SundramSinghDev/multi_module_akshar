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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.akshar.store.R;
import com.akshar.store.databinding.MagenativeFragmentLoadDescriptionBinding;
public class Ced_LoadDescription extends Fragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        MagenativeFragmentLoadDescriptionBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.magenative_fragment_load_description, null, false);
        WebView loaddescriptanddetail= binding1.MageNativeLoaddescriptanddetail;
        String str= getArguments().getString("htmldata");

        loaddescriptanddetail.loadDataWithBaseURL(null, str, "text/html", "utf-8", null);
        return binding1.getRoot();
    }


}