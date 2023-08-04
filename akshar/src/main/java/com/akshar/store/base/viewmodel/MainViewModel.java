package com.akshar.store.base.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.akshar.store.repository.Repository;
import com.akshar.store.rest.ApiCall;
import com.akshar.store.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class MainViewModel extends ViewModel {
    Boolean showloader=false;
    private static final String TAG = "MainViewModel";

    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public MainViewModel(Repository repository) {
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> checkValidity(Context context, String url){
        MutableLiveData<ApiResponse> mutableValidityCheck = new MutableLiveData<>();
        Call<Object> call = repository.checkValidity(url);
        apiCall.postRequest(call, context, mutableValidityCheck,showloader);
        return mutableValidityCheck;
    }

    public MutableLiveData<ApiResponse> getModulesList(Context context, JsonObject postData) {
        MutableLiveData<ApiResponse> mutableModulesData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getModuleList(parameters);
        apiCall.postRequest(call, context, mutableModulesData,showloader);
        return mutableModulesData;
    }

    public MutableLiveData<ApiResponse> getAllCategories(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableCategoriesData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getAllCategories(parameters);
        apiCall.postRequest(call, context, mutableCategoriesData,showloader);
        return mutableCategoriesData;
    }

    public MutableLiveData<ApiResponse> getCartCount(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableCartCountData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getCartCount(parameters);
        apiCall.postRequest(call, context, mutableCartCountData,showloader);
        return mutableCartCountData;
    }

}
