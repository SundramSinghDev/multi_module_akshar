package com.pronted.dto.feepayments.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FeeTermData : Serializable {

    @SerializedName("isSelected")
    @Expose
    var isSelected = false
    @SerializedName("studentFeeId")
    @Expose
    var studentFeeId = 0
    @SerializedName("academicYear")
    @Expose
    var academicYear = ""
    @SerializedName("classroomId")
    @Expose
    var classroomId = 0
    @SerializedName("feeHead")
    @Expose
    var feeHead = FeeHead()
    @SerializedName("feeTerm")
    @Expose
    var feeTerm = FeeTerm()
    @SerializedName("refFeeSetupId")
    @Expose
    var refFeeSetupId = 0
    @SerializedName("feeAmount")
    @Expose
    var feeAmount = 0.0
    @SerializedName("concessionAmount")
    @Expose
    var concessionAmount = 0.0
    @SerializedName("concessionCode")
    @Expose
    var concessionCode = ""
    @SerializedName("finalAmount")
    @Expose
    var finalAmount = 0F
    @SerializedName("dueAmount")
    @Expose
    var dueAmount = 0F
    @SerializedName("overdueAmount")
    @Expose
    var overdueAmount = 0F
    @SerializedName("dueDate")
    @Expose
    var dueDate = ""
    @SerializedName("priority")
    @Expose
    var priority = 0
    @SerializedName("paidAmount")
    @Expose
    var paidAmount = 0F
    override fun toString(): String {
        return "FeeTermData(isSelected=$isSelected, studentFeeId=$studentFeeId, academicYear='$academicYear', classroomId=$classroomId, feeHead=$feeHead, feeTerm=$feeTerm, refFeeSetupId=$refFeeSetupId, feeAmount=$feeAmount, concessionAmount=$concessionAmount, concessionCode='$concessionCode', finalAmount=$finalAmount, dueAmount=$dueAmount, overdueAmount=$overdueAmount, dueDate='$dueDate', priority=$priority, paidAmount=$paidAmount)"
    }


}