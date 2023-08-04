package com.akshar.store.rest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.custom_views.CustomLoader;
import com.akshar.store.databinding.MagenativeActivityNoModuleBinding;
import com.akshar.store.repository.Repository;
import com.akshar.store.ui.searchsection.activity.Ced_Searchview;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCall {
    private Repository repository;
    private CustomLoader customLoader;
    Dialog dialog = null;
    @Inject
    public ApiCall(Repository repository) {
        this.repository = repository;
    }

    public void postRequest(Call<Object> call, final Context context, MutableLiveData<ApiResponse> mutableLiveData,Boolean showloader) {
        if (!(context instanceof Ced_MainActivity)) {
            if (context != null && customLoader == null && showloader) {
                customLoader = new CustomLoader(context);
                customLoader.show();
            }
        }
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NotNull Call<Object> call, @NotNull Response<Object> response) {
                if (response.body() instanceof ArrayList) {
                    mutableLiveData.setValue(ApiResponse.success(new Gson().toJson(((ArrayList) response.body()).get(0))));
                } else {
                    mutableLiveData.setValue(ApiResponse.success(new Gson().toJson(response.body())));
                }
                if (customLoader != null && !(context instanceof Ced_MainActivity)) {
                    customLoader.dismiss();
                    customLoader = null;
                }
            }

            @Override
            public void onFailure(@NotNull Call<Object> call, @NotNull Throwable t) {
                mutableLiveData.setValue(ApiResponse.error(Objects.requireNonNull(t.getMessage())));
                if (customLoader != null && !(context instanceof Ced_MainActivity)) {
                    customLoader.dismiss();
                    customLoader = null;
                }
                showdialog_msg(context, t.getMessage(),call,mutableLiveData);
            }
        });
    }

    private void showdialog_msg(Context context, String message, Call<Object> call, MutableLiveData<ApiResponse> mutableLiveData) {
      //  Toast.makeText(context, "" +message, Toast.LENGTH_SHORT).show();
        MagenativeActivityNoModuleBinding binding= DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.magenative_activity_no_module,null,false);
        binding.cart2.setText(message);
        binding.conti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRequest(call.clone(),context, mutableLiveData,true);
                dialog.cancel();
            }
        });
        MaterialAlertDialogBuilder l= new MaterialAlertDialogBuilder(context, R.style.MaterialDialog)
                .setTitle(context.getResources().getString(R.string.app_name))
                .setIcon(R.mipmap.ic_launcher)
                .setView(binding.getRoot());
        dialog=l.create();
        dialog.show();
    }
}
