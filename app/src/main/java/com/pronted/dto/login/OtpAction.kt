package com.pronted.dto.login

import com.pronted.dto.ErrorResponse

sealed class OtpAction{

    class Success(val loginResponseModel: LoginResponseModel): OtpAction()

    class Fail(val errorResponse: ErrorResponse): OtpAction()
}
