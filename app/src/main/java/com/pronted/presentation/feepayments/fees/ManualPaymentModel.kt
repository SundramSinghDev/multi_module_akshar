package com.pronted.presentation.feepayments.fees

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import com.pronted.R
import com.pronted.dto.feepayments.BankAccount
import com.pronted.dto.feepayments.data.BankAccountRequest
import com.pronted.dto.feepayments.data.ManualFeePayment
import com.pronted.dto.feepayments.data.PaymentListRequest
import com.pronted.dto.feepayments.data.StudentRequest

class ManualPaymentModel : BaseObservable() {
    private val paymentDate = ObservableField<String>()
    private val receivedBy = ObservableField<String>()
    private val paymentMethod = ObservableField<String>()
    private val nameOfBank = ObservableField<String>()
    private val payRefNum = ObservableField<String>()
    private val remarks = ObservableField<String>()
    private val feeReceipt = ObservableField<String>()

    private var selectedBankModel = BankAccount()

    val paymentDateError = ObservableField<String>()
    val receivedByError = ObservableField<String>()
    val paymentMethodError = ObservableField<String>()
    val nameOfBankError = ObservableField<String>()
    val payRefNumError = ObservableField<String>()
    val remarksError = ObservableField<String>()
    val feeReceiptError = ObservableField<String>()


    fun getPaymentDate(): ObservableField<String> {
        paymentDateError.set(null)
        return paymentDate
    }

    fun setPaymentDate(date:String){
        paymentDate.set(date)
    }
    private fun paymentDate(): String {
        paymentDate.get()?.let {
            return it
        }
        return ""
    }

    fun getReceivedBy(): ObservableField<String> {
        receivedByError.set(null)
        return receivedBy
    }

    private fun receivedBy(): String {
        receivedBy.get()?.let {
            return it
        }
        return ""
    }

    fun getPaymentMethod(): ObservableField<String> {
        paymentMethodError.set(null)
        return paymentMethod
    }

    private fun paymentMethod(): String {
        paymentMethod.get()?.let {
            return it
        }
        return ""
    }

    fun setBankAccount(bank:BankAccount){
        selectedBankModel = bank
    }

    fun getNameOfBank(): ObservableField<String> {
        nameOfBankError.set(null)
        return nameOfBank
    }

    private fun nameOfBank(): String {
        nameOfBank.get()?.let {
            return it
        }
        return ""
    }

    fun getPayRefNum(): ObservableField<String> {
        payRefNumError.set(null)
        return payRefNum
    }

    private fun payRefNum(): String {
        payRefNum.get()?.let {
            return it
        }
        return ""
    }

    fun getRemarks(): ObservableField<String> {
        remarksError.set(null)
        return remarks
    }

    private fun remarks(): String {
        remarks.get()?.let {
            return it
        }
        return ""
    }

    fun getFeeReceipt(): ObservableField<String> {
        feeReceiptError.set(null)
        return feeReceipt
    }

    private fun feeReceipt(): String {
        feeReceipt.get()?.let {
            return it
        }
        return ""
    }

    fun isValid(context: Context?): Boolean {
        paymentDateError.set(null)
        receivedByError.set(null)
        paymentMethodError.set(null)
        nameOfBankError.set(null)
        payRefNumError.set(null)

        return if (paymentDate().isNotBlank() && receivedBy().isNotBlank() && paymentMethod().isNotBlank()
            && isValidPaymentMethod() && nameOfBank().isNotBlank() && feeReceipt().isNotBlank()
        ) {
            true
        } else {
            if (paymentDate().isBlank()) {
                paymentDateError.set(context?.getString(R.string.payment_date_is_required))
            }
            if (receivedBy().isBlank()) {
                receivedByError.set(context?.getString(R.string.add_receiver_details))
            }
            if (paymentDate().isBlank()) {
                paymentMethodError.set(context?.getString(R.string.payment_method_required))
            }
            if (nameOfBank().isBlank()) {
                nameOfBankError.set(context?.getString(R.string.select_bank_account))
            }
            if (payRefNum().isBlank()) {
                when (paymentMethod()) {
                    "Cash" ->
                        payRefNumError.set(context?.getString(R.string.enter_reference_number))
                    "Cheque" ->
                        payRefNumError.set(context?.getString(R.string.enter_cheque_number))
                    else ->
                        payRefNumError.set(context?.getString(R.string.enter_transaction_id))
                }
            }
            if (feeReceipt().isBlank()) {
                feeReceiptError.set(context?.getString(R.string.select_fee_receipt_template))
            }
            false
        }
    }

    private fun isValidPaymentMethod():Boolean{
        return if(paymentMethod() == "Cash")
            true
        else{
            payRefNum().isNotBlank()
        }
    }
    fun getPaymentRequest(studentId:Int, paymentList:ArrayList<PaymentListRequest>): ManualFeePayment {
        return ManualFeePayment(
            invoiceNumber = null,
            controlNumber = null,
            paymentDate = paymentDate(),
            paymentMethod = paymentMethod(),
            paymentDescription = remarks(),
            paymentReferenceNbr = if(paymentMethod() != "Cheque") payRefNum() else null,
            checkNbr = if(paymentMethod() == "Cheque") payRefNum() else null,
            templateName = feeReceipt(),
            bankAccount = BankAccountRequest(selectedBankModel.bankAccountId),
            student = StudentRequest(studentId),
            paymentsList = paymentList
        )
    }
}