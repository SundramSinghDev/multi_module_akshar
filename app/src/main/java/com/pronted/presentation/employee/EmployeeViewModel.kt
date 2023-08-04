package com.pronted.presentation.employee

import androidx.lifecycle.ViewModel
import com.pronted.domain.employee.EmployeeUseCase
import com.pronted.dto.employee.*
import com.pronted.dto.student.StudentProfileResponse
import com.pronted.dto.student.UpdateStudentProfileAction
import com.pronted.utils.extentions.EMP_DEPARTMENT
import kotlinx.coroutines.flow.Flow

class EmployeeViewModel(private val employeeUseCase: EmployeeUseCase) : ViewModel() {
    /**
     * Fetch Employees
     */
    fun fetchEmployees():
            Flow<EmployeesAction> = employeeUseCase.fetchEmployees()
    /**
     * Fetch payment methods
     */
    fun fetchPaymentMethods(fieldName:String):
            Flow<EmployeeDepartmentAction> = employeeUseCase.fetchPaymentMethods(fieldName)

    /**
     * Fetch Employees count
     */
    fun fetchEmployeeCount(headers: Map<String, String>?):
            Flow<EmployeeCountAction> = employeeUseCase.fetchEmployeeCount(headers)

    /**
     * Fetch employee departments
     */
    fun fetchEmployeeDepartments(fieldName:String):
            Flow<EmployeeDepartmentAction> = employeeUseCase.fetchPaymentMethods(fieldName)

    fun updateEmployeeProfile(editedProfileData: Employee):
            Flow<UpdateEmployeeProfileAction> = employeeUseCase.updateEmployeeProfile(editedProfileData)

}