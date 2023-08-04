package com.akshar.store.ui.orderssection.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.akshar.store.repository.Repository;
import com.akshar.store.rest.ApiCall;
import com.akshar.store.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.http.Header;

public class OrderViewModel extends ViewModel {
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public OrderViewModel(Repository repository)
    {
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getOrdersData(Context context, JsonObject postData, String hashkey){
        MutableLiveData<ApiResponse> mutableOrdersData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getOrders(parameters,hashkey);
        apiCall.postRequest(call, context, mutableOrdersData,showloader);
        return mutableOrdersData;
    }

    public MutableLiveData<ApiResponse> getOrderViewData(Context context, JsonObject postData, String hashkey)
    {
        MutableLiveData<ApiResponse> mutableOrderViewData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getOrderView(parameters,hashkey);
        apiCall.postRequest(call, context, mutableOrderViewData,showloader);
        return mutableOrderViewData;
    }
    public MutableLiveData<ApiResponse> getReorder(Context context, JsonObject postData,String hashkey)
    {
        MutableLiveData<ApiResponse> mutableReorderData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getReorder(parameters,hashkey);
        apiCall.postRequest(call, context, mutableReorderData,showloader);
        return mutableReorderData;
    }
}
