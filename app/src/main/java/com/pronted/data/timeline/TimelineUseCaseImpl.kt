package com.pronted.data.timeline

import com.pronted.domain.timeline.TimelineRepository
import com.pronted.domain.timeline.TimelineUseCase
import com.pronted.dto.ApiResponse
import com.pronted.dto.ErrorResponse
import com.pronted.dto.timeline.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TimelineUseCaseImpl(private val timelineRepository: TimelineRepository) : TimelineUseCase {

    override fun fetchTimetable(
        requestId: Int,
        date: String,
        isEmployee: Boolean,
        headers: Map<String, String>?
    ): Flow<TimetableAction> = timelineRepository.fetchTimeTable(requestId, date, isEmployee, headers).map {
        when (it) {
            is ApiResponse.Success -> {
                TimetableAction.Success(it.data)
            }
            is ApiResponse.Error -> {
                TimetableAction.Fail(it.errorResponse)
            }
            is ApiResponse.Exception -> {
                TimetableAction.Fail(ErrorResponse())
            }
        }
    }


    override fun fetchBirthdays(
        fromDate: String,
        toDate: String,
        size: Int,
        page: Int,
        forEmployee: Boolean
    ): Flow<BirthdaysAction> =
        timelineRepository.fetchBirthdays(fromDate, toDate, size, page, forEmployee).map {
            when (it) {
                is ApiResponse.Success -> {
                    BirthdaysAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    BirthdaysAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    BirthdaysAction.Fail(ErrorResponse())
                }
            }
        }

    override fun fetchFinanceSummary(
        fromDate: String,
        toDate: String,
    ): Flow<FinanceSummaryAction> = timelineRepository.fetchFinanceSummary(fromDate, toDate).map {
        when (it) {
            is ApiResponse.Success -> {
                FinanceSummaryAction.Success(it.data)
            }
            is ApiResponse.Error -> {
                FinanceSummaryAction.Fail(it.errorResponse)
            }
            is ApiResponse.Exception -> {
                FinanceSummaryAction.Fail(ErrorResponse())
            }
        }
    }

    override fun fetchFeeDetails(
        profileId: Int
    ): Flow<FeeDetailsAction> = timelineRepository.fetchFeeDetails(profileId).map {
        when (it) {
            is ApiResponse.Success -> {
                FeeDetailsAction.Success(it.data)
            }
            is ApiResponse.Error -> {
                FeeDetailsAction.Fail(it.errorResponse)
            }
            is ApiResponse.Exception -> {
                FeeDetailsAction.Fail(ErrorResponse())
            }
        }
    }


    override fun fetchCollectionSummary(
        groupBy: String,
        fromDate: String,
        toDate: String,
    ): Flow<CollectionSummaryAction> =
        timelineRepository.fetchCollectionSummary(groupBy, fromDate, toDate).map {
            when (it) {
                is ApiResponse.Success -> {
                    CollectionSummaryAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    CollectionSummaryAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    CollectionSummaryAction.Fail(ErrorResponse())
                }
            }
        }

    override fun fetchExpensesSummary(
        groupBy: String,
        fromDate: String,
        toDate: String,
    ): Flow<ExpensesSummaryAction> =
        timelineRepository.fetchExpensesSummary(groupBy, fromDate, toDate).map {
            when (it) {
                is ApiResponse.Success -> {
                    ExpensesSummaryAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    ExpensesSummaryAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    ExpensesSummaryAction.Fail(ErrorResponse())
                }
            }
        }

}