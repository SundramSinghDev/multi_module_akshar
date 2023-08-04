package com.pronted.dto.exam

import com.pronted.dto.ErrorResponse

sealed class ExamDropDownAction{
    class Success(val exams: ArrayList<ExamDropDownModel>) : ExamDropDownAction()

    class Fail(val errorResponse: ErrorResponse) : ExamDropDownAction()
}
