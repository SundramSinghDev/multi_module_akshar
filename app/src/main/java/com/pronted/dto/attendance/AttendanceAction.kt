package com.pronted.dto.attendance

import com.pronted.dto.ErrorResponse

sealed class AttendanceAction {
    class Success(val response: AttendanceModel) : AttendanceAction()

    class Fail(val errorResponse: ErrorResponse) : AttendanceAction()
}

sealed class AttendanceChartAction {
    class Success(val response: ArrayList<AttendanceChart>) : AttendanceChartAction()

    class Fail(val errorResponse: ErrorResponse) : AttendanceChartAction()
}

sealed class ShiftAction {
    class Success(val response: ArrayList<ShiftResponseModel>) : ShiftAction()
    class Fail(val errorResponse: ErrorResponse) : ShiftAction()
}

sealed class StudentsAttendanceAction {
    class Success(val response: ArrayList<AttendanceModel>) : StudentsAttendanceAction()
    class Fail(val errorResponse: ErrorResponse) : StudentsAttendanceAction()
}

sealed class SaveStudentsAttendanceAction {
    class Success(val response: ArrayList<AttendanceModel>) : SaveStudentsAttendanceAction()
    class Fail(val errorResponse: ErrorResponse) : SaveStudentsAttendanceAction()
}

sealed class EmployeesAttendanceAction {
    class Success(val response: ArrayList<AttendanceModel>) : EmployeesAttendanceAction()
    class Fail(val errorResponse: ErrorResponse) : EmployeesAttendanceAction()
}

sealed class SaveEmployeesAttendanceAction {
    class Success(val response: ArrayList<AttendanceModel>) : SaveEmployeesAttendanceAction()
    class Fail(val errorResponse: ErrorResponse) : SaveEmployeesAttendanceAction()
}