package com.pronted.dto.images

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileImageResponse(
    @SerializedName("URL")
    val imageUrl: String? = ""
) : Parcelable