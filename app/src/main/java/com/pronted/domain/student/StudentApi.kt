package com.pronted.domain.student

import com.pronted.dto.student.BloodGroup
import com.pronted.dto.student.ClassDropDownModel
import com.pronted.dto.student.StudentCount
import com.pronted.dto.student.StudentProfileResponse
import retrofit2.Response
import retrofit2.http.*

interface StudentApi {
    @GET("students/mobile/students/profile")
    suspend fun fetchStudentProfile(): Response<StudentProfileResponse>

    @GET("school/classrooms/dropdown")
    suspend fun fetchClassRoomDropdowns(
    ): Response<ArrayList<ClassDropDownModel>>

    @GET("students/mobile/students")
    suspend fun fetchStudentListByClassroomId(
        @Query("classroomId") classroomId: Int
    ): Response<ArrayList<StudentProfileResponse>>


    @GET("students/student-count")
    suspend fun fetchStudentCount(
        @HeaderMap headers: Map<String, String>?
    ): Response<StudentCount>

    @GET("lookup/?fieldName=BLOOD_GROUP")
    suspend fun fetchBloodGroups(
    ): Response<ArrayList<BloodGroup>>

    @PUT("students/mobile/students/{studentProfileId}")
    suspend fun updateStudentProfile(
        @Path("studentProfileId") studentProfileId: Int,
        @Body profile: StudentProfileResponse
    ):Response<StudentProfileResponse>
}