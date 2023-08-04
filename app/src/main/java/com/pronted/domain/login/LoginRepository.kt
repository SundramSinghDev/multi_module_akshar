package com.pronted.domain.login

import com.pronted.dto.ApiResponse
import com.pronted.dto.login.OtpResponseModel
import com.pronted.dto.login.LoginResponseModel
import com.pronted.dto.login.UserAppList
import kotlinx.coroutines.flow.Flow

/**
 * Login repository
 */
interface LoginRepository {
    /**
     * Login with phone number
     *
     * @param phoneNumber
     * @param otpFor
     */
    fun loginWithPhoneNumber(phoneNumber: String, otpFor:String): Flow<ApiResponse<OtpResponseModel>>

    /**
     * Validate otp
     *
     * @param mobileNumber
     * @param otp
     * @param deviceId
     */
    fun validateOtp(mobileNumber: String, otp: String, deviceId: String):
            Flow<ApiResponse<LoginResponseModel>>


}