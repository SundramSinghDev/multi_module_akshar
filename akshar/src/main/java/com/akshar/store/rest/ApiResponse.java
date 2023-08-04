package com.akshar.store.rest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonElement;
import com.akshar.store.utils.Status;
import com.akshar.store.utils.Urls;

import static com.akshar.store.utils.Status.ERROR;
import static com.akshar.store.utils.Status.LOADING;
import static com.akshar.store.utils.Status.SUCCESS;

public class ApiResponse {
    public final Status status;
    @Nullable
    public final String data;
    @Nullable
    public final String error;

    private ApiResponse(Status status, @Nullable String data, @Nullable String error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse loading() {
        return new ApiResponse(LOADING, null, null);
    }

    public static ApiResponse success(@NonNull String data) {
        return new ApiResponse(SUCCESS, data, null);
    }

    public static ApiResponse error(@NonNull String error) {
        Log.e(Urls.TAG, "error: "+error);

        return new ApiResponse(ERROR, null, error);
    }
}
