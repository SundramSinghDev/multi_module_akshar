package com.pronted.domain.noticeboard

import com.pronted.dto.noticeboard.NoticeBoard
import com.pronted.dto.timeline.*
import kotlinx.coroutines.flow.Flow

/**
 * Image access
 */

interface NoticeboardUseCase {

    /**
     * Fetch notice list
     *@param status
     * @return
     */
    fun fetchAllNotices(status:String):Flow<NoticeListAction>


    /**
     * Delete Notice
     */
    fun deleteNotice(noticeId:Int) : Flow<DeleteNoticeAction>

    /**
     * Create / Edit Notice
     */
    fun updateNotice(noticeBoard: NoticeBoard, creation: Boolean): Flow<EditNoticeAction>
}