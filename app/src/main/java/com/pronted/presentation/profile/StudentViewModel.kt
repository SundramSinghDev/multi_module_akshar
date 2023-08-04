package com.pronted.presentation.profile

import androidx.lifecycle.ViewModel
import com.pronted.domain.student.StudentDataUseCase
import com.pronted.dto.login.ClassesAction
import com.pronted.dto.student.*
import kotlinx.coroutines.flow.Flow

class StudentViewModel(private val studentDataUseCase: StudentDataUseCase) : ViewModel() {
    /**
     * Fetch student profile
     *
     */
    fun fetchStudentProfile():
            Flow<StudentProfileAction> = studentDataUseCase.fetchStudentProfile()

    /**
     * Fetch available classess
     */
    fun fetchAvailableClasses():
            Flow<ClassesAction> = studentDataUseCase.fetchAvailableClasses()

    /**
     * Fetch students count
     */
    fun fetchStudentsCount(headers: Map<String, String>?):
            Flow<StudentsCountAction> = studentDataUseCase.fetchStudentCount(headers)

    fun fetchStudentsByClassroomId(classroomId: Int):
            Flow<StudentsAction> = studentDataUseCase.fetchStudentsByClassroomId(classroomId)

    fun fetchBloodGroups():
            Flow<BloodGroupAction> = studentDataUseCase.fetchBloodGroups()

    fun updateStudentProfile(editedProfileData: StudentProfileResponse):
            Flow<UpdateStudentProfileAction> = studentDataUseCase.updateStudentProfile(editedProfileData)


}