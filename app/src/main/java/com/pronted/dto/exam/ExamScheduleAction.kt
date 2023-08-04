package com.pronted.dto.exam

import com.pronted.dto.ErrorResponse

sealed class ExamScheduleAction {
    class Success(val examSchedule: ExamScheduleModel) : ExamScheduleAction()

    class Fail(val errorResponse: ErrorResponse) : ExamScheduleAction()
}
