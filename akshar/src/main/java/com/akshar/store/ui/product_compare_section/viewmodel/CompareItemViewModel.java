package com.akshar.store.ui.product_compare_section.viewmodel;

/*public class CompareItemViewModel {
}*/

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.akshar.store.repository.Repository;
import com.akshar.store.rest.ApiCall;
import com.akshar.store.rest.ApiResponse;

import javax.inject.Inject;

import retrofit2.Call;

public class CompareItemViewModel  extends ViewModel
{
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public CompareItemViewModel(Repository repository)
    {
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> removefrom_Compare(Context context, JsonObject postData,String hashkey){
        MutableLiveData<ApiResponse> mutableData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.removefrom_Compare(parameters,hashkey);
        apiCall.postRequest(call, context, mutableData,showloader);
        return mutableData;
    }
  public MutableLiveData<ApiResponse> addtocart(Context context, JsonObject postData){
      MutableLiveData<ApiResponse> mutablecartData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.addToCart(parameters);
        apiCall.postRequest(call, context, mutablecartData,showloader);
        return mutablecartData;
    }


}
