package com.pronted.presentation.userapps

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pronted.domain.images.ImageAccessUseCase
import com.pronted.dto.login.ImageAction
import com.pronted.dto.login.SecurityGroupAction
import com.pronted.dto.login.UploadImageAction
import kotlinx.coroutines.flow.Flow

class ImagesViewModel(private val imageAccessUseCase: ImageAccessUseCase) : ViewModel() {

     private val mutableLiveDataSecurityGroups = MutableLiveData<ArrayList<String>>()

    /**
     * Fetch Image
     *
     * @param headers
     * @param module
     * @param profileId
     */
    fun fetchProfileImage(headers: Map<String, String>? = null, module: String, profileId: String):
            Flow<ImageAction> =
        imageAccessUseCase.fetchProfileImage(headers, module, profileId)

    /**
     * Fetch security group list
     */
    fun fetchSecurityGroupList(loginRole: String):
            Flow<SecurityGroupAction> = imageAccessUseCase.fetchSecurityGroupList(loginRole)

    /**
     * Fetch security group list
     */
    fun uploadProfilePhoto(headers: Map<String, String>? = null, profileId: String, imageUrl:String, module:String):
            Flow<UploadImageAction> = imageAccessUseCase.uploadProfileImage(headers, profileId, imageUrl, module)
}