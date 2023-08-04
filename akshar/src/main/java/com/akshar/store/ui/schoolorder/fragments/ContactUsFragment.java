package com.akshar.store.ui.schoolorder.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.databinding.FragmentContactUsBinding;
import com.akshar.store.databinding.LayoutEdittextFieldBinding;
import com.akshar.store.databinding.MagenativeFragmentLoadDescriptionBinding;
import com.akshar.store.rest.ApiResponse;
import com.akshar.store.ui.schoolorder.viewmodel.SchoolViewModel;
import com.akshar.store.utils.Urls;
import com.akshar.store.utils.ViewModelFactory;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

public class ContactUsFragment extends Fragment {

    public ContactUsFragment() {
        // Required empty public constructor
    }

    @Inject
    ViewModelFactory viewModelFactory;
    SchoolViewModel schoolViewModel;
    JSONObject requiredFields, response;
    JsonObject post_param;
    String label, value, is_required, type, name, file_value;
    FragmentContactUsBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requiredFields = new JSONObject();
        post_param = new JsonObject();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_contact_us, null, false);
        schoolViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(SchoolViewModel.class);
        getFormFields();
        binding.btnContactUs.setOnClickListener(v -> Log.i("POST_PARAMS", "onClick: " + post_param.toString()));
        return binding.getRoot();
    }

    private void getFormFields() {
        schoolViewModel.getSchoolContactFormField(getContext()).observe(getViewLifecycleOwner(), this::onGetResponse);
    }

    private void onGetResponse(ApiResponse apiResponse) {
        try {
            switch (apiResponse.status) {
                case SUCCESS:
                    applyData(apiResponse.data);
                    break;
                case ERROR:
                    Log.e(Urls.TAG, Objects.requireNonNull(apiResponse.error));
                    ((Ced_NavigationActivity) requireContext()).showmsg(getResources().getString(R.string.errorString));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyData(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("success") && jsonObject.getString("success").equals("true")) {
                if (jsonObject.has("data")) {
                    JSONArray form_field_array = jsonObject.getJSONArray("data");
                    if (form_field_array.length() > 0) {

                        for (int i = 0; i < form_field_array.length(); i++) {
                            JSONObject fields = form_field_array.getJSONObject(i);
                            type = fields.getString("type");
                            name = fields.getString("name");
                            if (fields.has("label"))
                                label = fields.getString("label");

                            int int_value;
                            if (fields.has("value")) {
                                if (fields.getString("value").equals("1.0") || fields.getString("value").equals("0.0")) {
                                    value = String.valueOf(fields.getInt("value"));
                                } else {
                                    value = fields.getString("value");
                                }
                            } else
                                value = "";

                            if (fields.has("is_required")) {
                                is_required = String.valueOf(fields.getInt("is_required"));
                                if (is_required.equals("1")) {
                                    requiredFields.put(name, label);
                                    label = label + "*";
                                }
                            } else
                                is_required = "0";

                            switch (type) {
                                case "text":
                                case "email":
                                case "textarea":
                                    create_fields(type, "main", fields);
                                    break;
                            }
                            Log.e("VALUE", "requiredFields=" + i + "= " + requiredFields);
                            Log.e("VALUE", "post_param=" + i + "= " + post_param);
                        }

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void create_fields(String type, String origin, JSONObject fields) {
        try {
            Log.e("VALUE", "type==" + type);
            LayoutEdittextFieldBinding layoutEdittextFieldBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_edittext_field, null, false);
            switch (type) {
                case "email":
                    layoutEdittextFieldBinding.fieldValueEdit.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    post_param.addProperty(name, Objects.requireNonNull(layoutEdittextFieldBinding.fieldValueEdit.getText()).toString());
                    layoutEdittextFieldBinding.fieldValueEdit.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            try {
                                Log.e("VALUE", fields.getString("name") + "= " + Objects.requireNonNull(layoutEdittextFieldBinding.fieldValueEdit.getText()).toString());
                                if (post_param.has(name)) {
                                    post_param.remove(name);
                                    post_param.addProperty(fields.getString("name"), Objects.requireNonNull(layoutEdittextFieldBinding.fieldValueEdit.getText()).toString());
                                }
                                if (requiredFields.has(name))
                                    requiredFields.remove(name);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    break;
                case "text":
                    if (name.equalsIgnoreCase("captcha")) {
                        layoutEdittextFieldBinding.fieldValueCaptcha.setVisibility(View.VISIBLE);
                        layoutEdittextFieldBinding.fieldLabel.setVisibility(View.VISIBLE);
                        layoutEdittextFieldBinding.fieldValueCaptcha.setText(value);
                        layoutEdittextFieldBinding.fieldLabel.setText(label);
                        layoutEdittextFieldBinding.fieldValueCaptcha.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                try {
                                    post_param.addProperty(fields.getString("name"), Objects.requireNonNull(layoutEdittextFieldBinding.fieldValueCaptcha.getText()).toString());
                                    if (requiredFields.has(name))
                                        requiredFields.remove(name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        layoutEdittextFieldBinding.fieldValueEdit.setVisibility(View.GONE);
                    }
                    layoutEdittextFieldBinding.fieldValueEdit.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            try {
                                post_param.addProperty(fields.getString("name"), Objects.requireNonNull(layoutEdittextFieldBinding.fieldValueEdit.getText()).toString());
                                if (requiredFields.has(name))
                                    requiredFields.remove(name);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    break;
                case "textarea":
                    layoutEdittextFieldBinding.textAreaParent.setVisibility(View.VISIBLE);
                    layoutEdittextFieldBinding.fieldValueTextArea.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            try {
                                post_param.addProperty(fields.getString("name"), Objects.requireNonNull(layoutEdittextFieldBinding.fieldValueTextArea.getText()).toString());
                                if (requiredFields.has(name))
                                    requiredFields.remove(name);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    layoutEdittextFieldBinding.fieldValueEditParent.setVisibility(View.GONE);
                    layoutEdittextFieldBinding.textAreaParent.setHint(label);
                    break;
            }
            layoutEdittextFieldBinding.fieldValueEditParent.setHint(label);
            layoutEdittextFieldBinding.fieldType.setText(type);
            layoutEdittextFieldBinding.fieldName.setText(name);
            layoutEdittextFieldBinding.fieldIsRequired.setText(is_required);

            if (origin.equalsIgnoreCase("main")) {
                layoutEdittextFieldBinding.mainLayout.setVisibility(View.VISIBLE);
                binding.layoutContactUsForm.addView(layoutEdittextFieldBinding.getRoot());
            } else {
                layoutEdittextFieldBinding.mainLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}