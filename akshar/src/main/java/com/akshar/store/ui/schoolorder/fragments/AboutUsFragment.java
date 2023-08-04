package com.akshar.store.ui.schoolorder.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.FragmentAboutUsBinding;
import com.akshar.store.databinding.MagenativeFragmentLoadDescriptionBinding;
import com.akshar.store.ui.schoolorder.ui.SchoolDetailsActivity;
import com.akshar.store.ui.schoolorder.utils.ChromeClient;

public class AboutUsFragment extends Fragment {

    public AboutUsFragment() {
        // Required empty public constructor
    }

    FragmentAboutUsBinding binding1;
    private String htmlData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_about_us, null, false);
        return binding1.getRoot();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            htmlData = ((SchoolDetailsActivity) requireActivity()).about_us != null ? ((SchoolDetailsActivity) requireActivity()).about_us : "";
            String str = "<html>" +
                    "<head>" +
                    "<style>" +
                    "body {\n" +
                    "    color: #777;\n" +
                    "    font-family: 'Open Sans','Helvetica Neue',Helvetica,Arial,sans-serif;\n" +
                    "    font-style: normal;\n" +
                    "    font-weight: 400;\n" +
                    "    line-height: 1.4;\n" +
                    "    font-size: 1.3rem;\n" +
                    "}" +
                    "a{\n" +
                    "    color: #222529;\n" +
                    "text-decoration: none;" +
                    "}" +
                    "img{max-width: 100%; width:auto; height: auto;}" +
                    "</style>" +
                    "</head>" +
                    "<body>" + htmlData + "</body>" +
                    "</html>";
            binding1.aboutUs.getSettings().setLoadWithOverviewMode(true);
//        binding1.aboutUs.getSettings().setUseWideViewPort(true);
            binding1.aboutUs.getSettings().setJavaScriptEnabled(true);
            binding1.aboutUs.getSettings().setDomStorageEnabled(true);
            binding1.aboutUs.loadDataWithBaseURL(null, str, "text/html", "utf-8", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}