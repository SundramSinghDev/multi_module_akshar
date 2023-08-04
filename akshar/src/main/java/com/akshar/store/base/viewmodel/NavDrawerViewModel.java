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

public class NavDrawerViewModel extends ViewModel {
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public NavDrawerViewModel(Repository repository){
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getWebCheckoutData(Context context, String url){
        MutableLiveData<ApiResponse> mutableWebCheckoutData = new MutableLiveData<>();
        Call<Object> call = repository.getDataFromUrl(url);
        apiCall.postRequest(call, context, mutableWebCheckoutData,showloader);
        return mutableWebCheckoutData;
    }

    public MutableLiveData<ApiResponse> getCartData(Context context, JsonObject postData)
    {
        MutableLiveData<ApiResponse> mutableCartData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getViewCart(parameters);
        apiCall.postRequest(call, context, mutableCartData,showloader);
        return mutableCartData;
    }
    public MutableLiveData<ApiResponse> registerdevicetoserver(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableRegisterDevice = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.registerdevicetoserver(parameters);
        apiCall.postRequest(call, context, mutableRegisterDevice,showloader);
        return mutableRegisterDevice;
    }

    public MutableLiveData<ApiResponse> getStoresData(Context context, String url)
    {
        MutableLiveData<ApiResponse> mutableStoresData = new MutableLiveData<>();
        Call<Object> call = repository.getDataFromUrl(url);
        apiCall.postRequest(call, context, mutableStoresData,showloader);
        return mutableStoresData;
    }

    public MutableLiveData<ApiResponse> setStore(Context context, String store_id){
        MutableLiveData<ApiResponse> mutableStoreResponse = new MutableLiveData<>();
        Call<Object> call = repository.setstore(store_id);
        apiCall.postRequest(call, context, mutableStoreResponse,showloader);
        return mutableStoreResponse;
    }

    public MutableLiveData<ApiResponse> getAdditionalInfo(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableAdditionalInfoResponse = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.additionalInfo(parameters);
        apiCall.postRequest(call, context, mutableAdditionalInfoResponse,showloader);
        return mutableAdditionalInfoResponse;
    }
    /*------------Customer Mobile Login-----------*/
    public MutableLiveData<ApiResponse> sendOTP(Context context, JsonObject postData) {
        MutableLiveData<ApiResponse> mutableRegisterData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.onSendOTP(parameters);
        apiCall.postRequest(call, context, mutableRegisterData, showloader);
        return mutableRegisterData;
    }

    public MutableLiveData<ApiResponse> validateMobileNumber(Context context, JsonObject postData) {
        MutableLiveData<ApiResponse> mutableForgotPassData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.onValidateNumber(parameters);
        apiCall.postRequest(call, context, mutableForgotPassData, showloader);
        return mutableForgotPassData;
    }

    public MutableLiveData<ApiResponse> verifyOTP(Context context, JsonObject postData) {
        MutableLiveData<ApiResponse> mutableRegisterData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.onVerifyOTP(parameters);
        apiCall.postRequest(call, context, mutableRegisterData, showloader);
        return mutableRegisterData;
    }
}
