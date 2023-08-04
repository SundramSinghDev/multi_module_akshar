package com.pronted.presentation.noticeboard

import androidx.lifecycle.ViewModel
import com.pronted.domain.noticeboard.NoticeboardUseCase
import com.pronted.domain.timeline.TimelineUseCase
import com.pronted.dto.noticeboard.NoticeBoard
import com.pronted.dto.timeline.DeleteNoticeAction
import com.pronted.dto.timeline.EditNoticeAction
import com.pronted.dto.timeline.NoticeListAction
import kotlinx.coroutines.flow.Flow

class NoticeboardViewModel(private val noticeboardUseCase: NoticeboardUseCase) : ViewModel() {

    /**
     * Fetch Notice list
     */
    fun fetchAllNotices(status:String):
            Flow<NoticeListAction> = noticeboardUseCase.fetchAllNotices(status)

    /**
     * Delete Notice
     */
    fun deleteNotice(noticeId:Int):
            Flow<DeleteNoticeAction> = noticeboardUseCase.deleteNotice(noticeId)
    /**
     * Update Notice
     */
    fun updateNotice(noticeBoard: NoticeBoard, isCreation:Boolean):
            Flow<EditNoticeAction> = noticeboardUseCase.updateNotice(noticeBoard, isCreation)
}