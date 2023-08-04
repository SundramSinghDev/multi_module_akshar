package com.pronted.dto.login

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

data class LoginResponseModel(
    @SerializedName("token") val token: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("isGuestUser") val isGuestUser: Boolean?,
    @SerializedName("appsList") val appsList: List<UserAppList>?,
    @SerializedName("route") val route: String?,
    @SerializedName("isEmployee") val isEmployee: Boolean?,
    @SerializedName("isPasswordChangeRequired") val isPasswordChangeRequired: Boolean?
)

@Parcelize
data class UserAppList(
    @SerializedName("isSelected")
    var isSelected: Boolean = false,
    @SerializedName("selectedPosition")
    @Expose
    var selectedPosition: Int = -1,
    @SerializedName("id")
    @Expose
    var id: Int = 0,
    @SerializedName("schoolCd")
    @Expose
    var schoolCd: String = "",
    @SerializedName("username")
    @Expose
    var username: String = "",
    @SerializedName("appName")
    @Expose
    var appName: String = "",
    @SerializedName("orgCodes")
    @Expose
    var orgCodes: String = "",
    @SerializedName("url")
    @Expose
    var url: String = "",
    @SerializedName("name")
    @Expose
    var name: String = "",
    @SerializedName("schoolCode")
    @Expose
    var schoolCode: String = "",
    @SerializedName("className")
    @Expose
    var className: String = "",
    @SerializedName("classroomId")
    @Expose
    var classRoomId: Int = 0,
    @SerializedName("studentCount")
    @Expose
    var studentCount: Int = 0,
    @SerializedName("schoolName")
    @Expose
    var schoolName: String = "",
    @SerializedName("studentName")
    @Expose
    var studentName: String = "",
    @SerializedName("employeeName")
    @Expose
    var employeeName: String = "",
    @SerializedName("classroom")
    @Expose
    var classroom: ClassRoomModel? = null,
    @SerializedName("studentContact")
    @Expose
    var studentContact: StudentModel? = null,
    @SerializedName("studentProfileId")
    @Expose
    var studentProfileId: Int = 0,
    @SerializedName("firstName")
    @Expose
    var firstName: String = "",
    @SerializedName("lastName")
    @Expose
    var lastName: String = "",
    @SerializedName("employeeCount")
    @Expose
    var employeeCount: Int = 0,
    @SerializedName("userUniqueId")
    @Expose
    var userUniqueId: Int = 0,
    @SerializedName("isDefault")
    @Expose
    var isDefault: Boolean = false,
    var profileImage: String? = ""
) : Parcelable

@Parcelize
data class ClassRoomModel(
    @SerializedName("classroomId")
    @Expose
    var classroomId: Int = 0,
    @SerializedName("classroomName")
    @Expose
    var classroomName: String = "",
    @SerializedName("displayOrder")
    @Expose
    var displayOrder: Int = 0,
    @SerializedName("courseName")
    @Expose
    var courseName: String = "",
    @SerializedName("degreeId")
    @Expose
    var degreeId: Int = 0,
    @SerializedName("degreeName")
    @Expose
    var degreeName: String = "",
    @SerializedName("deptId")
    @Expose
    var deptId: Int = 0,
    @SerializedName("deptName")
    @Expose
    var deptName: String = "",
    @SerializedName("courseId")
    @Expose
    var courseId: Int = 0,
) : Parcelable

@Parcelize
data class StudentModel(

    @SerializedName("studentProfileId")
    @Expose
    var studentProfileId: Int = 0,
    @SerializedName("address")
    @Expose
    var address: String = "",
    @SerializedName("primaryParentName")
    @Expose
    var primaryParentName: String = "",
    @SerializedName("primaryParentMobile")
    @Expose
    var primaryParentMobile: String = "",
    @SerializedName("primaryParentEmail")
    @Expose
    var primaryParentEmail: String = "",
    @SerializedName("primaryParentRelationship")
    @Expose
    var primaryParentRelationship: String = "",
    @SerializedName("primaryParentMobilesList")
    @Expose
    var primaryParentMobilesList: ArrayList<String>? = arrayListOf(),
    @SerializedName("primaryParentEmailsList")
    @Expose
    var primaryParentEmailsList: ArrayList<String>? = arrayListOf()
) : Parcelable


class BoardType(
    @SerializedName("type")
    @Expose
    var type: String = ""
) : Serializable