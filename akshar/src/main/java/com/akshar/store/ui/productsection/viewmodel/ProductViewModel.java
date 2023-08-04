package com.akshar.store.ui.productsection.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.akshar.store.repository.Repository;
import com.akshar.store.rest.ApiCall;
import com.akshar.store.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class ProductViewModel extends ViewModel {
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public ProductViewModel(Repository repository){
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getProductViewData(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableProductViewData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getProductView(parameters);
        apiCall.postRequest(call, context, mutableProductViewData,showloader);
        return mutableProductViewData;
    }

    public MutableLiveData<ApiResponse> addToWishList(Context context, JsonObject postData,String hashkey){
        MutableLiveData<ApiResponse> mutableAddWishListData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.addToWishList(parameters,hashkey);
        apiCall.postRequest(call, context, mutableAddWishListData,showloader);
        return mutableAddWishListData;
    }

    public MutableLiveData<ApiResponse> removeFromWishList(Context context, JsonObject postData,String hashkey){
        MutableLiveData<ApiResponse> mutableRemoveWishListData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.removeWishList(parameters,hashkey);
        apiCall.postRequest(call, context, mutableRemoveWishListData,showloader);
        return mutableRemoveWishListData;
    }
    public MutableLiveData<ApiResponse> saveshoolcode(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableRemoveWishListData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.saveshoolcode(parameters);
        apiCall.postRequest(call, context, mutableRemoveWishListData,showloader);
        return mutableRemoveWishListData;
    }

    public MutableLiveData<ApiResponse> addToCart(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableAddToCartData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.addToCart(parameters);
        apiCall.postRequest(call, context, mutableAddToCartData,showloader);
        return mutableAddToCartData;
    }
    public MutableLiveData<ApiResponse> addToCompare(Context context, JsonObject postData,String hashkey){
        MutableLiveData<ApiResponse> mutableAddToCompareData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.addToCompare(parameters,hashkey);
        apiCall.postRequest(call, context, mutableAddToCompareData,showloader);
        return mutableAddToCompareData;
    }
    public MutableLiveData<ApiResponse> viewimage(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableviewimage = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.viewimage(parameters);
        apiCall.postRequest(call, context, mutableviewimage,showloader);
        return mutableviewimage;
    }
}
