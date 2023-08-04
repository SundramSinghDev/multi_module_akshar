package com.pronted.data.employee

import com.pronted.domain.employee.EmployeeApi
import com.pronted.domain.employee.EmployeeRepository
import com.pronted.dto.ApiResponse
import com.pronted.dto.employee.Employee
import com.pronted.dto.employee.EmployeeCount
import com.pronted.dto.employee.EmployeeDepartment
import com.pronted.utils.extentions.handleApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EmployeeRepositoryImpl(private val employeeApi: EmployeeApi) :
    EmployeeRepository {
    override fun fetchEmployees(): Flow<ApiResponse<ArrayList<Employee>>> = flow {
        emit(
            handleApi {
                employeeApi.fetchEmployees()
            }
        )
    }

    override fun fetchPaymentMethods(fieldName: String): Flow<ApiResponse<ArrayList<EmployeeDepartment>>> =
        flow {
            emit(handleApi {
                employeeApi.fetchPaymentMethod(fieldName)
            })
        }

    override fun fetchEmployeeCount(headers: Map<String, String>?): Flow<ApiResponse<EmployeeCount>> = flow {
        emit(handleApi {
            employeeApi.fetchEmployeeCount(headers)
        })
    }

    override fun updateEmployeeProfile(editedProfileData: Employee): Flow<ApiResponse<Employee>> = flow {
        emit(handleApi {
            employeeApi.updateEmployeeProfile(editedProfileData)
        })
    }
}