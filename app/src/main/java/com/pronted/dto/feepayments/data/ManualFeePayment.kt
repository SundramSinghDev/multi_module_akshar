package com.pronted.dto.feepayments.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ManualFeePayment(
    @SerializedName("invoiceNumber")
    @Expose
    var invoiceNumber: String? = null,
    @SerializedName("controlNumber")
    @Expose
    var controlNumber: String? = null,
    @SerializedName("paymentDate")
    @Expose
    var paymentDate: String = "",
    @SerializedName("paymentMethod")
    @Expose
    var paymentMethod: String = "",
    @SerializedName("paymentDescription")
    @Expose
    var paymentDescription: String = "",
    @SerializedName("checkNbr")
    @Expose
    var checkNbr: String? = null,
    @SerializedName("paymentReferenceNbr")
    @Expose
    var paymentReferenceNbr: String? = null,
    @SerializedName("transactionStatus")
    @Expose
    var transactionStatus: String? = null,
    @SerializedName("clearanceStatus")
    @Expose
    var clearanceStatus: String? = null,
    @SerializedName("paymentClearedDate")
    @Expose
    var paymentClearedDate: String? = null,
    @SerializedName("paymentSource")
    @Expose
    var paymentSource: String? = null,

    @SerializedName("feeReceiptTemplateName")
    @Expose
    var templateName: String? = null,

    @SerializedName("bankAccount")
    @Expose
    var bankAccount: BankAccountRequest?,
    @SerializedName("student")
    @Expose
    var student: StudentRequest,
    @SerializedName("paymentsList")
    @Expose
    var paymentsList: ArrayList<PaymentListRequest>,
) : Serializable

data class BankAccountRequest(

    @SerializedName("bankAccountId")
    @Expose
    var bankAccountId: Int = 0
) : Serializable

data class StudentRequest(
    @SerializedName("studentProfileId")
    @Expose
    var studentProfileId: Int = 0
) : Serializable

data class PaymentListRequest(

    @SerializedName("studentFeeId")
    @Expose
    var studentFeeId: Int = 0,
    @SerializedName("paymentAmount")
    @Expose
    var paymentAmount: Float = 0F,
    @SerializedName("feeHead")
    @Expose
    var feeHead: String = "",
    @SerializedName("feeTerm")
    @Expose
    var feeTerm: String = ""
) : Serializable