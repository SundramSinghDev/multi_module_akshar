package com.pronted.presentation.userapps

import androidx.lifecycle.ViewModel
import com.pronted.domain.userapps.UserAppsUseCase
import com.pronted.dto.login.ImageAction
import com.pronted.dto.login.SchoolBoardAction
import com.pronted.dto.login.SecurityGroupAction
import com.pronted.dto.login.UserAccessAction
import kotlinx.coroutines.flow.Flow

class UserAppsViewModel (private val userAppUseCase: UserAppsUseCase) : ViewModel() {

    /**
     * Fetch user access info
     *
     * @param mobileNumber
     * @param appName
     */
    fun fetchUserAccessInfo(mobileNumber: String, appName: String):
            Flow<UserAccessAction> =
        userAppUseCase.fetchUserAccessInfo(mobileNumber, appName)

    fun fetchSchoolBoard():
            Flow<SchoolBoardAction> = userAppUseCase.fetchSchoolBoard()

}