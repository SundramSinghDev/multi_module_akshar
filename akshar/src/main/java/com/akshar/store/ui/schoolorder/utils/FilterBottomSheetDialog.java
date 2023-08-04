package com.akshar.store.ui.schoolorder.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import androidx.databinding.DataBindingUtil;

import com.akshar.store.R;
import com.akshar.store.databinding.SchoolFilterLayoutBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import javax.inject.Inject;

public class FilterBottomSheetDialog {
    private static final String TAG = "FilterBottomSheetDialog";
    public SchoolFilterLayoutBinding binding;
    public BottomSheetDialog bottomSheetDialog;
    Context context;

    @Inject
    public FilterBottomSheetDialog() {
    }

    @SuppressLint("RestrictedApi")
    public void init(Context context) {
        this.context = context;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.school_filter_layout, null, false);
        bottomSheetDialog = new BottomSheetDialog(context, R.style.SheetDialog);
        bottomSheetDialog.setContentView(binding.getRoot());
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        ((Activity) context).getResources().getColor(R.color.gray_variant)
                        , ((Activity) context).getResources().getColor(R.color.sky_blue)
                }
        );
        binding.radioDistrictBtn.setSupportButtonTintList(colorStateList);
        binding.radioPostalCodeBtn.setSupportButtonTintList(colorStateList);
        binding.radioStateBtn.setSupportButtonTintList(colorStateList);
        binding.radioGroupGender.setOnCheckedChangeListener((group, checkedId) -> {
            binding.btnFilter.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.sky_blue)));
            if (checkedId==R.id.radio_postal_code_btn){
                binding.postalCodeTxtInputLayout.setVisibility(View.VISIBLE);
            }else{
                binding.postalCodeTxtInputLayout.setVisibility(View.GONE);
            }
        });
    }


    public void showBottomSheetDialog() {
        binding.btnFilter.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.gray_variant_one)));
        bottomSheetDialog.show();
    }

}
