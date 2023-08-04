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

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.akshar.store.R;
import com.akshar.store.ui.websection.Ced_WebAppInterface;
import com.akshar.store.databinding.MagenativeFragmentLoadDescriptionBinding;

import java.util.HashMap;
public class Ced_CmsWebview extends Fragment
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
        if(!(str.contains("https") || str.contains("http")))
        {
            str="http://"+str;
        }
        loaddescriptanddetail.getSettings().setJavaScriptEnabled(true);
        loaddescriptanddetail.getSettings().setLoadWithOverviewMode(true);
        loaddescriptanddetail.getSettings().setUseWideViewPort(true);
        setUpWebViewDefaults(loaddescriptanddetail);
        HashMap hashMap= new HashMap();
        hashMap.put("Mobiconnectheader", getResources().getString(R.string.header));
        loaddescriptanddetail.loadUrl(str, hashMap);
        loaddescriptanddetail.addJavascriptInterface(new Ced_WebAppInterface(getActivity()), "Android");
        loaddescriptanddetail.setWebChromeClient(new WebChromeClient());
        return binding1.getRoot();
    }
    private void setUpWebViewDefaults(WebView webView)
    {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB)
        {
            settings.setDisplayZoomControls(false);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
            public void onLoadResource(WebView view, String url)
            {

            }
            public void onPageFinished(WebView view, String url)
            {

            }
        });
    }

}