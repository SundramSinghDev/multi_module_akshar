package com.pronted.data.attendance

import com.pronted.domain.attendance.AttendanceApi
import com.pronted.domain.attendance.AttendanceRepository
import com.pronted.dto.ApiResponse
import com.pronted.dto.attendance.AttendanceChart
import com.pronted.dto.attendance.AttendanceModel
import com.pronted.dto.attendance.ShiftResponseModel
import com.pronted.utils.extentions.handleApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AttendanceRepositoryImpl(private val attendanceApi: AttendanceApi) : AttendanceRepository {
    override fun fetchAttendanceStatsByTime(
        fromDate: String,
        toDate: String,
        profileId: Int
    ): Flow<ApiResponse<ArrayList<AttendanceChart>>> = flow {
        emit(handleApi {
            attendanceApi.fetchAttendanceStatsByTime(
                fromDate = fromDate,
                toDate = toDate,
                studentProfileId = profileId
            )
        })
    }

    override fun fetchAttendanceStatsByYearly(profileId: Int): Flow<ApiResponse<ArrayList<AttendanceChart>>> =
        flow {
            emit(handleApi {
                attendanceApi.fetchAttendanceStatsByYearly(
                    studentProfileId = profileId
                )
            })
        }

    override fun fetchTodayAttendance(profileId: Int): Flow<ApiResponse<AttendanceModel>> = flow {
        emit(handleApi {
            attendanceApi.fetchTodayAttendance(
                studentProfileId = profileId
            )
        })
    }

    override fun fetchShift(
        profileType: String,
        classRoomId: Int
    ): Flow<ApiResponse<ArrayList<ShiftResponseModel>>> = flow {
        emit(handleApi {
            attendanceApi.fetchShift(profileType, if (classRoomId != 0) classRoomId else null)
        })
    }

    override fun fetchStudentsAttendance(
        classRoomId: Int,
        shiftId: Int,
        date: String
    ): Flow<ApiResponse<ArrayList<AttendanceModel>>> = flow {
        emit(handleApi { attendanceApi.fetchStudentsAttendance(classRoomId, shiftId, date) })
    }

    override fun saveStudentsAttendance(
        profileType: String,
        classRoomId: Int,
        attendanceModelList: List<AttendanceModel>
    ): Flow<ApiResponse<ArrayList<AttendanceModel>>> = flow {
        emit(handleApi {
            attendanceApi.saveStudentsAttendance(profileType, classRoomId, attendanceModelList)
        })
    }

    override fun fetchEmployeesAttendance(
        shiftId: Int,
        date: String
    ): Flow<ApiResponse<ArrayList<AttendanceModel>>> = flow {
        emit(handleApi { attendanceApi.fetchEmployeesAttendance(shiftId, date) })
    }

    override fun saveEmployeesAttendance(
        profileType: String,
        attendanceModelList: List<AttendanceModel>
    ): Flow<ApiResponse<ArrayList<AttendanceModel>>> = flow {
        emit(handleApi {
            attendanceApi.saveEmployeesAttendance(profileType, attendanceModelList)
        })
    }
}