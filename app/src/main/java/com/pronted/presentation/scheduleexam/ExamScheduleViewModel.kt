package com.pronted.presentation.scheduleexam

import androidx.lifecycle.ViewModel
import com.pronted.domain.exam.ExamScheduleUseCase
import com.pronted.dto.exam.ExamDropDownAction
import com.pronted.dto.exam.ExamScheduleAction
import com.pronted.dto.exam.ExamScheduleDeleteAction
import com.pronted.dto.login.ClassesAction
import kotlinx.coroutines.flow.Flow

class ExamScheduleViewModel(private val examScheduleUseCase: ExamScheduleUseCase) : ViewModel() {

    /**
     * Fetch available classes
     */
    fun fetchAvailableClasses():
            Flow<ClassesAction> = examScheduleUseCase.fetchAvailableClasses()

    /**
     * Fetch exam drop down by class room id
     *
     * @param classRoomId
     * @return
     */
    fun fetchExamDropDownByClassRoomId(classRoomId: Int): Flow<ExamDropDownAction> =
        examScheduleUseCase.fetchExamByClassRoomId(classRoomId)

    /**
     * Fetch exam schedule by exam id
     *
     * @param examId
     * @return
     */
    fun fetchExamScheduleByExamId(examId: Int): Flow<ExamScheduleAction> =
        examScheduleUseCase.fetchExamScheduleByExamId(examId)

    /**
     * Delete schedule
     *
     * @param scheduleId
     * @return
     */
    fun deleteSchedule(scheduleId: Int): Flow<ExamScheduleDeleteAction> =
        examScheduleUseCase.deleteExamSchedule(scheduleId)
}