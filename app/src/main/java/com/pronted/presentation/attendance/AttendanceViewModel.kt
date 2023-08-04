package com.pronted.presentation.attendance

import androidx.lifecycle.ViewModel
import com.pronted.domain.attendance.AttendanceUseCase
import com.pronted.dto.attendance.*
import com.pronted.dto.login.ClassesAction
import kotlinx.coroutines.flow.Flow

class AttendanceViewModel(private val attendanceUseCase: AttendanceUseCase) : ViewModel() {
    /**
     * Fetch Attendance
     */
    fun fetchAttendanceStatsByTime(fromDate: String, toDate: String, profileId: Int):
            Flow<AttendanceChartAction> =
        attendanceUseCase.fetchAttendanceStatsByTime(fromDate, toDate, profileId)

    /**
     * Fetch Attendance yearly
     */
    fun fetchAttendanceStatsByYearly(profileId: Int):
            Flow<AttendanceChartAction> = attendanceUseCase.fetchAttendanceStatsByYearly(profileId)

    /**
     * Fetch Attendance yearly
     */
    fun fetchTodayAttendance(profileId: Int):
            Flow<AttendanceAction> = attendanceUseCase.fetchTodayAttendance(profileId)

    /**
     * Fetch available classes
     */
    fun fetchAvailableClasses():
            Flow<ClassesAction> = attendanceUseCase.fetchAvailableClasses()

    /**
     * Fetch shifts
     *
     * @param profileType
     * @param classRoomId
     * @return
     */
    fun fetchShifts(profileType: String, classRoomId: Int): Flow<ShiftAction> =
        attendanceUseCase.fetchShifts(profileType, classRoomId)

    /**
     * Fetch students attendance
     *
     * @param classRoomId
     * @param shiftId
     * @param date
     */
    fun fetchStudentsAttendance(classRoomId: Int, shiftId: Int, date: String) =
        attendanceUseCase.fetchStudentsAttendance(classRoomId, shiftId, date)

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
    ): Flow<SaveStudentsAttendanceAction> =
        attendanceUseCase.saveStudentsAttendance(profileType, classRoomId, attendanceModelList)

    /**
     * Fetch employees attendance
     *
     * @param shiftId
     * @param date
     */
    fun fetchEmployeesAttendance(shiftId: Int, date: String) =
        attendanceUseCase.fetchEmployeesAttendance(shiftId, date)

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
    ): Flow<SaveEmployeesAttendanceAction> =
        attendanceUseCase.saveEmployeesAttendance(profileType, attendanceModelList)
}