package com.pronted.data.attendance

import com.pronted.domain.attendance.AttendanceRepository
import com.pronted.domain.attendance.AttendanceUseCase
import com.pronted.domain.student.StudentDataRepository
import com.pronted.dto.ApiResponse
import com.pronted.dto.ErrorResponse
import com.pronted.dto.attendance.*
import com.pronted.dto.login.ClassesAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AttendanceUseCaseImpl(
    private val attendanceRepository: AttendanceRepository,
    private val studentDataRepository: StudentDataRepository
) : AttendanceUseCase {
    override fun fetchAttendanceStatsByTime(
        fromDate: String,
        toDate: String,
        profileId: Int
    ): Flow<AttendanceChartAction> =
        attendanceRepository.fetchAttendanceStatsByTime(fromDate, toDate, profileId).map {
            when (it) {
                is ApiResponse.Success -> {
                    AttendanceChartAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    AttendanceChartAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    AttendanceChartAction.Fail(ErrorResponse())
                }
            }
        }

    override fun fetchAttendanceStatsByYearly(profileId: Int): Flow<AttendanceChartAction> =
        attendanceRepository.fetchAttendanceStatsByYearly(profileId).map {
            when (it) {
                is ApiResponse.Success -> {
                    AttendanceChartAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    AttendanceChartAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    AttendanceChartAction.Fail(ErrorResponse())
                }
            }
        }

    override fun fetchTodayAttendance(profileId: Int): Flow<AttendanceAction> =
        attendanceRepository.fetchTodayAttendance(profileId).map {
            when (it) {
                is ApiResponse.Success -> {
                    AttendanceAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    AttendanceAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    AttendanceAction.Fail(ErrorResponse())
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

    override fun fetchShifts(profileType: String, classroomId: Int): Flow<ShiftAction> =
        attendanceRepository.fetchShift(profileType, classroomId).map {
            when (it) {
                is ApiResponse.Success -> {
                    ShiftAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    ShiftAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    ShiftAction.Fail(ErrorResponse())
                }
            }
        }

    override fun fetchStudentsAttendance(
        classRoomId: Int,
        shiftId: Int,
        date: String
    ): Flow<StudentsAttendanceAction> =
        attendanceRepository.fetchStudentsAttendance(classRoomId, shiftId, date).map {
            when (it) {
                is ApiResponse.Success -> {
                    StudentsAttendanceAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    StudentsAttendanceAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    StudentsAttendanceAction.Fail(ErrorResponse())
                }
            }
        }

    override fun saveStudentsAttendance(
        profileType: String,
        classRoomId: Int,
        attendanceModelList: List<AttendanceModel>
    ): Flow<SaveStudentsAttendanceAction> =
        attendanceRepository.saveStudentsAttendance(profileType, classRoomId, attendanceModelList)
            .map {
                when (it) {
                    is ApiResponse.Success -> {
                        SaveStudentsAttendanceAction.Success(it.data)
                    }
                    is ApiResponse.Error -> {
                        SaveStudentsAttendanceAction.Fail(it.errorResponse)
                    }
                    is ApiResponse.Exception -> {
                        SaveStudentsAttendanceAction.Fail(ErrorResponse())
                    }
                }
            }

    override fun fetchEmployeesAttendance(
        shiftId: Int,
        date: String
    ): Flow<EmployeesAttendanceAction> =
        attendanceRepository.fetchEmployeesAttendance(shiftId, date).map {
            when (it) {
                is ApiResponse.Success -> {
                    EmployeesAttendanceAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    EmployeesAttendanceAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    EmployeesAttendanceAction.Fail(ErrorResponse())
                }
            }
        }

    override fun saveEmployeesAttendance(
        profileType: String,
        attendanceModelList: List<AttendanceModel>
    ): Flow<SaveEmployeesAttendanceAction> =
        attendanceRepository.saveEmployeesAttendance(profileType, attendanceModelList)
            .map {
                when (it) {
                    is ApiResponse.Success -> {
                        SaveEmployeesAttendanceAction.Success(it.data)
                    }
                    is ApiResponse.Error -> {
                        SaveEmployeesAttendanceAction.Fail(it.errorResponse)
                    }
                    is ApiResponse.Exception -> {
                        SaveEmployeesAttendanceAction.Fail(ErrorResponse())
                    }
                }
            }
}