package com.pronted.domain.attendance

import com.pronted.dto.attendance.*
import com.pronted.dto.login.ClassesAction
import kotlinx.coroutines.flow.Flow

interface AttendanceUseCase {

    /**
     * Fetch available classes
     */
    fun fetchAvailableClasses(): Flow<ClassesAction>

    /**
     * Fetch attendance by date
     *
     * @param fromDate
     * @param toDate
     * @param profileId
     *
     * @return
     */
    fun fetchAttendanceStatsByTime(
        fromDate: String,
        toDate: String,
        profileId: Int
    ): Flow<AttendanceChartAction>

    /**
     * Fetch attendance by yearly
     *
     * @param profileId
     *
     * @return
     */
    fun fetchAttendanceStatsByYearly(profileId: Int): Flow<AttendanceChartAction>

    /**
     * Fetch attendance for today
     *
     * @param profileId
     *
     * @return
     */
    fun fetchTodayAttendance(profileId: Int): Flow<AttendanceAction>

    /**
     * Fetch shifts
     *
     * @param profileType
     * @param classroomId
     * @return
     */
    fun fetchShifts(profileType: String, classroomId: Int): Flow<ShiftAction>

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
    ): Flow<StudentsAttendanceAction>

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
    ): Flow<SaveStudentsAttendanceAction>

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
    ): Flow<EmployeesAttendanceAction>

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
    ): Flow<SaveEmployeesAttendanceAction>
}