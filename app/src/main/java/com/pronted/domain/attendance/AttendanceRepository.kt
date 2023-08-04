package com.pronted.domain.attendance

import com.pronted.dto.ApiResponse
import com.pronted.dto.attendance.AttendanceChart
import com.pronted.dto.attendance.AttendanceModel
import com.pronted.dto.attendance.ShiftResponseModel
import kotlinx.coroutines.flow.Flow

interface AttendanceRepository {
    /**
     * Fetch attendance by date
     *
     */
    fun fetchAttendanceStatsByTime(
        fromDate: String,
        toDate: String,
        profileId: Int
    ): Flow<ApiResponse<ArrayList<AttendanceChart>>>

    /**
     * Fetch attendance by yearly
     *
     * @param profileId
     *
     * @return
     */
    fun fetchAttendanceStatsByYearly(profileId: Int): Flow<ApiResponse<ArrayList<AttendanceChart>>>

    /**
     * Fetch attendance for today
     *
     * @param profileId
     *
     * @return
     */
    fun fetchTodayAttendance(profileId: Int): Flow<ApiResponse<AttendanceModel>>

    /**
     * Fetch shift
     *
     * @param profileType
     * @param classRoomId
     * @return
     */
    fun fetchShift(
        profileType: String,
        classRoomId: Int
    ): Flow<ApiResponse<ArrayList<ShiftResponseModel>>>

    /**
     * Fetch students attendance
     *
     * @param classRoomId
     * @param shiftId
     * @param date
     * @return
     */
    fun fetchStudentsAttendance(
        classRoomId: Int,
        shiftId: Int,
        date: String
    ): Flow<ApiResponse<ArrayList<AttendanceModel>>>

    /**
     * Save students attendance
     *
     * @param profileType
     * @param classRoomId
     * @param attendanceModelList
     * @return
     */
    fun saveStudentsAttendance(
        profileType: String,
        classRoomId: Int,
        attendanceModelList: List<AttendanceModel>
    ): Flow<ApiResponse<ArrayList<AttendanceModel>>>

    /**
     * Fetch employees attendance
     *
     * @param shiftId
     * @param date
     * @return
     */
    fun fetchEmployeesAttendance(
        shiftId: Int,
        date: String
    ): Flow<ApiResponse<ArrayList<AttendanceModel>>>

    /**
     * Save employees attendance
     *
     * @param profileType
     * @param attendanceModelList
     * @return
     */
    fun saveEmployeesAttendance(
        profileType: String,
        attendanceModelList: List<AttendanceModel>
    ): Flow<ApiResponse<ArrayList<AttendanceModel>>>
}