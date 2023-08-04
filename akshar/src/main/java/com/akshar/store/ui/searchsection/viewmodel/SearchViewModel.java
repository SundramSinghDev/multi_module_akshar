package com.akshar.store.ui.searchsection.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.akshar.store.repository.Repository;
import com.akshar.store.rest.ApiCall;
import com.akshar.store.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class SearchViewModel extends ViewModel {
    Boolean showloader=false;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public SearchViewModel(Repository repository){
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getAutoCompleteData(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableAutoCompleteData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getAutoCompleteData(parameters);
        apiCall.postRequest(call, context, mutableAutoCompleteData,showloader);
        return mutableAutoCompleteData;
    }

    public MutableLiveData<ApiResponse> getSearchListData(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableSearchData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getSearchList(parameters);
        apiCall.postRequest(call, context, mutableSearchData,showloader);
        return mutableSearchData;
    }

}
