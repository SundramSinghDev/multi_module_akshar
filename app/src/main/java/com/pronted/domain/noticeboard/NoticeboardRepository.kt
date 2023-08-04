package com.pronted.domain.noticeboard

import com.pronted.dto.ApiResponse
import com.pronted.dto.noticeboard.NoticeBoard
import com.pronted.dto.noticeboard.NoticeBoardModel
import com.pronted.dto.timeline.DeleteNoticeAction
import kotlinx.coroutines.flow.Flow

interface NoticeboardRepository {

    /**
     * Fetch notice list
     *
     */
    fun fetchAllNotices(status: String):
            Flow<ApiResponse<NoticeBoardModel>>

    /**
     * Delete Notice
     */
    fun deleteNotice(noticeId: Int): Flow<ApiResponse<Boolean>>

    /**
     * Create/Edit Notice
     */
    fun updateNotice(notice: NoticeBoard, isCreation: Boolean): Flow<ApiResponse<NoticeBoard>>
}