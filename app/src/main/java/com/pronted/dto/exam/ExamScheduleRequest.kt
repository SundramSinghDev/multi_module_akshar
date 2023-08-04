package com.pronted.dto.exam

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ExamScheduleRequest(
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("examId")
    @Expose
    var examId: Int? = null,
    @SerializedName("testId")
    @Expose
    var testId: Int? = null,
    @SerializedName("classroomId")
    @Expose
    var classroomId: Int? = null,
    @SerializedName("duration")
    @Expose
    var duration: Int? = null,
    @SerializedName("startDate")
    @Expose
    var startDate: String? = null,
    @SerializedName("startTime")
    @Expose
    var startTime: String? = null
) : Serializable