package com.pronted.dto.feepayments.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FeesDetailModel : Serializable {

    @SerializedName("academicYear")
    @Expose
    var academicYear = ""

    @SerializedName("feeHeadList")
    @Expose
    var feeHeadData = ArrayList<FeeHeadData>()

    var studentId = 0
    var processingAmount = 0F
}