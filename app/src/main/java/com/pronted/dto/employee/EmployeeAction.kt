package com.pronted.dto.employee

import com.pronted.dto.ErrorResponse
import com.pronted.dto.feepayments.BankAccount
import com.pronted.dto.feepayments.data.PaymentInvoice
import com.pronted.dto.student.StudentProfileResponse

sealed class EmployeesAction {
    class Success(val response: ArrayList<Employee>) : EmployeesAction()

    class Fail(val errorResponse: ErrorResponse) : EmployeesAction()
}

sealed class EmployeeCountAction {
    class Success(val response: EmployeeCount) : EmployeeCountAction()

    class Fail(val errorResponse: ErrorResponse) : EmployeeCountAction()
}
sealed class EmployeeDepartmentAction {
    class Success(val response: ArrayList<EmployeeDepartment>) : EmployeeDepartmentAction()

    class Fail(val errorResponse: ErrorResponse) : EmployeeDepartmentAction()
}

sealed class BanksAction {
    class Success(val response: ArrayList<BankAccount>) : BanksAction()

    class Fail(val errorResponse: ErrorResponse) : BanksAction()
}

sealed class FeeReceiptTempAction {
    class Success(val response: ArrayList<FeeTemp>) : FeeReceiptTempAction()

    class Fail(val errorResponse: ErrorResponse) : FeeReceiptTempAction()
}

sealed class ManualFeePaymentAction {
    class Success(val response: PaymentInvoice) : ManualFeePaymentAction()

    class Fail(val errorResponse: ErrorResponse) : ManualFeePaymentAction()
}

sealed class UpdateEmployeeProfileAction {
    class Success(val profileResponse: Employee) : UpdateEmployeeProfileAction()

    class Fail(val errorResponse: ErrorResponse) : UpdateEmployeeProfileAction()
}


sealed class InvoiceAction {
    class Success(val invoice: Invoice) : InvoiceAction()

    class Fail(val errorResponse: ErrorResponse) : InvoiceAction()
}