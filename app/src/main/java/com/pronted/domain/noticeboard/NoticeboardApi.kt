package com.pronted.domain.noticeboard

import com.pronted.dto.noticeboard.NoticeBoard
import com.pronted.dto.noticeboard.NoticeBoardModel
import com.pronted.dto.student.BloodGroup
import com.pronted.dto.student.ClassDropDownModel
import com.pronted.dto.student.StudentCount
import com.pronted.dto.student.StudentProfileResponse
import retrofit2.Response
import retrofit2.http.*

interface NoticeboardApi {
    @GET("message-center/noticeboard")
    suspend fun fetchAllNotices(
        @Query("status") status: String
    ): Response<NoticeBoardModel>

    @DELETE("message-center/noticeboard/{id}")
    suspend fun deleteNotice(
        @Path("id") id: Int
    ): Response<Boolean>

    @PUT("message-center/noticeboard/{scheduleId}")
    suspend fun updateNotice(
        @Path("scheduleId") scheduleId: Int,
        @Body jsonObject: NoticeBoard
    ): Response<NoticeBoard>

    @POST("message-center/noticeboard")
    suspend fun createNotice(
        @Body jsonObject: NoticeBoard
    ): Response<NoticeBoard>
}