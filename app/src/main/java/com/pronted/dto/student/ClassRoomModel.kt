package com.pronted.dto.student

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class ClassRoomModel(
    @SerializedName("classroomId")
    @Expose
    var classroomId: Int? = 0,
    @SerializedName("classroomName")
    @Expose
    var classroomName: String? = "",
    @SerializedName("displayOrder")
    @Expose
    var displayOrder: Int? = 0,
    @SerializedName("courseName")
    @Expose
    var courseName: String? = "",
    @SerializedName("degreeId")
    @Expose
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
    @SerializedName("courseId")
    @Expose
    var courseId: Int? = 0,
    @SerializedName("isActive")
    @Expose
    var isActive:String? ="",
    @SerializedName("academicYear")
    @Expose
    var academicYear: String? = "",
    @SerializedName("defaultClassroomSetupId")
    @Expose
    var defaultClassroomSetupId: String? = "",
) : Serializable