package com.pronted.presentation.feepayments

import androidx.lifecycle.ViewModel
import com.pronted.domain.feepayments.FeePaymentUseCase
import com.pronted.dto.employee.*
import com.pronted.dto.feepayments.OrderCreationAction
import com.pronted.dto.feepayments.PaymentHistoryAction
import com.pronted.dto.feepayments.PostPaymentAction
import com.pronted.dto.feepayments.StudentFeeDetailsAction
import com.pronted.dto.feepayments.data.ManualFeePayment
import com.pronted.dto.feepayments.data.PaymentGatewayOrderRequest
import com.pronted.dto.feepayments.data.RazorPayPaymentResponse
import kotlinx.coroutines.flow.Flow

class FeePaymentsViewModel(private val feePaymentsUseCase: FeePaymentUseCase) : ViewModel() {

    /**
     * Fetch student fee details
     *
     */
    fun fetchStudentFeeDetails(studentProfileId: Int):
            Flow<StudentFeeDetailsAction> =
        feePaymentsUseCase.fetchStudentFeeDetails(studentProfileId)

    /**
     * Fetch student fee details
     *
     */
    fun fetchFeePaymentHistory(studentProfileId: Int):
            Flow<PaymentHistoryAction> = feePaymentsUseCase.fetchFeePaymentHistory(studentProfileId)

    /**
     * create payment gateway order
     *
     */
    fun createRazorpayOrder(paymentReq: PaymentGatewayOrderRequest):
            Flow<OrderCreationAction> = feePaymentsUseCase.createRazorpayOrder(paymentReq)

    /**
     * process payment order post payment
     *
     */
    fun processOrderPostPayment(paymentResponse: RazorPayPaymentResponse):
            Flow<PostPaymentAction> = feePaymentsUseCase.processOrderPostPayment(paymentResponse)

    /**
     * Fetch Employees
     */
    fun fetchBanks():
            Flow<BanksAction> = feePaymentsUseCase.fetchBanks()

    /**
     * Fetch Employees
     */
    fun fetchFeeReceiptTemplates():
            Flow<FeeReceiptTempAction> = feePaymentsUseCase.fetchFeeReceiptTemplates()

    /**
     * add fee payment
     */
    fun addManualFeePayment(feePayment: ManualFeePayment):
            Flow<ManualFeePaymentAction> = feePaymentsUseCase.addManualFeePayment(feePayment)

    /**
     * Fetch invoice url
     */
    fun fetchInvoiceUrl(invoiceId:Int):
            Flow<InvoiceAction> = feePaymentsUseCase.fetchInvoice(invoiceId)
}