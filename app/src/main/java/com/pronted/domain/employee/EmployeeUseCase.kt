package com.pronted.domain.employee

import com.pronted.dto.employee.*
import com.pronted.dto.student.StudentProfileResponse
import com.pronted.dto.student.UpdateStudentProfileAction
import kotlinx.coroutines.flow.Flow
import retrofit2.http.HeaderMap

/**
 * Image access
 */

interface EmployeeUseCase {
    /**
     * Fetch employees
     *
     */
    fun fetchEmployees():
            Flow<EmployeesAction>
 /**
     * Fetch employee departments
     *
     */
    fun fetchPaymentMethods(fieldName:String):
            Flow<EmployeeDepartmentAction>

    /**
     * Fetch employees count
     *
     */
    fun fetchEmployeeCount(headers: Map<String, String>?):
            Flow<EmployeeCountAction>

    /**
     * Update employee profile
     */
    fun updateEmployeeProfile(editedProfileData: Employee):Flow<UpdateEmployeeProfileAction>
}