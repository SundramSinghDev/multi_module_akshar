package com.pronted.domain.student

import com.pronted.dto.login.ClassesAction
import com.pronted.dto.student.*
import kotlinx.coroutines.flow.Flow

/**
 * Image access
 */

interface StudentDataUseCase {
    /**
     * Fetch Student Profile
     *
     * @return
     */
    fun fetchStudentProfile(): Flow<StudentProfileAction>

    /**
     * Fetch available classes
     */
    fun fetchAvailableClasses(): Flow<ClassesAction>

    /**
     * Fetch available classes
     */
    fun fetchStudentCount(headers: Map<String, String>?): Flow<StudentsCountAction>

    /**
     * Fetch available classes
     */
    fun fetchStudentsByClassroomId(classroomId: Int): Flow<StudentsAction>

    /**
     * Fetch blood groups
     */
    fun fetchBloodGroups(): Flow<BloodGroupAction>

    /**
     * Update student profile
     */
    fun updateStudentProfile(editedProfileData: StudentProfileResponse):Flow<UpdateStudentProfileAction>
}