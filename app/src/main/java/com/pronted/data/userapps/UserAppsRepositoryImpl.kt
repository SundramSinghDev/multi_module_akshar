package com.pronted.data.userapps

import com.base.Preference
import com.pronted.domain.userapps.UserAppsApi
import com.pronted.domain.userapps.UserAppsRepository
import com.pronted.dto.ApiResponse
import com.pronted.dto.login.BoardType
import com.pronted.dto.login.UserAppList
import com.pronted.utils.extentions.DYNAMIC_HEADERS
import com.pronted.utils.extentions.handleApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserAppsRepositoryImpl(private val userAppsApi: UserAppsApi) : UserAppsRepository {
    override fun fetchUserAccessInfo(
        mobileNumber: String,
        appName: String
    ): Flow<ApiResponse<ArrayList<UserAppList>>> = flow {
        emit(handleApi {
            userAppsApi.fetchUserAccessInfo(
                mobileNumber = mobileNumber,
                appName = appName
            )
        })
    }

    override fun fetchSchoolBoard(): Flow<ApiResponse<BoardType>> = flow {
        emit(handleApi {
            userAppsApi.fetchClassRoomType()
        })
    }
}