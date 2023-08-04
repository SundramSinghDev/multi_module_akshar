package com.pronted.presentation.authentication

import androidx.lifecycle.ViewModel
import com.pronted.domain.login.LoginUseCase
import com.pronted.dto.login.LoginAction
import kotlinx.coroutines.flow.Flow

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {

    fun loginWithPhoneNumber(phoneNumber: String, otpFor:String): Flow<LoginAction> =
        loginUseCase.loginWithPhoneNumber(phoneNumber, otpFor)
}