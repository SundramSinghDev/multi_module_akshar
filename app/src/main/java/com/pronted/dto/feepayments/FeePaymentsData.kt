package com.pronted.dto.feepayments

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pronted.dto.student.StudentProfileResponse
import java.io.Serializable

class PaymentHistoryModel : Serializable{

     @SerializedName("invoiceNumber")
     @Expose
     var invoiceNumber = 0
     @SerializedName("controlNumber")
     @Expose
     var controlNumber = 0
     @SerializedName("paymentDate")
     @Expose
     var paymentDate = ""
     @SerializedName("receivedBy")
     @Expose
     var receivedBy = ""
     @SerializedName("paymentMethod")
     @Expose
     var paymentMethod = ""
     @SerializedName("paymentDescription")
     @Expose
     var paymentDescription = ""
     @SerializedName("checkNbr")
     @Expose
     var checkNbr = ""
     @SerializedName("paymentReferenceNbr")
     @Expose
     var paymentReferenceNbr = ""
     @SerializedName("transactionStatus")
     @Expose
     var transactionStatus = ""
     @SerializedName("clearanceStatus")
     @Expose
     var clearanceStatus = ""
     @SerializedName("paymentClearedDate")
     @Expose
     var paymentClearedDate = ""
     @SerializedName("paymentSource")
     @Expose
     var paymentSource = ""
     @SerializedName("bankAccount")
     @Expose
     var bankAccount = BankAccount()
     @SerializedName("student")
     @Expose
     var student = StudentProfileResponse()
     @SerializedName("paymentsList")
     @Expose
     var paymentsList = ArrayList<Payment>()
    override fun toString(): String {
        return "PaymentHistoryModel(invoiceNumber=$invoiceNumber, controlNumber=$controlNumber, paymentDate='$paymentDate', receivedBy='$receivedBy', paymentMethod='$paymentMethod', paymentDescription='$paymentDescription', checkNbr='$checkNbr', paymentReferenceNbr='$paymentReferenceNbr', transactionStatus='$transactionStatus', clearanceStatus='$clearanceStatus', paymentClearedDate='$paymentClearedDate', paymentSource='$paymentSource', bankAccount=$bankAccount, student=$student, paymentsList=$paymentsList)"
    }


}


 class BankAccount(
    @SerializedName("bankAccountId")
    @Expose
    var bankAccountId: Int = 0,
    @SerializedName("accountNickname")
    @Expose
    var accountNickname: String = "",
    @SerializedName("bankName")
    @Expose
    var bankName: String = "",
    @SerializedName("accountNumber")
    @Expose
    var accountNumber: String = "",
    @SerializedName("accountStartDate")
    @Expose
    var accountStartDate: String = "",
    @SerializedName("ifscCode")
    @Expose
    var ifscCode: String = "",
) : Serializable

class Payment(
    @SerializedName("studentFeeId")
    @Expose
    var studentFeeId: Int = 0,
    @SerializedName("feeHead")
    @Expose
    var feeHead: String = "",
    @SerializedName("feeTerm")
    @Expose
    var feeTerm: String = "",
    @SerializedName("paymentAmount")
    @Expose
    var paymentAmount: Float = 0F,
    @SerializedName("controlNumber")
    @Expose
    var controlNumber: Int = 0
) : Serializable
