package com.pronted.domain.feepayments

import com.pronted.dto.ApiResponse
import com.pronted.dto.employee.FeeTemp
import com.pronted.dto.employee.Invoice
import com.pronted.dto.employee.InvoiceAction
import com.pronted.dto.feepayments.BankAccount
import com.pronted.dto.feepayments.PaymentHistoryModel
import com.pronted.dto.feepayments.data.*
import kotlinx.coroutines.flow.Flow

interface FeePaymentsRepository {

    /**
     * Fetch Student fee details
     *
     */
    fun fetchStudentFeeDetails(profileId: Int):
            Flow<ApiResponse<ArrayList<FeesDetailModel>>>

    /**
     * Fetch student fee payment history
     */
    fun fetchFeePaymentHistory(profileId: Int): Flow<ApiResponse<ArrayList<PaymentHistoryModel>>>
    /**
     * create payment gateway order
     */
    fun createRazorpayOrder(paymentReq: PaymentGatewayOrderRequest): Flow<ApiResponse<RazorPayPaymentRequest>>

    /**
     * process order post payment
     */
    fun processOrderPostPayment(paymentResponse: RazorPayPaymentResponse): Flow<ApiResponse<ProcessOrderResponse>>

    /**
     * Fetch banks
     *
     */
    fun fetchBanks():
            Flow<ApiResponse<ArrayList<BankAccount>>>

    /**
     * Fetch fee receipt templates
     *
     */
    fun fetchFeeReceiptTemplates():
            Flow<ApiResponse<ArrayList<FeeTemp>>>

    /**
     * Add manual fee payments
     *
     */
    fun addManualFeePayment(feePayment: ManualFeePayment): Flow<ApiResponse<PaymentInvoice>>

    /**
     * fetch invoice url
     *
     */
    fun fetchInvoice(invoiceId:Int): Flow<ApiResponse<Invoice>>
}