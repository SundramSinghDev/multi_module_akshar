package com.pronted.domain.timeline

import com.pronted.dto.ApiResponse
import com.pronted.dto.feepayments.data.FeesDetailModel
import com.pronted.dto.student.StudentProfileResponse
import com.pronted.dto.timeline.*
import kotlinx.coroutines.flow.Flow

interface TimelineRepository {

    /**
     * Fetch Employee timetable
     *
     */
    fun fetchTimeTable(employeeId:Int, date:String, isEmployee:Boolean, headers: Map<String, String>?):
            Flow<ApiResponse<ArrayList<TimetableData>>>

    /**
     * Fetch birthdays
     *
     */
    fun fetchBirthdays(fromDate:String, toDate:String, size:Int, page:Int, forEmployee:Boolean):
            Flow<ApiResponse<BirthdaysData>>

    /**
     * Fetch finance summary
     *
     */
    fun fetchFinanceSummary(fromDate:String, toDate:String):
            Flow<ApiResponse<FinanceModel>>

    /**
     * Fetch fee details by student
     *
     */
    fun fetchFeeDetails(profileId:Int):
            Flow<ApiResponse<ArrayList<FeesDetailModel>>>

    /**
     * Fetch collection summary
     *
     */
    fun fetchCollectionSummary(groupBy:String, fromDate:String, toDate:String):
            Flow<ApiResponse<ArrayList<FeePayment>>>

    /**
     * Fetch expenses summary
     *
     */
    fun fetchExpensesSummary(groupBy:String, fromDate:String, toDate:String):
            Flow<ApiResponse<ArrayList<FeePayment>>>

}