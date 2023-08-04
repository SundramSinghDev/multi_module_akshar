package com.pronted.presentation.scheduleexam

import androidx.lifecycle.ViewModel
import com.pronted.domain.exam.AddExamScheduleUseCase
import com.pronted.dto.exam.AddExamScheduleAction
import com.pronted.dto.exam.ExamScheduleRequest
import com.pronted.dto.exam.ScheduleModel
import com.pronted.dto.exam.UpdateExamScheduleAction
import kotlinx.coroutines.flow.Flow

class AddExamScheduleViewModel(private val addExamScheduleUseCase: AddExamScheduleUseCase) :
    ViewModel() {

    /**
     * Create exam schedule
     *
     * @param examScheduleRequest
     * @return
     */
    fun createExamSchedule(examScheduleRequest: ExamScheduleRequest): Flow<AddExamScheduleAction> =
        addExamScheduleUseCase.createExamSchedule(examScheduleRequest)

    fun updateExamSchedule(scheduleModel: ScheduleModel): Flow<UpdateExamScheduleAction> =
        addExamScheduleUseCase.updateExamSchedule(scheduleModel)
}