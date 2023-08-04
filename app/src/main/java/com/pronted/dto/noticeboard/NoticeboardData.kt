package com.pronted.dto.noticeboard

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pronted.dto.timeline.Pageable
import com.pronted.dto.timeline.Sort
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoticeBoardModel(
    @SerializedName("content")
    @Expose
    val content: ArrayList<NoticeBoard> = arrayListOf(),
    @SerializedName("pageable")
    @Expose
    val pageable: Pageable?,
    @SerializedName("last")
    @Expose
    val last: Boolean = false,
    @SerializedName("totalPages")
    @Expose
    val totalPages: Int = 0,
    @SerializedName("totalElements")
    @Expose
    val totalElements: Int = 0,
    @SerializedName("sort")
    @Expose
    val sort: Sort?,
    @SerializedName("first")
    @Expose
    val first: Boolean = false,
    @SerializedName("number")
    @Expose
    val number: Int = 0,
    @SerializedName("numberOfElements")
    @Expose
    val numberOfElements: Int = 0,
    @SerializedName("size")
    @Expose
    val size: Int = 0,
    @SerializedName("empty")
    @Expose
    val empty: Boolean = false
) : Parcelable

@Parcelize
data class NoticeBoard(
    @SerializedName("id")
    @Expose
    var id: Int = 0,
    @SerializedName("schoolCd")
    @Expose
    var schoolCd: String? = "",
    @SerializedName("startDate")
    @Expose
    var startDate: String = "",
    @SerializedName("endDate")
    @Expose
    var endDate: String = "",

    @SerializedName("title")
    @Expose
    var title: String = "",

    @SerializedName("description")
    @Expose
    var description: String = "",

    @SerializedName("visibility")
    @Expose
    var visibility: String = "",

    @SerializedName("recordStatus")
    @Expose
    var recordStatus: String? = "",

    @SerializedName("auditFields")
    @Expose
    var auditFields: AuditFields?
) : Parcelable

@Parcelize
data class AuditFields(
    @SerializedName("createdBy")
    @Expose
    var createdBy: String = "",

    @SerializedName("updatedBy")
    @Expose
    var updatedBy: String = "",

    @SerializedName("createdTs")
    @Expose
    var createdTs: String = "",

    @SerializedName("updatedTs")
    @Expose
    var updatedTs: String = "",
) : Parcelable