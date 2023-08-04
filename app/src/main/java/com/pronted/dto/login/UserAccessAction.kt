package com.pronted.dto.login

import com.pronted.dto.ErrorResponse
import com.pronted.dto.images.ProfileImageResponse
import com.pronted.dto.student.ClassDropDownModel
import com.pronted.dto.student.StudentProfileResponse

sealed class UserAccessAction {
    class Success(val response: ArrayList<UserAppList>) : UserAccessAction()

    class Fail(val errorResponse: ErrorResponse) : UserAccessAction()
}

sealed class ImageAction {
    class Success(val imageResponse: ProfileImageResponse) : ImageAction()

    class Fail(val errorResponse: ErrorResponse) : ImageAction()
}
sealed class UploadImageAction {
    class Success(val imageResponse: ProfileImageResponse) : UploadImageAction()

    class Fail(val errorResponse: ErrorResponse) : UploadImageAction()
}


sealed class SecurityGroupAction {
    class Success(val securityGroupList: ArrayList<String>) : SecurityGroupAction()

    class Fail(val errorResponse: ErrorResponse) : SecurityGroupAction()
}

sealed class ClassesAction {
    class Success(val classes: ArrayList<ClassDropDownModel>) : ClassesAction()

    class Fail(val errorResponse: ErrorResponse) : ClassesAction()
}

sealed class SchoolBoardAction {
    class Success(val response: BoardType) : SchoolBoardAction()

    class Fail(val errorResponse: ErrorResponse) : SchoolBoardAction()
}
