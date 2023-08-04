package com.pronted.presentation.scheduleexam

import androidx.lifecycle.ViewModel
import com.pronted.domain.exam.ExamScheduleUseCase
import com.pronted.dto.exam.ExamDropDownAction
import com.pronted.dto.exam.ExamScheduleAction
import kotlinx.coroutines.flow.Flow

class ParentExamScheduleViewModel(private val examScheduleUseCase: ExamScheduleUseCase) :
    ViewModel() {

    /**
     * Fetch exam drop down for parent
     *
     * @return
     */
    fun fetchExamDropDownForParent(): Flow<ExamDropDownAction> =
        examScheduleUseCase.fetchExamForParent()

    /**
     * Fetch exam schedule by exam id
     *
     * @param examId
     * @return
     */
    fun fetchExamScheduleByExamId(examId: Int): Flow<ExamScheduleAction> =
        examScheduleUseCase.fetchExamScheduleByExamId(examId)
}