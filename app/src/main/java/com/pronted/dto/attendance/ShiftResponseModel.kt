package com.pronted.dto.attendance

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShiftResponseModel(
    @SerializedName("shiftId")
    val shiftId: Int?,
    @SerializedName("name")
    val name : String?,
    @SerializedName("schoolCd")
    val schoolCd: String?,
    @SerializedName("profileType")
    val profileType: String?,
    @SerializedName("startTime")
    val startTime :String?,
    @SerializedName("endTime")
    val endTime: String?,
    @SerializedName("graceTime")
    val graceTime: String?,
    @SerializedName("punchStartTime")
    val punchStartTime: String?,
    @SerializedName("punchEndTime")
    val punchEndTime: String?,
    @SerializedName("mappedCount")
    val mappedCount: Int?,
    @SerializedName("assignmentType")
    val assignmentType: String?,
    @SerializedName("isActive")
    val isActive: String?,
    @SerializedName("actualCount")
    val actualCount: Int?
): Parcelable
