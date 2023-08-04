package com.pronted.data.exam

import com.pronted.domain.exam.ExamScheduleApi
import com.pronted.domain.exam.ExamScheduleRepository
import com.pronted.dto.ApiResponse
import com.pronted.dto.exam.ExamDropDownModel
import com.pronted.dto.exam.ExamScheduleModel
import com.pronted.dto.exam.ExamScheduleRequest
import com.pronted.dto.exam.ScheduleModel
import com.pronted.utils.extentions.handleApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExamScheduleRepositoryImpl(private val examScheduleApi: ExamScheduleApi) :
    ExamScheduleRepository {

    override fun fetchExamsByClassroomId(classroomId: Int): Flow<ApiResponse<ArrayList<ExamDropDownModel>>> =
        flow {
            emit(handleApi {
                examScheduleApi.fetchExamByClassRoomId((classroomId))
            })
        }

    override fun fetchExamScheduleByExamId(examId: Int): Flow<ApiResponse<ExamScheduleModel>> =
        flow {
            emit(handleApi {
                examScheduleApi.fetchExamScheduleByExamId(examId)
            })
        }

    override fun createExamSchedule(examScheduleRequest: ExamScheduleRequest): Flow<ApiResponse<ExamScheduleModel>> =
        flow {
            emit(handleApi {
                examScheduleApi.createExamSchedule(examScheduleRequest)
            })
        }

    override fun fetchExamsForParent(): Flow<ApiResponse<ArrayList<ExamDropDownModel>>> = flow {
        emit(handleApi {
            examScheduleApi.fetchExamForParent()
        })
    }

    override fun updateExamSchedule(scheduleModel: ScheduleModel): Flow<ApiResponse<ScheduleModel>> = flow {
        emit(handleApi {
            examScheduleApi.updateExamSchedule(scheduleModel)
        })
    }

    override fun deleteExamSchedule(scheduleId: Int): Flow<ApiResponse<Unit>> = flow {
        emit(handleApi {
            examScheduleApi.deleteExamSchedule(scheduleId)
        })
    }
}