package com.pronted.domain.login

import com.pronted.dto.login.LoginAction
import com.pronted.dto.login.OtpAction
import com.pronted.dto.login.UserAccessAction
import kotlinx.coroutines.flow.Flow

/**
 * Login use case
 */
interface LoginUseCase {
    /**
     * Login with phone number
     *
     * @param mobileNumber
     * @param otpFor
     * @return
     */
    fun loginWithPhoneNumber(mobileNumber: String, otpFor:String): Flow<LoginAction>

    /**
     * Validate otp
     *
     * @param otp
     * @return
     */
    fun validateOtp(mobileNumber:String, otp: String, deviceId: String): Flow<OtpAction>

}