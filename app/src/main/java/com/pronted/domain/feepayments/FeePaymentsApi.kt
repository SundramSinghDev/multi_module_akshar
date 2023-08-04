package com.pronted.domain.feepayments


import com.pronted.dto.employee.FeeTemp
import com.pronted.dto.employee.Invoice
import com.pronted.dto.feepayments.BankAccount
import com.pronted.dto.feepayments.PaymentHistoryModel
import com.pronted.dto.feepayments.data.*
import retrofit2.Response
import retrofit2.http.*

interface FeePaymentsApi {

    @GET("finance/fees")
    suspend fun fetchFeeDetails(
        @Query("studentProfileId") studentProfileId: Int
    ): Response<ArrayList<FeesDetailModel>>

    @GET("finance/fees/payments")
    suspend fun fetchPaymentHistory(
        @Query("studentProfileId") studentProfileId: Int
    ): Response<ArrayList<PaymentHistoryModel>>

    @POST("pg/razorpay/create-order")
    suspend fun createPaymentOrder(
        @Body model: PaymentGatewayOrderRequest
    ): Response<RazorPayPaymentRequest>

    @PUT("pg/razorpay/process-order")
    suspend fun processOrderPostPayment(
        @Query("orderId") orderId: String,
        @Body model: RazorPayPaymentResponse
    ): Response<ProcessOrderResponse>

    @GET("finance/bankAccounts/school")
    suspend fun fetchBankAccounts(): Response<ArrayList<BankAccount>>

    @GET("finance/feeReceiptTemplates")
    suspend fun fetchFeeReceiptTemp(): Response<ArrayList<FeeTemp>>

    @POST("finance/fees/payments")
    suspend fun addFeePayment(
        @Body paymentRequest: ManualFeePayment
    ): Response<PaymentInvoice>

    @GET("finance/fees/receipt")
    suspend fun fetchInVoice(
        @Query("invoiceNumber") invoiceNumber: Int
    ): Response<Invoice>
}