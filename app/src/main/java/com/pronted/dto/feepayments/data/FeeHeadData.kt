package com.pronted.dto.feepayments.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FeeHeadData : Serializable{

    @SerializedName("isSelected")
    @Expose
    var isSelected = false
    @SerializedName("feeHead")
    @Expose
    var feeHead = ""
    @SerializedName("feeItemList")
    @Expose
    var feeTerms = ArrayList<FeeTermData>()


}