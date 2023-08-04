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

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;

import java.util.Objects;

public class Ced_ProductViewImageFragment extends Fragment {
    private View v;
    private String url;
    private ImageView image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        try {
            v = inflater.inflate(R.layout.magenative_image, container, false);
            ActivityManager.MemoryInfo memoryInfo = getAvailableMemory();
            if (!memoryInfo.lowMemory) {
                image = v.findViewById(R.id.MageNative_image);
                url = Objects.requireNonNull(getArguments()).getString("url");
                final CharSequence[] stack = getArguments().getCharSequenceArray("stack");
                final int position = getArguments().getInt("position");

                image.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), Ced_ZoomImagePagerActivity.class);
                    intent.putExtra("IMAGEURL", stack);
                    intent.putExtra("POS", position);
                    startActivity(intent);
                });

                try {
                    if (!memoryInfo.lowMemory) {
                        Glide.with(Objects.requireNonNull(getActivity()))
                                .load(url)
                                .placeholder(R.drawable.placeholder)
                                .error((R.drawable.placeholder))
                                .into(image);
                        //new DownloadTask(image).execute(url);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Intent main = new Intent(getActivity(), Ced_MainActivity.class);
            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Objects.requireNonNull(getActivity()).startActivity(main);
            getActivity().overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
        return v;
    }

    private ActivityManager.MemoryInfo getAvailableMemory() {
        getActivity();
        ActivityManager activityManager = (ActivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        Objects.requireNonNull(activityManager).getMemoryClass();
        return memoryInfo;
    }

    public String geturl() {
        return url;
    }
}