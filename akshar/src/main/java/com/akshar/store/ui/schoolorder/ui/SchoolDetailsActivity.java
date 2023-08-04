package com.akshar.store.ui.schoolorder.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;

import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.ActivitySchoolDetailsBinding;
import com.akshar.store.rest.ApiResponse;
import com.akshar.store.ui.schoolorder.fragments.AboutUsFragment;
import com.akshar.store.ui.schoolorder.fragments.ContactUsFragment;
import com.akshar.store.ui.schoolorder.fragments.ProductListFragment;
import com.akshar.store.ui.schoolorder.model.SchoolDetailsData;
import com.akshar.store.ui.schoolorder.viewmodel.SchoolViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SchoolDetailsActivity extends Ced_NavigationActivity {

    ActivitySchoolDetailsBinding schoolDetailsBinding;
    @Inject
    ViewModelFactory viewModelFactory;
    SchoolViewModel schoolViewModel;
    public static String sellers_id, sud_seller_id;
    // tab titles
    private final String[] titles = new String[]{"Our Products", "About Us", "Contact Us"};
    public String about_us = "Not Available";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            schoolDetailsBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_school_details, content, true);
            hideToolBar();
            selectMySchoolTab();
            schoolViewModel = new ViewModelProvider(this, viewModelFactory).get(SchoolViewModel.class);
            if (getIntent().hasExtra("seller_id") && getIntent().getStringExtra("seller_id") != null) {
                sellers_id = getIntent().getStringExtra("seller_id");
            }
            if (getIntent().hasExtra("sub_seller_id") && getIntent().getStringExtra("sub_seller_id") != null) {
                sud_seller_id = getIntent().getStringExtra("sub_seller_id");
            }
            getSchoolDetails();

            schoolDetailsBinding.backIv.setOnClickListener(v -> onBackPressed());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSchoolDetails() {
        try {
            JsonObject postParams = new JsonObject();
            postParams.addProperty("seller_id", sud_seller_id);
            schoolViewModel.onSchoolDetailsOrView(this, postParams).observe(this, this::onGetResponse);
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

    private void applyData(String response) {
        try {
            if (response != null) {
                JSONObject res_json_obj = new JSONObject(response);
                if (res_json_obj.has("success") && res_json_obj.getString("success").equals("true")) {
                    SchoolDetailsData schoolDetailsData = new GsonBuilder().create().fromJson(res_json_obj.getJSONObject("data").toString(), SchoolDetailsData.class);
                    if (schoolDetailsData.getSchoolData() != null && schoolDetailsData.getSchoolData().size() > 0) {
                        about_us = schoolDetailsData.getSchoolData().get(0).getAboutUs().get(0);
                        schoolDetailsBinding.setDataModel(schoolDetailsData.getSchoolData().get(0));

                    }
                    schoolDetailsBinding.viewPager.setAdapter(new SchoolDetailsPagerAdapter(getSupportFragmentManager()));
                    schoolDetailsBinding.viewPager.disableScroll(true);
                    schoolDetailsBinding.tabLayout.setupWithViewPager(schoolDetailsBinding.viewPager);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class SchoolDetailsPagerAdapter extends FragmentPagerAdapter {

        public SchoolDetailsPagerAdapter(@NonNull FragmentManager fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ProductListFragment();
                case 1:
                    return new AboutUsFragment();
                case 2:
                    return new ContactUsFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
