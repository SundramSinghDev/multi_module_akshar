package com.pronted.dto.student.college

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CollegeDropDownModel(
    @SerializedName("degreeId")
    @Expose
    var degreeId: Int = 0,

    @SerializedName("degreeName")
    @Expose
    var degreeName: String = "",

    @SerializedName("displayOrder")
    @Expose
    var displayOrder: String = "",

    @SerializedName("isActive")
    @Expose
    var isActive: String = "",

    @SerializedName("deptList")
    @Expose
    var deptList: ArrayList<DepartmentModel> = arrayListOf()
) : Serializable

data class DepartmentModel(
    @SerializedName("degreeId")
    @Expose
    var degreeId: Int = 0,
    @SerializedName("deptId")
    @Expose
    var deptId: Int = 0,

    @SerializedName("departmentName")
    @Expose
    var degreeName: String = "",
    @SerializedName("isActive")
    @Expose
    var isActive: String = "",
    @SerializedName("coursesList")
    @Expose
    var deptList: ArrayList<CourseModel> = arrayListOf()
) : Serializable

data class CourseModel(
    @SerializedName("classroomId")
    @Expose
    var classroomId: Int? = 0,
    @SerializedName("classroomName")
    @Expose
    var classroomName: String? = "",
    @SerializedName("displayOrder")
    @Expose
    var displayOrder: Int? = 0,
    var degreeId: Int? = 0,
    @SerializedName("degreeName")
    @Expose
    var degreeName: String? = "",
    @SerializedName("deptId")
    @Expose
    var deptId: Int? = 0,
    @SerializedName("deptName")
    @Expose
    var deptName: String? = "",
    @SerializedName("isActive")
    @Expose
    var isActive: String = "",
    @SerializedName("defaultCourseSetupId")
    @Expose
    var defaultCourseSetupId: String = "",
    @SerializedName("classroomsList")
    @Expose
    var classroomsList: ArrayList<ClassroomsListModel> = arrayListOf()
) : Serializable

data class ClassroomsListModel(
    @SerializedName("degreeId")
    @Expose
    var degreeId: Int = 0,

    @SerializedName("degreeName")
    @Expose
    var degreeName: String = "",
    @SerializedName("classroomId")
    @Expose
    var classroomId: Int? = 0,
    @SerializedName("classroomName")
    @Expose
    var classroomName: String? = "",
    @SerializedName("deptId")
    @Expose
    var deptId: Int? = 0,
    @SerializedName("deptName")
    @Expose
    var deptName: String? = "",
    @SerializedName("courseId")
    @Expose
    var courseId: Int = 0,

    @SerializedName("courseName")
    @Expose
    var courseName: String = "",

    @SerializedName("displayOrder")
    @Expose
    var displayOrder: String = "",
    @SerializedName("isActive")
    @Expose
    var isActive: String = "",
    @SerializedName("defaultCourseSetupId")
    @Expose
    var defaultCourseSetupId: String = "",
    @SerializedName("className")
    @Expose
    var className: String? = "",
    @SerializedName("isSelected")
    @Expose
    var isSelected: Boolean = false
)