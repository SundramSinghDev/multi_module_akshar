package com.pronted.dto.exam

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ExamDropDownModel(
    @SerializedName("examId")
    @Expose
    var examId: Int = 0,

    @SerializedName("examName")
    @Expose
    var examName: String = "",

    @SerializedName("examType")
    @Expose
    var examType: String = "",

    @SerializedName("scheduleStatus")
    @Expose
    var scheduleStatus: String = "",

    @SerializedName("marksStatus")
    @Expose
    var marksStatus: String = "",

    @SerializedName("tests")
    @Expose
    var testsList: List<TestListModel>?
) : Serializable

data class TestListModel(
    @SerializedName("testId")
    @Expose
    var testId: Int = 0,

    @SerializedName("examId")
    @Expose
    var examId: Int = 0,

    @SerializedName("displayOrder")
    @Expose
    var displayOrder: Int = 0,

    @SerializedName("testName")
    @Expose
    var testName: String = ""
) : Serializable
