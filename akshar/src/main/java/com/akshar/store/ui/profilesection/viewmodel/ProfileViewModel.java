package com.akshar.store.ui.profilesection.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.akshar.store.repository.Repository;
import com.akshar.store.rest.ApiCall;
import com.akshar.store.rest.ApiResponse;

import org.json.JSONObject;

import javax.inject.Inject;

import retrofit2.Call;

public class ProfileViewModel extends ViewModel {
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public ProfileViewModel(Repository repository){
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getProfileFieldsData(Context context, String url){
        MutableLiveData<ApiResponse> mutableProfileFieldsData = new MutableLiveData<>();
        Call<Object> call = repository.getDataFromUrl(url);
        apiCall.postRequest(call, context, mutableProfileFieldsData,showloader);
        return mutableProfileFieldsData;
    }

    public MutableLiveData<ApiResponse> getProfileUpdateData(Context context, JsonObject postData, String hashkey){
        MutableLiveData<ApiResponse> mutableProfileUpdateData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getProfileUpdate(parameters,hashkey);
        apiCall.postRequest(call, context, mutableProfileUpdateData,showloader);
        return mutableProfileUpdateData;
    }
}
