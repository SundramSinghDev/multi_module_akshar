package com.akshar.store.ui.loginsection.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.akshar.store.repository.Repository;
import com.akshar.store.rest.ApiCall;
import com.akshar.store.rest.ApiResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import retrofit2.Call;

public class RegisterViewModel extends ViewModel {
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public RegisterViewModel(Repository repository){
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getFieldsData(Context context){
        MutableLiveData<ApiResponse> mutableFieldsData = new MutableLiveData<>();
        Call<Object> call = repository.getRegisterFields();
        apiCall.postRequest(call, context, mutableFieldsData,showloader);
        return mutableFieldsData;
    }

    public MutableLiveData<ApiResponse> getRegisterData(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableRegisterData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getRegisterData(parameters);
        apiCall.postRequest(call, context, mutableRegisterData,showloader);
        return mutableRegisterData;
    }

    public MutableLiveData<ApiResponse> getLoginData(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableLoginData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getLoginData(parameters);
        apiCall.postRequest(call, context, mutableLoginData,showloader);
        return mutableLoginData;
    }

}
