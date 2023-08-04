package com.akshar.store.ui.addresssection.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.akshar.store.repository.Repository;
import com.akshar.store.rest.ApiCall;
import com.akshar.store.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class AddressViewModel extends ViewModel {

    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public AddressViewModel(Repository repository){
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getCountriesData(Context context){
        MutableLiveData<ApiResponse> mutableCountriesData = new MutableLiveData<>();
        Call<Object> call = repository.getcountry();
        apiCall.postRequest(call, context, mutableCountriesData,showloader);
        return mutableCountriesData;
    }

    public MutableLiveData<ApiResponse> getStatesData(Context context, String country_code){
        MutableLiveData<ApiResponse> mutableStateData = new MutableLiveData<>();
        Call<Object> call = repository.getStates(country_code);
        apiCall.postRequest(call, context, mutableStateData,showloader);
        return mutableStateData;
    }

    public MutableLiveData<ApiResponse> saveAddressData(Context context, JsonObject postData,String hashkey){
        MutableLiveData<ApiResponse> mutableSaveAddressData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.saveAddress(parameters,hashkey);
        apiCall.postRequest(call, context, mutableSaveAddressData,showloader);
        return mutableSaveAddressData;
    }

    public MutableLiveData<ApiResponse> getRequiredFields(Context context){
        MutableLiveData<ApiResponse> mutableAddressFieldsData = new MutableLiveData<>();
        Call<Object> call = repository.getrequiredfields();
        apiCall.postRequest(call, context, mutableAddressFieldsData,showloader);
        return mutableAddressFieldsData;
    }

    public MutableLiveData<ApiResponse> getAddressListData(Context context, JsonObject postData,String hashkey){
        MutableLiveData<ApiResponse> mutableAddressListData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getAddressList(parameters,hashkey);
        apiCall.postRequest(call, context, mutableAddressListData,showloader);
        return mutableAddressListData;
    }

    public MutableLiveData<ApiResponse> deleteAddressData(Context context, JsonObject postData,String hashkey){
        MutableLiveData<ApiResponse> mutableDeleteAddressData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.deleteAddress(parameters,hashkey);
        apiCall.postRequest(call, context, mutableDeleteAddressData,showloader);
        return mutableDeleteAddressData;
    }

}
