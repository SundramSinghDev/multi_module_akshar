package com.pronted.data.student

import com.pronted.domain.student.StudentDataRepository
import com.pronted.domain.student.StudentDataUseCase
import com.pronted.dto.ApiResponse
import com.pronted.dto.ErrorResponse
import com.pronted.dto.login.ClassesAction
import com.pronted.dto.student.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StudentDataUseCaseImpl(private val studentDataRepository: StudentDataRepository) :
    StudentDataUseCase {
    override fun fetchStudentProfile(): Flow<StudentProfileAction> =
        studentDataRepository.fetchStudentProfile().map {
            when (it) {
                is ApiResponse.Success -> {
                    StudentProfileAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    StudentProfileAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    StudentProfileAction.Fail(ErrorResponse())
                }
            }
        }

    override fun fetchAvailableClasses(): Flow<ClassesAction> =
        studentDataRepository.fetchAvailableClasses().map {
            when (it) {
                is ApiResponse.Success -> {
                    ClassesAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    ClassesAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    ClassesAction.Fail(ErrorResponse())
                }
            }
        }

    override fun fetchStudentsByClassroomId(classroomId: Int): Flow<StudentsAction> =
        studentDataRepository.fetchStudentsByClassroomId(classroomId).map {
            when (it) {
                is ApiResponse.Success -> {
                    StudentsAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    StudentsAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    StudentsAction.Fail(ErrorResponse())
                }
            }
        }

    override fun fetchStudentCount(headers: Map<String, String>?): Flow<StudentsCountAction> =
        studentDataRepository.fetchStudentCount(headers).map {
            when (it) {
                is ApiResponse.Success -> {
                    StudentsCountAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    StudentsCountAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    StudentsCountAction.Fail(ErrorResponse())
                }
            }
        }

    override fun fetchBloodGroups(): Flow<BloodGroupAction> =
        studentDataRepository.fetchBloodGroups().map {
            when (it) {
                is ApiResponse.Success -> {
                    BloodGroupAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    BloodGroupAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    BloodGroupAction.Fail(ErrorResponse())
                }
            }
        }

    override fun updateStudentProfile(editedProfileData: StudentProfileResponse): Flow<UpdateStudentProfileAction> = studentDataRepository.updateStudentProfile(editedProfileData).map {
        when (it) {
            is ApiResponse.Success -> {
                UpdateStudentProfileAction.Success(it.data)
            }
            is ApiResponse.Error -> {
                UpdateStudentProfileAction.Fail(it.errorResponse)
            }
            is ApiResponse.Exception -> {
                UpdateStudentProfileAction.Fail(ErrorResponse())
            }
        }
    }
}