package com.pronted.domain.student

import com.pronted.dto.ApiResponse
import com.pronted.dto.student.BloodGroup
import com.pronted.dto.student.ClassDropDownModel
import com.pronted.dto.student.StudentCount
import com.pronted.dto.student.StudentProfileResponse
import kotlinx.coroutines.flow.Flow

interface StudentDataRepository {

    /**
     * Fetch Student Profile
     *
     */
    fun fetchStudentProfile():
            Flow<ApiResponse<StudentProfileResponse>>

    /**
     * Fetch available classes
     */
    fun fetchAvailableClasses(): Flow<ApiResponse<ArrayList<ClassDropDownModel>>>

    /**
     * Fetch available classes
     */
    fun fetchStudentCount(headers: Map<String, String>?): Flow<ApiResponse<StudentCount>>

    /**
     * Fetch students by classroom id
     */
    fun fetchStudentsByClassroomId(classroomId: Int): Flow<ApiResponse<ArrayList<StudentProfileResponse>>>

    /**
     * Fetch Blood groups
     */
    fun fetchBloodGroups():Flow<ApiResponse<ArrayList<BloodGroup>>>

    /**
     * Update student profile
     */
    fun updateStudentProfile(editedProfileData: StudentProfileResponse):Flow<ApiResponse<StudentProfileResponse>>

}