package com.akshar.store.ui.schoolorder.ui;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.ActivitySchoolListBinding;
import com.akshar.store.databinding.SchoolFilterLayoutBinding;
import com.akshar.store.rest.ApiResponse;
import com.akshar.store.ui.schoolorder.adapter.SchoolListAdapter;
import com.akshar.store.ui.schoolorder.model.Response;
import com.akshar.store.ui.schoolorder.viewmodel.SchoolViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SchoolListActivity extends Ced_NavigationActivity {

    ActivitySchoolListBinding schoolListBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    SchoolViewModel schoolViewModel;
    SchoolListAdapter adapter;
    boolean loaDing = true, firstTime = true;
    int current = 1;
    @Inject
    FilterBottomSheetDialog filterBottomSheetDialog;
    JsonObject postParams;
    int total_item = 0;
    String search_query = "";
    boolean isFromSchoolOrder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            schoolListBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_school_list, content, true);
            schoolListBinding.setClickListeners(this);
            schoolViewModel = new ViewModelProvider(this, viewModelFactory).get(SchoolViewModel.class);
            filterBottomSheetDialog.init(SchoolListActivity.this);
            selectMySchoolTab();
            postParams = new JsonObject();
            showBackButtonWithTitle("School Orders");
            if (getIntent().hasExtra("from")) {
                isFromSchoolOrder = true;
            }
            schoolListBinding.schoolListRv.setLayoutManager(new LinearLayoutManager(this));
            onGetVendorList(current);
            schoolListBinding.schoolListRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && loaDing) {
                        if (search_query.length() == 0) {
                            current = current + 1;
                            onGetVendorList(current);
                        }
                    }
                }
            });

            schoolListBinding.MageNativeEditTextSearch.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(schoolListBinding.MageNativeEditTextSearch.getText())) {
                        firstTime = true;
                        total_item = 0;
                        search_query = schoolListBinding.MageNativeEditTextSearch.getText().toString().trim();
                        onGetVendorList(current);
                    } else {
                        showmsg(getString(R.string.please_enter_some_word));
                    }
                    return true;
                }
                return false;
            });

            schoolListBinding.MageNativeEditTextSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 0) {
                        search_query = "";
                        total_item = 0;
                        firstTime = true;
                        onGetVendorList(1);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onGetVendorList(int current) {
        try {
            postParams.addProperty("store_id", cedSessionManagement.getStoreId());
            postParams.addProperty("page_size", "10");
            postParams.addProperty("current_page", current);
            postParams.addProperty("query", search_query);
            postParams.addProperty("filter", "");
            schoolViewModel.getVendorList(this, postParams).observe(this, this::onGetResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onGetResponse(ApiResponse apiResponse) {
        try {
            switch (apiResponse.status) {
                case SUCCESS:
                    applyData(apiResponse.data);
                    break;
                case ERROR:
                    Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                    showmsg(getResources().getString(R.string.errorString));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    private void applyData(String data) {
        try {
            JSONObject response = new JSONObject(data);
            if (response.has("success") && response.getString("success").equals("true")) {
                Response response1 = new GsonBuilder().create().fromJson(response.getJSONObject("data").toString(), Response.class);
                if (response1.getSchoolList().size() > 0) {
                    total_item = total_item + response1.getSchoolList().size();
                    schoolListBinding.totalNumberItem.setText(total_item + " Schools");
                    if (firstTime) {
                        firstTime = false;
                        adapter = new SchoolListAdapter(response1.getSchoolList(), this);
                        schoolListBinding.schoolListRv.setAdapter(adapter);
                    } else {
                        adapter.data.addAll(response1.getSchoolList());
                    }
                    adapter.isFromSchoolOrder = isFromSchoolOrder;
                    adapter.notifyDataSetChanged();
                } else {
                    if (firstTime) {
                        schoolListBinding.emptyMsg.setVisibility(View.VISIBLE);
                    }
                    showmsg(getString(R.string.no_more_data_available));
                    loaDing = false;
                }
            } else {
                showmsg(getString(R.string.no_more_data_available));
                loaDing = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onFilterButtonClicked(View view) {
        try {
            filterBottomSheetDialog.showBottomSheetDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class FilterBottomSheetDialog {
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
                if (checkedId == R.id.radio_postal_code_btn) {
                    binding.postalCodeTxtInputLayout.setVisibility(View.VISIBLE);
                } else {
                    binding.postalCodeTxtInputLayout.setVisibility(View.GONE);
                }
            });
            binding.btnFilter.setOnClickListener(v -> bottomSheetDialog.dismiss());
            binding.clearFilterTv.setOnClickListener(v -> bottomSheetDialog.dismiss());
        }


        public void showBottomSheetDialog() {
            binding.btnFilter.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.gray_variant_one)));
            bottomSheetDialog.show();
        }

    }
}