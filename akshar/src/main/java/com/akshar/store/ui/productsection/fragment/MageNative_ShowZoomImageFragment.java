package com.akshar.store.ui.productsection.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.akshar.store.R;
import com.akshar.store.databinding.FragmentShowZoomImageBinding;

public class MageNative_ShowZoomImageFragment extends Fragment
{

    //MageNative_ProductImageAdapter2 productImageAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        String currenturl = getArguments().getString("current");
        FragmentShowZoomImageBinding binding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_show_zoom_image, null, false);
        binding1.MageNativeCross.setOnClickListener(v1 -> getActivity().onBackPressed());
        Glide.with(getActivity())
                .load(currenturl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
//                .override(320,320)
                .into(binding1.MageNativeImage);
        return  binding1.getRoot();
    }
}