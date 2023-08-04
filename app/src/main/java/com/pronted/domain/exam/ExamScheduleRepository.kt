package com.pronted.domain.exam

import com.pronted.dto.ApiResponse
import com.pronted.dto.exam.ExamDropDownModel
import com.pronted.dto.exam.ExamScheduleModel
import com.pronted.dto.exam.ExamScheduleRequest
import com.pronted.dto.exam.ScheduleModel
import kotlinx.coroutines.flow.Flow

interface ExamScheduleRepository {
    /**
     * Fetch exams by classroom id
     *
     * @param classroomId
     * @return
     */
    fun fetchExamsByClassroomId(classroomId: Int): Flow<ApiResponse<ArrayList<ExamDropDownModel>>>

    /**
     * Fetch exam schedule by exam id
     *
     * @param examId
     * @return
     */
    fun fetchExamScheduleByExamId(examId: Int): Flow<ApiResponse<ExamScheduleModel>>

    /**
     * Create exam schedule
     *
     * @param examScheduleRequest
     * @return
     */
    fun createExamSchedule(examScheduleRequest: ExamScheduleRequest): Flow<ApiResponse<ExamScheduleModel>>

    /**
     * Fetch exams for parent
     *
     * @return
     */
    fun fetchExamsForParent(): Flow<ApiResponse<ArrayList<ExamDropDownModel>>>

    /**
     * Update exam schedule
     *
     * @param scheduleModel
     * @return
     */
    fun updateExamSchedule(scheduleModel: ScheduleModel): Flow<ApiResponse<ScheduleModel>>

    /**
     * Delete exam schedule
     *
     * @param scheduleId
     * @return
     */
    fun deleteExamSchedule(scheduleId: Int): Flow<ApiResponse<Unit>>
}