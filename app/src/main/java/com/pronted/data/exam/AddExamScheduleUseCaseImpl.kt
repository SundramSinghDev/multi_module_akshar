package com.pronted.data.exam

import com.pronted.domain.exam.AddExamScheduleUseCase
import com.pronted.domain.exam.ExamScheduleRepository
import com.pronted.dto.ApiResponse
import com.pronted.dto.ErrorResponse
import com.pronted.dto.exam.AddExamScheduleAction
import com.pronted.dto.exam.ExamScheduleRequest
import com.pronted.dto.exam.ScheduleModel
import com.pronted.dto.exam.UpdateExamScheduleAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AddExamScheduleUseCaseImpl(
    private val examScheduleRepository: ExamScheduleRepository
) : AddExamScheduleUseCase {

    override fun createExamSchedule(examScheduleRequest: ExamScheduleRequest): Flow<AddExamScheduleAction> =
        examScheduleRepository.createExamSchedule(examScheduleRequest).map {
            when (it) {
                is ApiResponse.Success -> {
                    AddExamScheduleAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    AddExamScheduleAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    AddExamScheduleAction.Fail(ErrorResponse())
                }
            }
        }

    override fun updateExamSchedule(scheduleModel: ScheduleModel): Flow<UpdateExamScheduleAction> =
        examScheduleRepository.updateExamSchedule(scheduleModel).map {
            when (it) {
                is ApiResponse.Success -> {
                    UpdateExamScheduleAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    UpdateExamScheduleAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    UpdateExamScheduleAction.Fail(ErrorResponse())
                }
            }
        }
}