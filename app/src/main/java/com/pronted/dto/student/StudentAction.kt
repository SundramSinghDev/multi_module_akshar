package com.pronted.dto.student

import com.pronted.dto.ErrorResponse

sealed class StudentsCountAction {
    class Success(val response: StudentCount) : StudentsCountAction()

    class Fail(val errorResponse: ErrorResponse) : StudentsCountAction()
}

sealed class StudentProfileAction {
    class Success(val studentProfileResponse: StudentProfileResponse) : StudentProfileAction()

    class Fail(val errorResponse: ErrorResponse) : StudentProfileAction()
}

sealed class UpdateStudentProfileAction {
    class Success(val studentProfileResponse: StudentProfileResponse) : UpdateStudentProfileAction()

    class Fail(val errorResponse: ErrorResponse) : UpdateStudentProfileAction()
}

sealed class StudentsAction {
    class Success(val students: ArrayList<StudentProfileResponse>) : StudentsAction()

    class Fail(val errorResponse: ErrorResponse) : StudentsAction()

}

sealed class BloodGroupAction {
    class Success(val bloodGroups: ArrayList<BloodGroup>) : BloodGroupAction()

    class Fail(val errorResponse: ErrorResponse) : BloodGroupAction()

}