package com.pronted.data.feepayments

import com.pronted.domain.feepayments.FeePaymentsApi
import com.pronted.domain.feepayments.FeePaymentsRepository
import com.pronted.dto.ApiResponse
import com.pronted.dto.employee.FeeTemp
import com.pronted.dto.employee.Invoice
import com.pronted.dto.feepayments.BankAccount
import com.pronted.dto.feepayments.PaymentHistoryModel
import com.pronted.dto.feepayments.data.*
import com.pronted.utils.extentions.handleApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FeePaymentsRepositoryImpl(private val feePaymentsApi: FeePaymentsApi) :
    FeePaymentsRepository {
    override fun fetchStudentFeeDetails(profileId: Int): Flow<ApiResponse<ArrayList<FeesDetailModel>>> =
        flow {
            emit(handleApi {
                feePaymentsApi.fetchFeeDetails(profileId)
            })
        }

    override fun fetchFeePaymentHistory(profileId: Int): Flow<ApiResponse<ArrayList<PaymentHistoryModel>>> =
        flow {
            emit(handleApi {
                feePaymentsApi.fetchPaymentHistory(profileId)
            })
        }

    override fun createRazorpayOrder(paymentReq: PaymentGatewayOrderRequest): Flow<ApiResponse<RazorPayPaymentRequest>> =
        flow {
            emit(handleApi {
                feePaymentsApi.createPaymentOrder(paymentReq)
            })
        }

    override fun processOrderPostPayment(paymentResponse: RazorPayPaymentResponse): Flow<ApiResponse<ProcessOrderResponse>> =
        flow {
            emit(handleApi {
                feePaymentsApi.processOrderPostPayment(paymentResponse.orderId, paymentResponse)
            })
        }

    override fun fetchBanks(): Flow<ApiResponse<ArrayList<BankAccount>>> = flow {
        emit(handleApi {
            feePaymentsApi.fetchBankAccounts()
        })
    }

    override fun fetchFeeReceiptTemplates(): Flow<ApiResponse<ArrayList<FeeTemp>>> = flow {
        emit(handleApi {
            feePaymentsApi.fetchFeeReceiptTemp()
        })
    }

    override fun addManualFeePayment(feePayment: ManualFeePayment): Flow<ApiResponse<PaymentInvoice>> =
        flow {
            emit(handleApi {
                feePaymentsApi.addFeePayment(feePayment)
            })
        }

    override fun fetchInvoice(invoiceId: Int): Flow<ApiResponse<Invoice>> = flow {
        emit(
            handleApi {
                feePaymentsApi.fetchInVoice(invoiceId)
            }
        )
    }
}