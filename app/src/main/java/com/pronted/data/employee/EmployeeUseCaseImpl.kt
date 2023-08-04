package com.pronted.data.employee

import com.pronted.domain.employee.EmployeeRepository
import com.pronted.domain.employee.EmployeeUseCase
import com.pronted.dto.ApiResponse
import com.pronted.dto.ErrorResponse
import com.pronted.dto.employee.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class EmployeeUseCaseImpl (private val employeeRepository: EmployeeRepository) :
    EmployeeUseCase {
    override fun fetchEmployees(): Flow<EmployeesAction>  = employeeRepository.fetchEmployees().map {
        when (it) {
            is ApiResponse.Success -> {
                EmployeesAction.Success(it.data)
            }
            is ApiResponse.Error -> {
                EmployeesAction.Fail(it.errorResponse)
            }
            is ApiResponse.Exception -> {
                EmployeesAction.Fail(ErrorResponse())
            }
        }
    }

    override fun fetchPaymentMethods(fieldName: String): Flow<EmployeeDepartmentAction> = employeeRepository.fetchPaymentMethods(fieldName).map {
        when (it) {
            is ApiResponse.Success -> {
                EmployeeDepartmentAction.Success(it.data)
            }
            is ApiResponse.Error -> {
                EmployeeDepartmentAction.Fail(it.errorResponse)
            }
            is ApiResponse.Exception -> {
                EmployeeDepartmentAction.Fail(ErrorResponse())
            }
        }
    }

    override fun fetchEmployeeCount(headers: Map<String, String>?): Flow<EmployeeCountAction> = employeeRepository.fetchEmployeeCount(headers).map {
        when (it) {
            is ApiResponse.Success -> {
                EmployeeCountAction.Success(it.data)
            }
            is ApiResponse.Error -> {
                EmployeeCountAction.Fail(it.errorResponse)
            }
            is ApiResponse.Exception -> {
                EmployeeCountAction.Fail(ErrorResponse())
            }
        }
    }

    override fun updateEmployeeProfile(editedProfileData: Employee): Flow<UpdateEmployeeProfileAction> = employeeRepository.updateEmployeeProfile(editedProfileData).map {
        when (it) {
            is ApiResponse.Success -> {
                UpdateEmployeeProfileAction.Success(it.data)
            }
            is ApiResponse.Error -> {
                UpdateEmployeeProfileAction.Fail(it.errorResponse)
            }
            is ApiResponse.Exception -> {
                UpdateEmployeeProfileAction.Fail(ErrorResponse())
            }
        }
    }
}