package com.pronted.domain.userapps

import com.pronted.dto.ApiResponse
import com.pronted.dto.login.BoardType
import com.pronted.dto.login.SchoolBoardAction
import com.pronted.dto.login.UserAppList
import kotlinx.coroutines.flow.Flow

/**
 * Timeline repository
 */
interface UserAppsRepository {
    /**
     * Fetch user access
     *
     * @param mobileNumber
     * @param appName
     */
    fun fetchUserAccessInfo(mobileNumber: String, appName: String):
            Flow<ApiResponse<ArrayList<UserAppList>>>

    /**
     * Fetch type of school
     */
    fun fetchSchoolBoard(): Flow<ApiResponse<BoardType>>
}