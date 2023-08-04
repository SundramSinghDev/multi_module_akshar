package com.pronted.presentation.timeline

import androidx.lifecycle.ViewModel
import com.pronted.domain.timeline.TimelineUseCase
import com.pronted.dto.timeline.*
import kotlinx.coroutines.flow.Flow

class TimeLineViewModel(private val timelineUseCase: TimelineUseCase) : ViewModel() {

    /**
     * Fetch employee timetable
     *
     */
    fun fetchTimetable(requestId:Int, date:String, isEmployee:Boolean):
            Flow<TimetableAction> = timelineUseCase.fetchTimetable(requestId, date, isEmployee, null)

    /**
     * Fetch employee timetable
     *
     */
    fun fetchTimetable(requestId:Int, date:String, isEmployee:Boolean, headers: Map<String, String>?):
            Flow<TimetableAction> = timelineUseCase.fetchTimetable(requestId, date, isEmployee, headers)


    /**
     * Fetch Birthdays
     */
    fun fetchBirthdays(fromDate:String, toDate:String, size:Int, page:Int, forEmployee:Boolean):
            Flow<BirthdaysAction> = timelineUseCase.fetchBirthdays(fromDate, toDate, size, page, forEmployee)

    /**
     * Fetch finance summary
     */
    fun fetchFinanceSummary(fromDate:String, toDate:String):
            Flow<FinanceSummaryAction> = timelineUseCase.fetchFinanceSummary(fromDate, toDate)

    /**
     * Fetch fee details based on profile id
     */
    fun fetchFeeDetails(profileId:Int):
            Flow<FeeDetailsAction> = timelineUseCase.fetchFeeDetails(profileId)

    /**
     * Fetch collection summary
     */
    fun fetchCollectionSummary(groupBy:String, fromDate:String, toDate:String):
            Flow<CollectionSummaryAction> = timelineUseCase.fetchCollectionSummary(groupBy,fromDate, toDate)
    /**
     * Fetch expenses summary
     */
    fun fetchExpensesSummary(groupBy:String, fromDate:String, toDate:String):
            Flow<ExpensesSummaryAction> = timelineUseCase.fetchExpensesSummary(groupBy, fromDate, toDate)


}