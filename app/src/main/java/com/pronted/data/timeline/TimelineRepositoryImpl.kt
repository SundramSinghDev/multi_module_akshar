package com.pronted.data.timeline

import com.pronted.domain.timeline.TimelineApi
import com.pronted.domain.timeline.TimelineRepository
import com.pronted.dto.ApiResponse
import com.pronted.dto.feepayments.data.FeesDetailModel
import com.pronted.dto.timeline.*
import com.pronted.utils.extentions.handleApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TimelineRepositoryImpl(private val timelineApi: TimelineApi) : TimelineRepository {
    override fun fetchTimeTable(
        requestId: Int,
        date: String,
        isEmployee: Boolean,
        headers: Map<String, String>?
    ): Flow<ApiResponse<ArrayList<TimetableData>>> = flow {
        emit(handleApi {
            if (isEmployee)
                timelineApi.fetchEmployeeTimetable(
                    empId = requestId,
                    date = date,
                    headers = headers
                )
            else
                timelineApi.fetchChildrenTimetable(
                    classroomId = requestId,
                    date = date,
                    headers = headers
                )
        })
    }

    override fun fetchBirthdays(
        fromDate: String,
        toDate: String,
        size: Int,
        page: Int,
        forEmployee: Boolean
    ): Flow<ApiResponse<BirthdaysData>> = flow {
        emit(handleApi {
            if (forEmployee) {
                timelineApi.fetchEmployeeBirthdays(
                    fromDate = fromDate,
                    toDate = toDate,
                    size = size,
                    page = page
                )
            } else {
                timelineApi.fetchBirthdays(
                    fromDate = fromDate,
                    toDate = toDate,
                    size = size,
                    page = page
                )
            }
        })
    }

    override fun fetchFinanceSummary(
        fromDate: String,
        toDate: String
    ): Flow<ApiResponse<FinanceModel>> = flow {
        emit(handleApi {
            timelineApi.fetchFinanceSummary(
                fromDate = fromDate,
                toDate = toDate
            )
        })
    }

    override fun fetchFeeDetails(
        profileId:Int
    ): Flow<ApiResponse<ArrayList<FeesDetailModel>>> = flow {
        emit(handleApi {
            timelineApi.fetchFeeDetails(
                studentProfileId = profileId
            )
        })
    }

    override fun fetchCollectionSummary(
        groupBy: String,
        fromDate: String,
        toDate: String
    ): Flow<ApiResponse<ArrayList<FeePayment>>> = flow {
        emit(handleApi {
            timelineApi.fetchCollection(
                groupBy = groupBy,
                fromDate = fromDate,
                toDate = toDate
            )
        })
    }

    override fun fetchExpensesSummary(
        groupBy: String,
        fromDate: String,
        toDate: String
    ): Flow<ApiResponse<ArrayList<FeePayment>>> = flow {
        emit(handleApi {
            timelineApi.fetchExpenses(
                groupBy = groupBy,
                fromDate = fromDate,
                toDate = toDate
            )
        })
    }
}