package com.pronted.domain.login

import com.pronted.dto.login.LoginResponseModel
import com.pronted.dto.login.OtpResponseModel
import com.pronted.dto.login.UserAppList
import retrofit2.Response
import retrofit2.http.*

/**
 * Login api
 */
interface LoginApi {
    @POST("auth/otp/send")
    suspend fun loginWithPhoneNumber(@Query("mobile") phoneNumber: String,
                                     @Query("otpModule") otpModule: String): Response<OtpResponseModel>

    @FormUrlEncoded
    @POST("auth/validateOtp")
    suspend fun validateOtp(
        @Field("username") mobileNumber: String,
        @Field("password") otp: String
    ): Response<LoginResponseModel>

}