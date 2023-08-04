package com.pronted.domain.userapps

import com.pronted.dto.login.*
import kotlinx.coroutines.flow.Flow

/**
 * TIme line
 */
interface UserAppsUseCase {
    /**
     * Fetch User Access
     *
     * @param mobileNumber
     * @param appName
     * @return
     */
    fun fetchUserAccessInfo(mobileNumber: String, appName: String): Flow<UserAccessAction>

    /**
     * Fetch User Access
     */
    fun fetchSchoolBoard(): Flow<SchoolBoardAction>

}