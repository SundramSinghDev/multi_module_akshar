package com.pronted.data.feepayments

import com.pronted.domain.feepayments.FeePaymentUseCase
import com.pronted.domain.feepayments.FeePaymentsRepository
import com.pronted.dto.ApiResponse
import com.pronted.dto.ErrorResponse
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
import kotlinx.coroutines.flow.map

class FeePaymentsUseCaseImpl(private val feePaymentsRepository: FeePaymentsRepository) :
    FeePaymentUseCase {
    override fun fetchStudentFeeDetails(profileId: Int): Flow<StudentFeeDetailsAction> =
        feePaymentsRepository.fetchStudentFeeDetails(profileId).map {
            when (it) {
                is ApiResponse.Success -> {
                    StudentFeeDetailsAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    StudentFeeDetailsAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    StudentFeeDetailsAction.Fail(ErrorResponse())
                }
            }
        }

    override fun fetchFeePaymentHistory(profileId: Int): Flow<PaymentHistoryAction> =
        feePaymentsRepository.fetchFeePaymentHistory(profileId).map {
            when (it) {
                is ApiResponse.Success -> {
                    PaymentHistoryAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    PaymentHistoryAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    PaymentHistoryAction.Fail(ErrorResponse())
                }
            }
        }

    override fun createRazorpayOrder(paymentReq: PaymentGatewayOrderRequest): Flow<OrderCreationAction> =
        feePaymentsRepository.createRazorpayOrder(paymentReq).map {
            when (it) {
                is ApiResponse.Success -> {
                    OrderCreationAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    OrderCreationAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    OrderCreationAction.Fail(ErrorResponse())
                }
            }
        }

    override fun processOrderPostPayment(paymentResponse: RazorPayPaymentResponse): Flow<PostPaymentAction> =
        feePaymentsRepository.processOrderPostPayment(paymentResponse).map {
            when (it) {
                is ApiResponse.Success -> {
                    PostPaymentAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    PostPaymentAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    PostPaymentAction.Fail(ErrorResponse())
                }
            }
        }

    override fun fetchBanks(): Flow<BanksAction> = feePaymentsRepository.fetchBanks().map {
        when (it) {
            is ApiResponse.Success -> {
                BanksAction.Success(it.data)
            }
            is ApiResponse.Error -> {
                BanksAction.Fail(it.errorResponse)
            }
            is ApiResponse.Exception -> {
                BanksAction.Fail(ErrorResponse())
            }
        }
    }

    override fun fetchFeeReceiptTemplates(): Flow<FeeReceiptTempAction> =
        feePaymentsRepository.fetchFeeReceiptTemplates().map {
            when (it) {
                is ApiResponse.Success -> {
                    FeeReceiptTempAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    FeeReceiptTempAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    FeeReceiptTempAction.Fail(ErrorResponse())
                }
            }
        }

    override fun addManualFeePayment(feePayment: ManualFeePayment): Flow<ManualFeePaymentAction> = feePaymentsRepository.addManualFeePayment(feePayment).map {
        when (it) {
            is ApiResponse.Success -> {
                ManualFeePaymentAction.Success(it.data)
            }
            is ApiResponse.Error -> {
                ManualFeePaymentAction.Fail(it.errorResponse)
            }
            is ApiResponse.Exception -> {
                ManualFeePaymentAction.Fail(ErrorResponse())
            }
        }
    }

    override fun fetchInvoice(invoiceId: Int): Flow<InvoiceAction> = feePaymentsRepository.fetchInvoice(invoiceId).map {
        when (it) {
            is ApiResponse.Success -> {
                InvoiceAction.Success(it.data)
            }
            is ApiResponse.Error -> {
                InvoiceAction.Fail(it.errorResponse)
            }
            is ApiResponse.Exception -> {
                InvoiceAction.Fail(ErrorResponse())
            }
        }
    }
}