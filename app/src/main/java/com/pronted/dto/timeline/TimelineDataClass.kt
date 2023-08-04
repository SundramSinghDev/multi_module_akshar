package com.pronted.dto.timeline

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

data class TimetableData(
    @SerializedName("weekday")
    @Expose
    var weekDay: String = "",
    @SerializedName("periodwiseTimetableList")
    @Expose
    var periodWiseTimetableList: ArrayList<PeriodWiseTimetable>? = arrayListOf()
)

@Parcelize
data class PeriodWiseTimetable(
    @SerializedName("periodNumber")
    @Expose
    val periodNumber: Int = 0,
    @SerializedName("timetable")
    @Expose
    var timetable: ArrayList<TimeTableSchedule> = arrayListOf()
) : Parcelable

@Parcelize
data class TimeTableSchedule(
    @SerializedName("timeTableId")
    @Expose
    val timeTableId: Int = 0,
    @SerializedName("weekDay")
    @Expose
    var weekDay: String = "",

    @SerializedName("periodNumber")
    @Expose
    var periodNumber: String = "",
    @SerializedName("subjectId")
    @Expose
    val subjectId: Int = 0,
    @SerializedName("subjectName")
    @Expose
    var subjectName: String = "",

    @SerializedName("startTime")
    @Expose
    var startTime: String = "",

    @SerializedName("endTime")
    @Expose
    var endTime: String = "",

    @SerializedName("classroom")
    @Expose
    var classroom: ClassRoom? = null,

    @SerializedName("teacher")
    @Expose
    var teacher: TeacherModel? = null
) : Parcelable

@Parcelize
data class ClassRoom(
    @SerializedName("classroomId")
    @Expose
    var classroomId: Int = 0,
    @SerializedName("classroomName")
    @Expose
    var classroomName: String = "",
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
    @SerializedName("courseName")
    @Expose
    var courseName: String = "",

    @SerializedName("displayOrder")
    @Expose
    var displayOrder: Int = 0,
    @SerializedName("isActive")
    @Expose
    var isActive: String = ""
) : Parcelable

@Parcelize
data class TeacherModel(
    @SerializedName("employeeProfileId")
    @Expose
    var employeeProfileId: Int = 0,

    @SerializedName("employeeId")
    @Expose
    var employeeId: String = "",

    @SerializedName("fullName")
    @Expose
    var fullName: String = "",

    @SerializedName("department")
    @Expose
    var department: String = "",

    @SerializedName("designation")
    @Expose
    var designation: String = "",
    @SerializedName("namePrefix")
    @Expose
    var namePrefix: String = "",
    @SerializedName("firstName")
    @Expose
    var firstName: String = "",
    @SerializedName("middleName")
    @Expose
    var middleName: String = "",
    @SerializedName("lastName")
    @Expose
    var lastName: String = "",
    @SerializedName("phonePrimary")
    @Expose
    var phonePrimary: String = "",
    @SerializedName("gender")
    @Expose
    var gender: String = "",
    @SerializedName("email")
    @Expose
    var email: String = "",
    @SerializedName("imageUrl")
    @Expose
    var imageUrl: String = ""
) : Parcelable

@Parcelize
data class BirthdaysData(
    @SerializedName("content")
    @Expose
    val content: ArrayList<BirthDayModel> = arrayListOf(),
    @SerializedName("pageable")
    @Expose
    val pageable: Pageable?,
    @SerializedName("last")
    @Expose
    val last: Boolean = false,
    @SerializedName("totalPages")
    @Expose
    val totalPages: Int = 0,
    @SerializedName("totalElements")
    @Expose
    val totalElements: Int = 0,
    @SerializedName("sort")
    @Expose
    val sort: Sort?,
    @SerializedName("first")
    @Expose
    val first: Boolean = false,
    @SerializedName("number")
    @Expose
    val number: Int = 0,
    @SerializedName("numberOfElements")
    @Expose
    val numberOfElements: Int = 0,
    @SerializedName("size")
    @Expose
    val size: Int = 0,
    @SerializedName("empty")
    @Expose
    val empty: Boolean = false
) : Parcelable

@Parcelize
data class BirthDayModel(
    val firstName: String?,
    val lastName: String?,
    val degreeName: String?,
    val birthday: String?,
    val courseName: String?,
    val classroomName: String?,
    val profileType: String?,
    val deptName: String?,
    val imageUrl: String?,
    val rollNbr: String?,
    val admissionNumber: String?
) : Parcelable

@Parcelize
data class Pageable(
    @SerializedName("sort") val sort: Sort?,
    @SerializedName("pageNumber") val pageNumber: Int,
    @SerializedName("pageSize") val pageSize: Int = 0,
    @SerializedName("offset") val offset: Int = 0,
    @SerializedName("unpaged") val unPaged: Boolean = false,
    @SerializedName("paged") val paged: Boolean = false
) : Parcelable

@Parcelize
data class Sort(
    @SerializedName("sorted") val sorted: Boolean = false,
    @SerializedName("unsorted") val unsorted: Boolean = false,
    @SerializedName("empty") val empty: Boolean = false
) : Parcelable

@Parcelize
data class FinanceModel(
    val feeSummary: FeeSummeryModel?,
    val feePayment: ArrayList<FeePayment>?,
    val expenseSummary: ArrayList<FeePayment>?
) : Parcelable

@Parcelize
data class FeeSummeryModel(
    val feehead: String?,
    val term: String?,
    val feeAmount: Int,
    val concession: Int = 0,
    val amountAfterConcession: Int = 0,
    val amountPaid: Int = 0,
    val dueAmount: Int = 0,
    val overdueAmount: Int = 0
) : Parcelable

@Parcelize
data class FeePayment(
    @SerializedName("key")
    @Expose
    var key: String = "",
    @SerializedName("value")
    @Expose
    var value: Int = 0
) : Parcelable

