package com.pronted.data.userapps

import com.pronted.domain.userapps.UserAppsRepository
import com.pronted.domain.userapps.UserAppsUseCase
import com.pronted.dto.ApiResponse
import com.pronted.dto.ErrorResponse
import com.pronted.dto.login.SchoolBoardAction
import com.pronted.dto.login.SecurityGroupAction
import com.pronted.dto.login.UserAccessAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserAppsUseCaseImpl(private val userAppsRepository: UserAppsRepository) : UserAppsUseCase {
    override fun fetchUserAccessInfo(
        mobileNumber: String,
        appName: String
    ): Flow<UserAccessAction> = userAppsRepository.fetchUserAccessInfo(mobileNumber, appName).map {
        when (it) {
            is ApiResponse.Success -> {
                UserAccessAction.Success(it.data)
            }
            is ApiResponse.Error -> {
                UserAccessAction.Fail(it.errorResponse)
            }
            is ApiResponse.Exception -> {
                UserAccessAction.Fail(ErrorResponse())
            }
        }
    }

    override fun fetchSchoolBoard(): Flow<SchoolBoardAction> =
        userAppsRepository.fetchSchoolBoard().map {
            when (it) {
                is ApiResponse.Success -> {
                    SchoolBoardAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    SchoolBoardAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    SchoolBoardAction.Fail(ErrorResponse())
                }
            }
        }
}