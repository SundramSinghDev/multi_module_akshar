package com.pronted.domain.images

import com.pronted.dto.login.ImageAction
import com.pronted.dto.login.SecurityGroupAction
import com.pronted.dto.login.UploadImageAction
import kotlinx.coroutines.flow.Flow

/**
 * Image access
 */

interface ImageAccessUseCase {
    /**
     * Fetch image
     *
     * @param headers
     * @param module
     * @param profileId
     * @return
     */
    fun fetchProfileImage(headers: Map<String, String>?, module: String, profileId: String) : Flow<ImageAction>


    /**
     * Fetch security group list
     *
     * @param appName
     * @return
     */
    fun fetchSecurityGroupList(appName:String): Flow<SecurityGroupAction>

    /**
     * Fetch image
     *
     * @param headers
     * @param profileId
     * @param imageUrl
     * @return
     */
    fun uploadProfileImage(headers: Map<String, String>?, profileId: String, imageUrl: String, module: String) : Flow<UploadImageAction>
}