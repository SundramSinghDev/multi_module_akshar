package com.pronted.utils.extentions

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.base.Preference
import com.pronted.dto.ApiResponse
import com.pronted.dto.ErrorResponse
import com.pronted.utils.ApiStatusCode
import com.pronted.utils.EventFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

/**
 * Coroutine delay
 *
 * @param time
 * @param dispatcher
 * @param callback
 * @receiver
 */
fun Fragment.coroutineDelay(
    time: Long, dispatcher: CoroutineDispatcher = Dispatchers.Main,
    callback: () -> Unit
) {
    lifecycleScope.launch(dispatcher) {
        delay(time)
        callback()
    }
}


suspend fun <T : Any> handleApi(
    execute: suspend () -> Response<T>
): ApiResponse<T> {
    return try {
        val response = execute()
        val body = response.body()
        Preference.instance.putBoolean(DYNAMIC_HEADERS, false)
        if (response.isSuccessful && body != null) {
            ApiResponse.Success(body)
        } else {

            val errorResponse: ErrorResponse? =
                response.errorBody()?.string()?.toModel<ErrorResponse>()
            if(errorResponse?.status == ApiStatusCode.Status_408){
                EventFlow.publishEvent(EVENT_LOGOUT,false)
            }
            ApiResponse.Error(errorResponse ?: ErrorResponse())
        }
    } catch (e: HttpException) {
        if(e.code() == ApiStatusCode.Status_408){
            EventFlow.publishEvent(EVENT_LOGOUT,false)
        }
        ApiResponse.Error(ErrorResponse(status = e.code(), error = e.message()))
    } catch (e: Throwable) {
        ApiResponse.Exception(e)
    }
}
