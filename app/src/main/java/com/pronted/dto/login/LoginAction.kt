package com.pronted.dto.login

import com.pronted.dto.ErrorResponse

sealed class LoginAction{

    class Success(val otpResponseModel: OtpResponseModel): LoginAction()

    class Fail(val errorResponse: ErrorResponse): LoginAction()
}
