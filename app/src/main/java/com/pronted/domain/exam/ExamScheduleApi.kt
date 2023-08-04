package com.pronted.domain.exam

import com.pronted.dto.exam.ExamDropDownModel
import com.pronted.dto.exam.ExamScheduleModel
import com.pronted.dto.exam.ExamScheduleRequest
import com.pronted.dto.exam.ScheduleModel
import retrofit2.Response
import retrofit2.http.*

interface ExamScheduleApi {
    /**
     * Fetch exams by class room id
     */
    @GET("academics/exams")
    suspend fun fetchExamByClassRoomId(
        @Query("classroomId") classroomId: Int
    ): Response<ArrayList<ExamDropDownModel>>

    @GET("academics/exams/schedule")
    suspend fun fetchExamScheduleByExamId(
        @Query("examId") examId: Int
    ): Response<ExamScheduleModel>

    @POST("academics/exams/schedule")
    suspend fun createExamSchedule(
        @Body examSchedule: ExamScheduleRequest
    ): Response<ExamScheduleModel>

    @GET("academics/exams")
    suspend fun fetchExamForParent(): Response<ArrayList<ExamDropDownModel>>

    @PUT("academics/exams/schedule")
    suspend fun updateExamSchedule(
        @Body scheduleModel: ScheduleModel
    ): Response<ScheduleModel>

    @DELETE("academics/exams/schedule")
    suspend fun deleteExamSchedule(
        @Query("scheduleId") scheduleId: Int
    ): Response<Unit>
}