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

public class SellerListViewModel extends ViewModel {

    Boolean showloader=true;
    private static final String TAG = "SellerListViewModel";
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public SellerListViewModel(Repository repository)
    {
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getSellerListData(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableProductListData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getSellerList(parameters);
        apiCall.postRequest(call, context, mutableProductListData,showloader);
        return mutableProductListData;
    }
    public MutableLiveData<ApiResponse> getCountriesData(Context context){
        MutableLiveData<ApiResponse> mutableCountriesData = new MutableLiveData<>();
        Call<Object> call = repository.getcountry();
        apiCall.postRequest(call, context, mutableCountriesData,showloader);
        return mutableCountriesData;
    }
    public MutableLiveData<ApiResponse> getStates(Context context, String country_code){
        MutableLiveData<ApiResponse> mutableStateData = new MutableLiveData<>();
        Call<Object> call = repository.getStates(country_code);
        apiCall.postRequest(call, context, mutableStateData,showloader);
        return mutableStateData;
    }
}