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
package com.akshar.store.ui.introsection.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.R;

import java.util.Objects;

public class Ced_Illustration_4 extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.magenative_illustraion_4, null);

        TextView getstarted = v.findViewById(R.id.MageNative_getstarted);
        ImageView image = v.findViewById(R.id.MageNative_image);

        final Animation animationFadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.magenative_fade_in);
        image.startAnimation(animationFadeIn);

        getstarted.setOnClickListener(v1 -> {
            Intent intent = new Intent(getActivity(), Ced_MainActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
            getActivity().finish();
        });
        return v;
    }
}