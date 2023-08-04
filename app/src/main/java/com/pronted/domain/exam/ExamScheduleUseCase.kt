package com.pronted.domain.exam

import com.pronted.dto.exam.ExamDropDownAction
import com.pronted.dto.exam.ExamScheduleAction
import com.pronted.dto.exam.ExamScheduleDeleteAction
import com.pronted.dto.login.ClassesAction
import kotlinx.coroutines.flow.Flow

interface ExamScheduleUseCase {

    /**
     * Fetch available classes
     */
    fun fetchAvailableClasses(): Flow<ClassesAction>

    /**
     * Fetch exam by class room id
     *
     * @param classRoomId
     * @return
     */
    fun fetchExamByClassRoomId(classRoomId: Int): Flow<ExamDropDownAction>

    /**
     * Fetch exam schedule by exam id
     *
     * @param examId
     * @return
     */
    fun fetchExamScheduleByExamId(examId: Int): Flow<ExamScheduleAction>

    /**
     * Fetch exam for parent
     *
     * @return
     */
    fun fetchExamForParent(): Flow<ExamDropDownAction>

    /**
     * Delete exam schedule
     *
     * @param scheduleId
     * @return
     */
    fun deleteExamSchedule(scheduleId: Int): Flow<ExamScheduleDeleteAction>
}