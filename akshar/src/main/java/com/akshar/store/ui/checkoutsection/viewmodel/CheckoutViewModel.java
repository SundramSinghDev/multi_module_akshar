package com.akshar.store.ui.checkoutsection.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.akshar.store.repository.Repository;
import com.akshar.store.rest.ApiCall;
import com.akshar.store.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class CheckoutViewModel extends ViewModel {
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public CheckoutViewModel(Repository repository){
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> saveBillingAddress(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableSaveAddressData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.saveBillingShipping(parameters);
        apiCall.postRequest(call, context, mutableSaveAddressData,showloader);
        return mutableSaveAddressData;
    }

    public MutableLiveData<ApiResponse> getShippingPaymentMethods(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableShippingPaymentData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getShippingPayment(parameters);
        apiCall.postRequest(call, context, mutableShippingPaymentData,showloader);
        return mutableShippingPaymentData;
    }

    public MutableLiveData<ApiResponse> saveShippingPaymentMethods(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableSaveMethodsData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.saveShippingPayment(parameters);
        apiCall.postRequest(call, context, mutableSaveMethodsData,showloader);
        return mutableSaveMethodsData;
    }

    public MutableLiveData<ApiResponse> saveOrder(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableSaveOrderResponse = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.saveOrder(parameters);
        apiCall.postRequest(call, context, mutableSaveOrderResponse,showloader);
        return mutableSaveOrderResponse;
    }

    public MutableLiveData<ApiResponse> getRazorPayOrderId(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableSaveOrderResponse = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getRazorPayOrderId(parameters);
        apiCall.postRequest(call, context, mutableSaveOrderResponse,showloader);
        return mutableSaveOrderResponse;
    }

    public MutableLiveData<ApiResponse> getAdditionalInfo(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableAdditionalInfoResponse = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.additionalInfo(parameters);
        apiCall.postRequest(call, context, mutableAdditionalInfoResponse,showloader);
        return mutableAdditionalInfoResponse;
    }
}
