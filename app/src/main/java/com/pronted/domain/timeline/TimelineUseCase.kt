package com.pronted.domain.timeline

import com.pronted.dto.timeline.*
import kotlinx.coroutines.flow.Flow

/**
 * Image access
 */

interface TimelineUseCase {
    /**
     * Fetch Employee Timetable
     *
     * @return
     */
    fun fetchTimetable(requestId:Int, date:String, isEmployee:Boolean, headers: Map<String, String>?) : Flow<TimetableAction>

    /**
     * Fetch Birthdays
     *
     * @param fromDate
     * @param toDate
     * @param size
     * @param page
     * @return
     */
    fun fetchBirthdays(fromDate:String, toDate:String, size:Int, page:Int,forEmployee:Boolean):Flow<BirthdaysAction>

    /**
     * Fetch finance summary
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    fun fetchFinanceSummary(fromDate:String, toDate:String):Flow<FinanceSummaryAction>

    /**
     * Fetch finance summary
     *
     * @param profileId
     * @return
     */
    fun fetchFeeDetails(profileId:Int):Flow<FeeDetailsAction>

    /**
     * Fetch collection summary
     *@param groupBy
     * @param fromDate
     * @param toDate
     * @return
     */
    fun fetchCollectionSummary(groupBy:String, fromDate:String, toDate:String):Flow<CollectionSummaryAction>

    /**
     * Fetch expenses summary
     *@param groupBy
     * @param fromDate
     * @param toDate
     * @return
     */
    fun fetchExpensesSummary(groupBy:String, fromDate:String, toDate:String):Flow<ExpensesSummaryAction>

}