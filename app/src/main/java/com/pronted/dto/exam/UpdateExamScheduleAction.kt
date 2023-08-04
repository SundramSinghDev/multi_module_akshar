package com.pronted.dto.exam

import com.pronted.dto.ErrorResponse

sealed class UpdateExamScheduleAction {
    class Success(val scheduleModel: ScheduleModel) : UpdateExamScheduleAction()

    class Fail(val errorResponse: ErrorResponse) : UpdateExamScheduleAction()
}
