package com.pronted.domain.images

import com.google.android.gms.common.api.Api
import com.pronted.dto.ApiResponse
import com.pronted.dto.images.ProfileImageResponse
import kotlinx.coroutines.flow.Flow

interface ImageAccessRepository {

    /**
     * Fetch employee image
     *
     * @param headers
     * @param module
     * @param profileId
     */
    fun fetchProfileImage(headers: Map<String, String>?, module: String, profileId: String):
            Flow<ApiResponse<ProfileImageResponse>>

    /**
     * Fetch security group list
     *
     * @param appName
     */
    fun fetchSecurityGroupList(appName: String):
            Flow<ApiResponse<ArrayList<String>>>

    /**
     * Fetch employee image
     *
     * @param headers
     * @param profileId
     * @param imageUrl
     */
    fun uploadProfileImage(
        headers: Map<String, String>?,
        profileId: String,
        imageUrl: String,
        module: String
    ): Flow<ApiResponse<ProfileImageResponse>>

}