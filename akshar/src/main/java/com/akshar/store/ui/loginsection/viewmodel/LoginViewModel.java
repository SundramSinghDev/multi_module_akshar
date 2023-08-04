package com.akshar.store.ui.loginsection.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.akshar.store.R;
import com.akshar.store.base.activity.Ced_MainActivity;
import com.akshar.store.base.activity.Ced_NavigationActivity;
import com.akshar.store.repository.Repository;
import com.akshar.store.rest.ApiCall;
import com.akshar.store.rest.ApiResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import retrofit2.Call;

public class LoginViewModel extends ViewModel {
    Boolean showloader=true;
    private JsonObject parameters;
    @Inject
    Repository repository;
    @Inject
    ApiCall apiCall;

    @Inject
    public LoginViewModel(Repository repository){
        this.repository = repository;
        apiCall = new ApiCall(repository);
        parameters = new JsonObject();
    }

    public MutableLiveData<ApiResponse> getLoginData(Context context, JsonObject postData){
        MutableLiveData<ApiResponse> mutableLoginData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getLoginData(parameters);
        apiCall.postRequest(call, context, mutableLoginData,showloader);
        return mutableLoginData;
    }

    public MutableLiveData<ApiResponse> getSocialLoginData(Context context, JsonObject postData) throws JSONException {
       /* parameters.add("parameters", postData);
        Call<Object> call = repository.getSocialLoginData(parameters);
        apiCall.postRequest(call, context, mutableSocialLoginData);*/
        MutableLiveData<ApiResponse> mutableSocialLoginData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getSocialLoginData(parameters);
        apiCall.postRequest(call, context, mutableSocialLoginData,showloader);
        return mutableSocialLoginData;
    }

    public MutableLiveData<ApiResponse> forgotPassword(Context context, JsonObject postData/*, String hashkey*/){
        MutableLiveData<ApiResponse> mutableForgotPassData = new MutableLiveData<>();
        parameters.add("parameters", postData);
        Call<Object> call = repository.getForgotPassData(parameters/*,hashkey*/);
        apiCall.postRequest(call, context, mutableForgotPassData,showloader);
        return mutableForgotPassData;
    }

    public void getFacebookKeyHash(Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(context.getApplicationContext(), Ced_MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.magenative_slide_in, R.anim.magenative_slide_out);
        }
    }
}
