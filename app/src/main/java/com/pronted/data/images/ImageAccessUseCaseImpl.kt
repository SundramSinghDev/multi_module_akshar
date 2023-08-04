package com.pronted.data.images

import com.pronted.domain.images.ImageAccessRepository
import com.pronted.domain.images.ImageAccessUseCase
import com.pronted.dto.ApiResponse
import com.pronted.dto.ErrorResponse
import com.pronted.dto.login.ImageAction
import com.pronted.dto.login.SecurityGroupAction
import com.pronted.dto.login.UploadImageAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ImageAccessUseCaseImpl (private val imageAccessRepository: ImageAccessRepository) :
    ImageAccessUseCase {
    override fun fetchProfileImage(
        headers: Map<String, String>?, module: String, profileId: String
    ): Flow<ImageAction> = imageAccessRepository.fetchProfileImage(headers, module, profileId).map {
        when (it) {
            is ApiResponse.Success -> {
                ImageAction.Success(it.data)
            }
            is ApiResponse.Error -> {
                ImageAction.Fail(it.errorResponse)
            }
            is ApiResponse.Exception -> {
                ImageAction.Fail(ErrorResponse())
            }
        }
    }

    override fun fetchSecurityGroupList(
        appName: String
    ): Flow<SecurityGroupAction> = imageAccessRepository.fetchSecurityGroupList(appName).map {
        when (it) {
            is ApiResponse.Success -> {
                SecurityGroupAction.Success(it.data)
            }
            is ApiResponse.Error -> {
                SecurityGroupAction.Fail(it.errorResponse)
            }
            is ApiResponse.Exception -> {
                SecurityGroupAction.Fail(ErrorResponse())
            }
        }
    }

    override fun uploadProfileImage(
        headers: Map<String, String>?,
        profileId: String,
        imageUrl: String,
        module: String
    ): Flow<UploadImageAction> = imageAccessRepository.uploadProfileImage(headers, profileId, imageUrl, module).map {
        when (it) {
            is ApiResponse.Success -> {
                UploadImageAction.Success(it.data)
            }
            is ApiResponse.Error -> {
                UploadImageAction.Fail(it.errorResponse)
            }
            is ApiResponse.Exception -> {
                UploadImageAction.Fail(ErrorResponse())
            }
        }
    }
}