package com.pronted.domain.timeline


import com.pronted.dto.feepayments.data.FeesDetailModel
import com.pronted.dto.noticeboard.NoticeBoardModel
import com.pronted.dto.timeline.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

interface TimelineApi {
    @GET("academics/timetable")
    suspend fun fetchEmployeeTimetable(
        @Query("employeeProfileId") empId: Int,
        @Query("date") date: String,
        @HeaderMap headers: Map<String, String>?
    ): Response<ArrayList<TimetableData>>

    @GET("academics/timetable")
    suspend fun fetchChildrenTimetable(
        @Query("classroomId") classroomId: Int,
        @Query("date") date: String,
        @HeaderMap headers: Map<String, String>?
    ): Response<ArrayList<TimetableData>>

    @GET("students/birthdays")
    suspend fun fetchBirthdays(
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String,
        @Query("size") size: Int,
        @Query("page") page: Int
    ): Response<BirthdaysData>

    @GET("hr/employees/birthdays")
    suspend fun fetchEmployeeBirthdays(
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String,
        @Query("size") size: Int,
        @Query("page") page: Int
    ): Response<BirthdaysData>

    @GET("finance/summary")
    suspend fun fetchFinanceSummary(
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String
    ): Response<FinanceModel>

    @GET("finance/fees")
    suspend fun fetchFeeDetails(
        @Query("studentProfileId") studentProfileId: Int
    ): Response<ArrayList<FeesDetailModel>>

    @GET("finance/fees/payments-summary")
    suspend fun fetchCollection(
        @Query("groupBy") groupBy: String,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String
    ): Response<ArrayList<FeePayment>>

    @GET("finance/expenses/summary")
    suspend fun fetchExpenses(
        @Query("groupBy") groupBy: String,
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String
    ): Response<ArrayList<FeePayment>>

}