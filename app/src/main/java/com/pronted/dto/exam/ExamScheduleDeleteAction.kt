package com.pronted.dto.exam

import com.pronted.dto.ErrorResponse

sealed class ExamScheduleDeleteAction{
    object Success : ExamScheduleDeleteAction()

    class Fail(val errorResponse: ErrorResponse) : ExamScheduleDeleteAction()
}
