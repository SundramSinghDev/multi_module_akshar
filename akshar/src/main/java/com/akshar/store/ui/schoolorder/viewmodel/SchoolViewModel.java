package com.akshar.store.ui.schoolorder.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.akshar.store.repository.Repository;
import com.akshar.store.rest.ApiCall;
import com.akshar.store.rest.ApiResponse;
import com.google.gson.JsonObject;

import javax.inject.Inject;

import retrofit2.Call;

public class SchoolViewModel extends ViewModel {

    Boolean showLoader = true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public SchoolViewModel(Repository repository) {
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getVendorList(Context context, JsonObject postData) {
        MutableLiveData<ApiResponse> mutableOrdersData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.vendorList(parameters);
        apiCall.postRequest(call, context, mutableOrdersData, showLoader);
        return mutableOrdersData;
    }

    public MutableLiveData<ApiResponse> onSchoolDetailsOrView(Context context, JsonObject postData) {
        MutableLiveData<ApiResponse> mutableOrdersData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.schoolDetailsOrView(parameters);
        apiCall.postRequest(call, context, mutableOrdersData, showLoader);
        return mutableOrdersData;
    }

    public MutableLiveData<ApiResponse> getVendorProducts(Context context, JsonObject postData) {
        MutableLiveData<ApiResponse> mutableOrdersData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getVendorProducts(parameters);
        apiCall.postRequest(call, context, mutableOrdersData, showLoader);
        return mutableOrdersData;
    }

    public MutableLiveData<ApiResponse> getSchoolContactFormField(Context context){
        MutableLiveData<ApiResponse> mutableFieldsData = new MutableLiveData<>();
        Call<Object> call = repository.getSchoolContactFormField();
        apiCall.postRequest(call, context, mutableFieldsData,showLoader);
        return mutableFieldsData;
    }

    public MutableLiveData<ApiResponse> addToWishList(Context context, JsonObject postData,String hashkey){
        MutableLiveData<ApiResponse> mutableAddWishListData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.addToWishList(parameters,hashkey);
        apiCall.postRequest(call, context, mutableAddWishListData,showLoader);
        return mutableAddWishListData;
    }

    public MutableLiveData<ApiResponse> removeFromWishList(Context context, JsonObject postData,String hashkey){
        MutableLiveData<ApiResponse> mutableRemoveWishListData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.removeWishList(parameters,hashkey);
        apiCall.postRequest(call, context, mutableRemoveWishListData,showLoader);
        return mutableRemoveWishListData;
    }
}
