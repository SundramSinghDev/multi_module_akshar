package com.pronted.dto.login

import com.google.gson.annotations.SerializedName

data class OtpResponseModel(
    @SerializedName("recipientAddress") val recipientAddress: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("referenceId") val referenceId: String?,
    @SerializedName("errorCode") val errorCode: String?,
    @SerializedName("message") val message: String?
)