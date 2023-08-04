package com.pronted.dto.student

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ClassDropDownModel(

    @SerializedName("courseId")
    @Expose
    var courseId: Int = 0,

    @SerializedName("courseName")
    @Expose
    var courseName: String = "",

    @SerializedName("displayOrder")
    @Expose
    var displayOrder: String = "",

    @SerializedName("classroomsList")
    @Expose
    var sections: ArrayList<SectionItem>
) : Serializable

data class SectionItem(
    @SerializedName("classroomId")
    @Expose
    var classroomId: Int = 0,

    @SerializedName("degreeName")
    @Expose
    var degreeName: String = "",

    @SerializedName("deptName")
    @Expose
    var deptName: String = "",

    @SerializedName("courseName")
    @Expose
    var courseName: String = "",

    @SerializedName("classroomName")
    @Expose
    var classroomName: String = "",

    @SerializedName("displayOrder")
    @Expose
    var displayOrder: Int = 0,

    @SerializedName("isSelected")
    @Expose
    var isSelected: Boolean = false
):Serializable
