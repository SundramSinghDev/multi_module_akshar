package com.pronted.dto.exam

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ExamScheduleModel(
    @SerializedName("examId")
    val examId: Int = 0,

    @SerializedName("testId")
    val testId: Int = 0,

    @SerializedName("examinationName")
    val examinationName: String = "",

    @SerializedName("testName")
    val testName: String = "",

    @SerializedName("schedule")
    var scheduleModel: List<ScheduleModel>? = null
) : Serializable

data class ScheduleModel(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("subjectId")
    val subjectId: Int = 0,

    @SerializedName("subjectName")
    val subjectName: String = "",

    @SerializedName("date")
    var date: String = "",

    @SerializedName("startTime")
    var startTime: String = "",

    @SerializedName("endTime")
    val endTime: String = "",

    @SerializedName("duration")
    var duration: Int = 0
) : Serializable