package com.akshar.store.ui.wishlistsection.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.akshar.store.repository.Repository;
import com.akshar.store.rest.ApiCall;
import com.akshar.store.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class WishListViewModel extends ViewModel {
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public WishListViewModel(Repository repository){
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getWishListData(Context context, JsonObject postData,String hashkey){
        MutableLiveData<ApiResponse> mutableWishListData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getWishList(parameters,hashkey);
        apiCall.postRequest(call, context, mutableWishListData,showloader);
        return mutableWishListData;
    }

    public MutableLiveData<ApiResponse> clearWishListData(Context context, JsonObject postData,String hashkey){
        MutableLiveData<ApiResponse> mutableClearData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.clearWishList(parameters,hashkey);
        apiCall.postRequest(call, context, mutableClearData,showloader);
        return mutableClearData;
    }

    public MutableLiveData<ApiResponse> removeFromWishList(Context context, JsonObject postData,String hashkey){
        MutableLiveData<ApiResponse> mutableRemoveData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.removeWishList(parameters,hashkey);
        apiCall.postRequest(call, context, mutableRemoveData,showloader);
        return mutableRemoveData;
    }
}
