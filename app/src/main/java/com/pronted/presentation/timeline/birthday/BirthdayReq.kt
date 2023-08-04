package com.pronted.presentation.timeline.birthday

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BirthdayReq(
    val fromDate: String?,
    val toDate: String?,
    val page: Int,
    val title: String
) : Parcelable
