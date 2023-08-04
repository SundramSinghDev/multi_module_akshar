package com.pronted.dto.student

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class StudentProfileResponse(

    @SerializedName("studentProfileId")
    val studentProfileId: Int = 0,
    @SerializedName("schoolCd")
    val schoolCd: String = "",
    @SerializedName("rollNbr")
    val rollNbr: String? = "",
    @SerializedName("admissionNumber")
    @Expose
    var admissionNumber: String? = null,
    @SerializedName("firstName")
    @Expose
    var firstName: String? = null,
    @SerializedName("lastName")
    @Expose
    var lastName: String? = null,
    @SerializedName("classroomId")
    val classroomId: Int? = 0,
    @SerializedName("classroom")
    val classroom: ClassRoomModel? = null,
    @SerializedName("gender")
    @Expose
    var gender: String? = null,
    @SerializedName("dateOfBirth")
    @Expose
    var dateOfBirth: String? = null,
    @SerializedName("bloodGroup")
    @Expose
    var bloodGroup: String? = null,
    @SerializedName("recordStatus")
    @Expose
    var recordStatus: String? = null,
    @SerializedName("imageUrl")
    @Expose
    var imageUrl: String? = null,
    @SerializedName("transportationFlag")
    @Expose
    var transportationFlag: String? = null,
    @SerializedName("houseName")
    @Expose
    var houseName: String? = null,
    @SerializedName("feeCategory")
    @Expose
    var feeCategory: String? = null,
    @SerializedName("isVerified")
    @Expose
    var isVerified: String? = null,
    @SerializedName("courseName")
    @Expose
    var courseName: String? = null,
    @SerializedName("fullName")
    @Expose
    var fullName: String = "",
    @SerializedName("studentContact")
    @Expose
    var studentContact: StudentContact? = null,
    @SerializedName("isSelected")
    val isSelected: Boolean = false

) : Serializable


data class StudentCount(
    @SerializedName("value")
    @Expose
    var value: Int = 0
) : Serializable

data class BloodGroup(
    @SerializedName("lookupSetupId")
    @Expose
    var lookupSetupId: Int = 0,

    @SerializedName("module")
    @Expose
    var module: String = "",

    @SerializedName("fieldName")
    @Expose
    var fieldName: String = "",

    @SerializedName("displayName")
    @Expose
    var displayName: String = "",

    @SerializedName("value")
    @Expose
    var value: String = ""
)
