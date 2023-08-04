package com.pronted.domain.employee

import com.pronted.dto.employee.Employee
import com.pronted.dto.employee.EmployeeCount
import com.pronted.dto.employee.EmployeeDepartment
import retrofit2.Response
import retrofit2.http.*

interface EmployeeApi {
    @GET("hr/employees/all")
    suspend fun fetchEmployees(
    ): Response<ArrayList<Employee>>

    @GET("lookup")
    suspend fun fetchPaymentMethod(
        @Query("fieldName") fieldName: String
    ): Response<ArrayList<EmployeeDepartment>>

    @GET("hr/employees/count")
    suspend fun fetchEmployeeCount(
        @HeaderMap headers: Map<String, String>?
    ): Response<EmployeeCount>

    @PUT("hr/employees/mobile")
    suspend fun updateEmployeeProfile(
        @Body jsonObject: Employee
    ): Response<Employee>
}