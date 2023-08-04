package com.pronted.dto.exam

import com.pronted.dto.ErrorResponse

sealed class AddExamScheduleAction {
    class Success(val examSchedule: ExamScheduleModel) : AddExamScheduleAction()

    class Fail(val errorResponse: ErrorResponse) : AddExamScheduleAction()
}
