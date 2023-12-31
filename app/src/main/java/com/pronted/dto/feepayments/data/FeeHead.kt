package com.pronted.dto.feepayments.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FeeHead : Serializable {

    @SerializedName("feeHeadSetupId")
    @Expose
    var feeHeadSetupId = 0
    @SerializedName("headName")
    @Expose
    var headName = ""
    @SerializedName("transportFlag")
    @Expose
    var transportFlag = ""

}