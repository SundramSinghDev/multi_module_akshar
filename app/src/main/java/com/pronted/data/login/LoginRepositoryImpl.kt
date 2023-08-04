package com.pronted.data.login

import com.pronted.domain.login.LoginApi
import com.pronted.domain.login.LoginRepository
import com.pronted.dto.ApiResponse
import com.pronted.dto.login.LoginResponseModel
import com.pronted.dto.login.OtpResponseModel
import com.pronted.dto.login.UserAppList
import com.pronted.utils.extentions.handleApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Login repository impl
 *
 * @property loginApi
 */
class LoginRepositoryImpl(private val loginApi: LoginApi) : LoginRepository {

    override fun loginWithPhoneNumber(phoneNumber: String, otpFor:String): Flow<ApiResponse<OtpResponseModel>> =
        flow {
            emit(handleApi { loginApi.loginWithPhoneNumber(phoneNumber, otpFor) })
        }

    override fun validateOtp(
        mobileNumber: String,
        otp: String,
        deviceId: String
    ): Flow<ApiResponse<LoginResponseModel>> = flow {
        emit(handleApi {
            loginApi.validateOtp(
                mobileNumber = mobileNumber, otp = otp
            )
        })
    }


}