package com.pronted.dto.student

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable


data class StudentContact (
    @SerializedName("studentProfileId")
    @Expose
    var studentProfileId: Int? = null,
    @SerializedName("address")
    var address: AddressModel? = null,
    @SerializedName("primaryParentName")
    @Expose
    var primaryParentName: String? = null,
    @SerializedName("primaryParentMobile")
    @Expose
    var primaryParentMobile: String? = null,
    @SerializedName("primaryParentEmail")
    @Expose
    var primaryParentEmail: String? = null,
    @SerializedName("primaryParentRelationship")
    @Expose
    var primaryParentRelationship: String? = null,
    @SerializedName("primaryParentMobilesList")
    @Expose
    var primaryParentMobilesList: ArrayList<String> = arrayListOf(),
    @SerializedName("primaryParentEmailsList")
    @Expose
    var primaryParentEmailsList: ArrayList<String> = arrayListOf()
) : Serializable