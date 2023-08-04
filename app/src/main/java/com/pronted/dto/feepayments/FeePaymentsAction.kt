package com.pronted.dto.feepayments

import com.pronted.dto.ErrorResponse
import com.pronted.dto.feepayments.data.FeesDetailModel
import com.pronted.dto.feepayments.data.ProcessOrderResponse
import com.pronted.dto.feepayments.data.RazorPayPaymentRequest

sealed class StudentFeeDetailsAction {
    class Success(val feeList: ArrayList<FeesDetailModel>) : StudentFeeDetailsAction()

    class Fail(val errorResponse: ErrorResponse) : StudentFeeDetailsAction()
}

sealed class PaymentHistoryAction {
    class Success(val paymentsHistory: ArrayList<PaymentHistoryModel>) : PaymentHistoryAction()

    class Fail(val errorResponse: ErrorResponse) : PaymentHistoryAction()
}

sealed class OrderCreationAction {
    class Success(val paymentRequest: RazorPayPaymentRequest) : OrderCreationAction()

    class Fail(val errorResponse: ErrorResponse) : OrderCreationAction()
}


sealed class PostPaymentAction {
    class Success(val orderResponse: ProcessOrderResponse) : PostPaymentAction()

    class Fail(val errorResponse: ErrorResponse) : PostPaymentAction()
}