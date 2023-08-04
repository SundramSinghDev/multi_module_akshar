package com.pronted.data.exam

import com.pronted.domain.exam.ExamScheduleRepository
import com.pronted.domain.exam.ExamScheduleUseCase
import com.pronted.domain.student.StudentDataRepository
import com.pronted.dto.ApiResponse
import com.pronted.dto.ErrorResponse
import com.pronted.dto.exam.ExamDropDownAction
import com.pronted.dto.exam.ExamScheduleAction
import com.pronted.dto.exam.ExamScheduleDeleteAction
import com.pronted.dto.login.ClassesAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExamScheduleUseCaseImpl(
    private val examScheduleRepository: ExamScheduleRepository,
    private val studentDataRepository: StudentDataRepository
) : ExamScheduleUseCase {

    override fun fetchAvailableClasses(): Flow<ClassesAction> =
        studentDataRepository.fetchAvailableClasses().map {
            when (it) {
                is ApiResponse.Success -> {
                    ClassesAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    ClassesAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    ClassesAction.Fail(ErrorResponse())
                }
            }
        }

    override fun fetchExamByClassRoomId(classRoomId: Int): Flow<ExamDropDownAction> =
        examScheduleRepository.fetchExamsByClassroomId(classRoomId).map {
            when (it) {
                is ApiResponse.Success -> {
                    ExamDropDownAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    ExamDropDownAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    ExamDropDownAction.Fail(ErrorResponse())
                }
            }
        }

    override fun fetchExamScheduleByExamId(examId: Int): Flow<ExamScheduleAction> =
        examScheduleRepository.fetchExamScheduleByExamId(examId).map {
            when (it) {
                is ApiResponse.Success -> {
                    ExamScheduleAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    ExamScheduleAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    ExamScheduleAction.Fail(ErrorResponse())
                }
            }
        }

    override fun fetchExamForParent(): Flow<ExamDropDownAction> =
        examScheduleRepository.fetchExamsForParent().map {
            when (it) {
                is ApiResponse.Success -> {
                    ExamDropDownAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    ExamDropDownAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    ExamDropDownAction.Fail(ErrorResponse())
                }
            }
        }

    override fun deleteExamSchedule(scheduleId: Int): Flow<ExamScheduleDeleteAction> =
        examScheduleRepository.deleteExamSchedule(scheduleId).map {
            when (it) {
                is ApiResponse.Success -> {
                    ExamScheduleDeleteAction.Success
                }
                is ApiResponse.Error -> {
                    ExamScheduleDeleteAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    ExamScheduleDeleteAction.Fail(ErrorResponse())
                }
            }
        }
}