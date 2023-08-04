package com.akshar.store.ui.sellersection.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.akshar.store.repository.Repository;
import com.akshar.store.rest.ApiCall;
import com.akshar.store.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class SellerShopViewModel extends ViewModel {
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public SellerShopViewModel(Repository repository)
    {
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getSellerShopData(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableProductListData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getSellerShops(parameters);
        apiCall.postRequest(call, context, mutableProductListData,showloader);
        return mutableProductListData;
    }

}