package com.pronted.domain.attendance

import com.pronted.dto.attendance.AttendanceChart
import com.pronted.dto.attendance.AttendanceModel
import com.pronted.dto.attendance.ShiftResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AttendanceApi {
    @GET("attendance/stats")
    suspend fun fetchAttendanceStatsByTime(
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String,
        @Query("studentProfileId") studentProfileId: Int
    ): Response<ArrayList<AttendanceChart>>

    @GET("attendance/stats")
    suspend fun fetchAttendanceStatsByYearly(
        @Query("studentProfileId") studentProfileId: Int
    ): Response<ArrayList<AttendanceChart>>

    @GET("attendance/today")
    suspend fun fetchTodayAttendance(
        @Query("studentProfileId") studentProfileId: Int
    ): Response<AttendanceModel>

    @GET("attendance/shifts")
    suspend fun fetchShift(
        @Query("profileType") profileType: String,
        @Query("classroomIdList") classRoomId: Int?
    ): Response<ArrayList<ShiftResponseModel>>

    @GET("attendance/students")
    suspend fun fetchStudentsAttendance(
        @Query("classroomId") classRoomId: Int,
        @Query("shiftId") shiftId: Int,
        @Query("date") date: String
    ): Response<ArrayList<AttendanceModel>>

    @POST("attendance/record-student-attendance")
    suspend fun saveStudentsAttendance(
        @Query("profileType") profileType: String,
        @Query("classroomId") classroomId: Int,
        @Body studentList: List<AttendanceModel>
    ): Response<ArrayList<AttendanceModel>>

    @GET("attendance/employees")
    suspend fun fetchEmployeesAttendance(
        @Query("shiftId") shiftId: Int,
        @Query("date") date: String
    ): Response<ArrayList<AttendanceModel>>

    @POST("attendance/record-employee-attendance")
    suspend fun saveEmployeesAttendance(
        @Query("profileType") profileType: String,
        @Body employeesList: List<AttendanceModel>
    ): Response<ArrayList<AttendanceModel>>
}