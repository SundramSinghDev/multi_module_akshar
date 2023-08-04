package com.pronted.dto.attendance

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pronted.dto.login.ClassRoomModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class AttendanceModel(
    @SerializedName("rollNbr")
    @Expose
    var rollNbr: String = "",
    @SerializedName("admissionNumber")
    @Expose
    var admissionNumber: String?,

    @SerializedName("firstName")
    @Expose
    var firstName: String = "",

    @SerializedName("lastName")
    @Expose
    var lastName: String = "",

    @SerializedName("date")
    @Expose
    var date: String = "",

    @SerializedName("shiftId")
    @Expose
    var shiftId: Int = 0,

    @SerializedName("shiftName")
    @Expose
    var shiftName: String = "",

    @SerializedName("attendanceInd")
    @Expose
    var attendanceInd: String?,

    @SerializedName("lateEntryFlag")
    @Expose
    var lateEntryFlag: String = "",

    @SerializedName("profileId")
    @Expose
    var profileId: Int = 0,

    @SerializedName("inTime")
    @Expose
    var inTime: String = "",

    @SerializedName("outTime")
    @Expose
    var outTime: String = "",

    @SerializedName("studentContact")
    @Expose
    var studentContact: String = "",

    var attendanceStatus: String = "",

    @SerializedName("classroom")
    var classRoomModel: ClassRoomModel?
) : Parcelable

@Parcelize
data class AttendanceChart(
    @SerializedName("key")
    @Expose
    var key: String = "",
    @SerializedName("value")
    @Expose
    var value: Int = 0
) : Parcelable