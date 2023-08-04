package com.pronted.dto.login

import com.google.gson.annotations.SerializedName

data class UserProfile(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("username") val userName: String,
    @SerializedName("appName") val appName: String?,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("orgCode") val orgCode: String?,
    @SerializedName("orgName") val orgName: String?,
    @SerializedName("schoolCode") val schoolCode: String?,
    @SerializedName("userUniqueId") val userUniqueId: String,
    @SerializedName("isDefault") val isDefault: String?,
    @SerializedName("schoolName") val schoolName: String?,
    @SerializedName("studentName") val studentName: String?,
    @SerializedName("employeeName") val employeeName: String?,
    @SerializedName("className") val className: String?,
    @SerializedName("classroomId") val classroomId: String?,
    @SerializedName("route") val route: String?
)
