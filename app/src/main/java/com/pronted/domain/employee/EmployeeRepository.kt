package com.pronted.domain.employee

import com.pronted.dto.ApiResponse
import com.pronted.dto.employee.*
import kotlinx.coroutines.flow.Flow

interface EmployeeRepository {

    /**
     * Fetch employees
     *
     */
    fun fetchEmployees():
            Flow<ApiResponse<ArrayList<Employee>>>

    /**
     * Fetch employee departments
     *
     */
    fun fetchPaymentMethods(fieldName:String):
            Flow<ApiResponse<ArrayList<EmployeeDepartment>>>

    /**
     * Fetch employees count
     *
     */
    fun fetchEmployeeCount(headers: Map<String, String>?):
            Flow<ApiResponse<EmployeeCount>>

    /**
     * Update employee profile
     */
    fun updateEmployeeProfile(editedProfileData: Employee):Flow<ApiResponse<Employee>>
}