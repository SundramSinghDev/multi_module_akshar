package com.pronted.dto.feepayments.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PaymentGatewayOrderRequest(
    @SerializedName("gateway")
    @Expose
    var gateway: String = "",
    @SerializedName("custId")
    @Expose
    var customerId: String = "",
    @SerializedName("custName")
    @Expose
    var customerName: String = "",
    @SerializedName("custEmail")
    @Expose
    var customerEmail: String = "",
    @SerializedName("custMobile")
    @Expose
    var customerMobile: String = "",
    @SerializedName("product")
    @Expose
    var product: String = "",
    @SerializedName("amount")
    @Expose
    var amount: Int = 0,
    @SerializedName("paymentSplits")
    @Expose
    var paymentSplits: ArrayList<PaymentSplits> = arrayListOf()
) : Serializable

class PaymentSplits(
    @SerializedName("name")
    @Expose
    var name: String = "",
    @SerializedName("description")
    @Expose
    var description: String = "",
    @SerializedName("feeHeadSetupId")
    @Expose
    var feeHeadSetupId: Long = 0L,
    @SerializedName("feeHead")
    @Expose
    var feeHead: String = "",
    @SerializedName("feeTerm")
    @Expose
    var feeTerm: String = "",
    @SerializedName("studentFeeId")
    @Expose
    var studentFeeId: Long = 0L,
    @SerializedName("amount")
    @Expose
    var amount: Int = 0
) : Serializable


class RazorPayPaymentRequest(
    @SerializedName("orderId")
    @Expose
    var orderId: String = "",
    @SerializedName("schoolCd")
    @Expose
    var schoolCd: String = "",
    @SerializedName("amount")
    @Expose
    var amount: Float = 0F,
    @SerializedName("amountPaid")
    @Expose
    var amountPaid: Float = 0F,
    @SerializedName("amountDue")
    @Expose
    var amountDue: Float = 0F,
    @SerializedName("attempts")
    @Expose
    var attempts: Int = 0,
    @SerializedName("custId")
    @Expose
    var customerId: Int = 0,
    @SerializedName("product")
    @Expose
    var product: String = "",
    @SerializedName("custMobileNumber")
    @Expose
    var customerMobileNumber: String = "",
    @SerializedName("custEmail")
    @Expose
    var customerEmail: String = "",
    @SerializedName("transactionStatus")
    @Expose
    var transactionStatus: String = "",
    @SerializedName("paymentStatus")
    @Expose
    var paymentStatus: String = "",
    @SerializedName("currency")
    @Expose
    var currency: String = "",
    @SerializedName("createdAt")
    @Expose
    var createdAt: String = "",
    var paymentId: String = "",
    var signature: String = "",
    var errorCode: Int = 0,
    var errorResponse: String? = ""
) : Serializable


data class RazorPayPaymentResponse(
    var orderId: String = "",
    var paymentId: String = "",
    var signature: String? = "",
    var httpStatusCode: Int = 0,
    var error: String? = null
)


data class ProcessOrderResponse(
    @SerializedName("invoice")
    var invoiceUrl: String = "",
)

data class PaymentInvoice(
    @SerializedName("invoice")
    @Expose
    var invoice: String = ""
) : Serializable