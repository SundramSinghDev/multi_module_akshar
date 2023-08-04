package com.pronted.presentation.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pronted.domain.login.LoginUseCase
import com.pronted.dto.ErrorResponse
import com.pronted.dto.login.LoginAction
import com.pronted.dto.login.OtpAction
import com.pronted.dto.login.UserAccessAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OtpViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private var errorResponseMutableLiveData: MutableLiveData<ErrorResponse> = MutableLiveData()
    private val isLoading = MutableLiveData<Boolean>()


    /**
     * Validate otp
     *
     * @param mobileNumber
     * @param otp
     * @param deviceId
     */
    fun validateOtp(mobileNumber: String, otp: String, deviceId: String): Flow<OtpAction> =
        loginUseCase.validateOtp(mobileNumber, otp, deviceId)

    /**
     * Login with phone number
     *
     * @param phoneNumber
     */
    fun loginWithPhoneNumber(phoneNumber: String, otpFor:String): Flow<LoginAction> =
        loginUseCase.loginWithPhoneNumber(phoneNumber, otpFor)

}