package com.pronted.data.login

import com.pronted.domain.login.LoginRepository
import com.pronted.domain.login.LoginUseCase
import com.pronted.dto.ApiResponse
import com.pronted.dto.ErrorResponse
import com.pronted.dto.login.LoginAction
import com.pronted.dto.login.OtpAction
import com.pronted.dto.login.UserAccessAction
import com.pronted.utils.extentions.*
import com.base.*
import io.paperdb.Paper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginUseCaseImpl(private val loginRepository: LoginRepository) : LoginUseCase {
    override fun loginWithPhoneNumber(mobileNumber: String, otpFor:String): Flow<LoginAction> =
        loginRepository.loginWithPhoneNumber(mobileNumber, otpFor).map {
            when (it) {
                is ApiResponse.Success -> {
                    LoginAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    LoginAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    LoginAction.Fail(ErrorResponse())
                }
            }
        }

    override fun validateOtp(mobileNumber: String, otp: String, deviceId: String): Flow<OtpAction> =
        loginRepository.validateOtp(mobileNumber, otp, deviceId).map {
            when (it) {
                is ApiResponse.Success -> {
                    it.data.token?.let { _token ->
                        Preference.instance.putString(ACCESS_TOKEN, _token)
                    }
                    OtpAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    OtpAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    OtpAction.Fail(ErrorResponse())
                }
            }
        }


}