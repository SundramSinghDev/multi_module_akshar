package com.pronted.domain.exam

import com.pronted.dto.exam.*
import kotlinx.coroutines.flow.Flow

interface AddExamScheduleUseCase {

    fun createExamSchedule(examScheduleRequest: ExamScheduleRequest): Flow<AddExamScheduleAction>

    fun updateExamSchedule(scheduleModel: ScheduleModel): Flow<UpdateExamScheduleAction>
}