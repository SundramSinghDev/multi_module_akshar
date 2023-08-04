package com.akshar.store.ui.cartsection.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.akshar.store.repository.Repository;
import com.akshar.store.rest.ApiCall;
import com.akshar.store.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class CartViewModel extends ViewModel {
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public CartViewModel(Repository repository) {
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> viewCart(Context context, JsonObject postData) {
        MutableLiveData<ApiResponse> mutableCartData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getViewCart(parameters);
        apiCall.postRequest(call, context, mutableCartData,showloader);
        return mutableCartData;
    }

    public MutableLiveData<ApiResponse> emptyCart(Context context, JsonObject postData) {
        MutableLiveData<ApiResponse> mutableEmptyCartResponse = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.emptyCart(parameters);
        apiCall.postRequest(call, context, mutableEmptyCartResponse,showloader);
        return mutableEmptyCartResponse;
    }
    public MutableLiveData<ApiResponse> applycoupon(Context context, JsonObject postData) {
        MutableLiveData<ApiResponse> mutableapplycouponResponse = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.applycoupon(parameters);
        apiCall.postRequest(call, context, mutableapplycouponResponse,showloader);
        return mutableapplycouponResponse;
    }


    public MutableLiveData<ApiResponse> deleteFromCart(Context context, JsonObject postData) {
        MutableLiveData<ApiResponse> mutableDeleteCartResponse = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.deleteFromCart(parameters);
        apiCall.postRequest(call, context, mutableDeleteCartResponse,showloader);
        return mutableDeleteCartResponse;
    }

    public MutableLiveData<ApiResponse> updateCart(Context context, JsonObject postData) {
        MutableLiveData<ApiResponse> mutableUpdateCartResponse = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.updateCart(parameters);
        apiCall.postRequest(call, context, mutableUpdateCartResponse,showloader);
        return mutableUpdateCartResponse;
    }
}
