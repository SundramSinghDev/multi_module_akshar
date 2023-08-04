package com.pronted.data.images

import com.base.Preference
import com.pronted.domain.images.ImageAccessApi
import com.pronted.domain.images.ImageAccessRepository
import com.pronted.dto.ApiResponse
import com.pronted.dto.images.ProfileImageResponse
import com.pronted.utils.extentions.DYNAMIC_HEADERS
import com.pronted.utils.extentions.handleApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ImageAccessRepositoryImpl(private val imageAccessApi: ImageAccessApi) :
    ImageAccessRepository {
    override fun fetchProfileImage(
        headers: Map<String, String>?,
        module: String,
        profileId: String
    ): Flow<ApiResponse<ProfileImageResponse>> = flow {
        emit(handleApi {
            if (headers == null) {
                imageAccessApi.fetchImage(
                    module = module,
                    profileId = profileId
                )
            } else {
                imageAccessApi.fetchImage(
                    headers = headers,
                    module = module,
                    profileId = profileId
                )
            }
        })
    }

    override fun fetchSecurityGroupList(appName: String): Flow<ApiResponse<ArrayList<String>>> =
        flow {
            emit(
                handleApi {
                    imageAccessApi.fetchSecurityGroupList(appName)
                }
            )
        }

    override fun uploadProfileImage(
        headers: Map<String, String>?,
        profileId: String,
        imageUrl: String,
        module: String
    ): Flow<ApiResponse<ProfileImageResponse>> = flow {
        emit(handleApi {
            imageAccessApi.uploadImage(headers, module, profileId, imageUrl)
        })
    }
}