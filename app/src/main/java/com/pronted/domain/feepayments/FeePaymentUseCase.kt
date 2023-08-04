package com.pronted.domain.feepayments

import com.pronted.dto.employee.BanksAction
import com.pronted.dto.employee.FeeReceiptTempAction
import com.pronted.dto.employee.InvoiceAction
import com.pronted.dto.employee.ManualFeePaymentAction
import com.pronted.dto.feepayments.OrderCreationAction
import com.pronted.dto.feepayments.PaymentHistoryAction
import com.pronted.dto.feepayments.PostPaymentAction
import com.pronted.dto.feepayments.StudentFeeDetailsAction
import com.pronted.dto.feepayments.data.ManualFeePayment
import com.pronted.dto.feepayments.data.PaymentGatewayOrderRequest
import com.pronted.dto.feepayments.data.RazorPayPaymentResponse
import kotlinx.coroutines.flow.Flow

/**
 * Image access
 */

interface FeePaymentUseCase {

    /**
     * Fetch student fee details
     */
    fun fetchStudentFeeDetails(profileId: Int): Flow<StudentFeeDetailsAction>

    /**
     * Fetch student fee payment history
     */
    fun fetchFeePaymentHistory(profileId: Int): Flow<PaymentHistoryAction>

    /**
     * create payment gateway order
     */
    fun createRazorpayOrder(paymentReq: PaymentGatewayOrderRequest): Flow<OrderCreationAction>

    /**
     * process order post payment
     */
    fun processOrderPostPayment(paymentResponse: RazorPayPaymentResponse): Flow<PostPaymentAction>

    /**
     * Fetch banks
     *
     */
    fun fetchBanks():
            Flow<BanksAction>

    /**
     * Fetch fee receipt templates
     *
     */
    fun fetchFeeReceiptTemplates():
            Flow<FeeReceiptTempAction>

    /**
     * Add manual fee payments
     *
     */
    fun addManualFeePayment(feePayment: ManualFeePayment): Flow<ManualFeePaymentAction>

    /**
     * fetch invoice url
     *
     */
    fun fetchInvoice(invoiceId:Int): Flow<InvoiceAction>
}