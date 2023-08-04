package com.pronted.data.student

import com.pronted.domain.student.StudentApi
import com.pronted.domain.student.StudentDataRepository
import com.pronted.dto.ApiResponse
import com.pronted.dto.student.BloodGroup
import com.pronted.dto.student.ClassDropDownModel
import com.pronted.dto.student.StudentCount
import com.pronted.dto.student.StudentProfileResponse
import com.pronted.utils.extentions.handleApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StudentDataRepositoryImpl(private val studentApi: StudentApi) :
    StudentDataRepository {
    override fun fetchStudentProfile(): Flow<ApiResponse<StudentProfileResponse>> = flow {
        emit(handleApi {
            studentApi.fetchStudentProfile()
        })
    }

    override fun fetchAvailableClasses(): Flow<ApiResponse<ArrayList<ClassDropDownModel>>> = flow {
        emit(handleApi {
            studentApi.fetchClassRoomDropdowns()
        })
    }

    override fun fetchStudentsByClassroomId(classroomId: Int): Flow<ApiResponse<ArrayList<StudentProfileResponse>>> =
        flow {
            emit(handleApi {
                studentApi.fetchStudentListByClassroomId(classroomId)
            })
        }

    override fun fetchStudentCount(headers: Map<String, String>?): Flow<ApiResponse<StudentCount>> = flow {
        emit(handleApi {
            studentApi.fetchStudentCount(headers)
        })
    }

    override fun fetchBloodGroups(): Flow<ApiResponse<ArrayList<BloodGroup>>> = flow {
        emit(handleApi {
            studentApi.fetchBloodGroups()
        })
    }

    override fun updateStudentProfile(editedProfileData: StudentProfileResponse): Flow<ApiResponse<StudentProfileResponse>>  = flow {
        emit(handleApi {
            studentApi.updateStudentProfile(editedProfileData.studentProfileId, editedProfileData)
        })
    }
}