package com.pronted.data.noticeboard

import com.anychart.core.ui.Timeline
import com.pronted.domain.noticeboard.NoticeboardRepository
import com.pronted.domain.noticeboard.NoticeboardUseCase
import com.pronted.domain.timeline.TimelineRepository
import com.pronted.domain.timeline.TimelineUseCase
import com.pronted.domain.userapps.UserAppsRepository
import com.pronted.domain.userapps.UserAppsUseCase
import com.pronted.dto.ApiResponse
import com.pronted.dto.ErrorResponse
import com.pronted.dto.login.SecurityGroupAction
import com.pronted.dto.login.UserAccessAction
import com.pronted.dto.noticeboard.NoticeBoard
import com.pronted.dto.timeline.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoticeboardUseCaseImpl(private val noticeboardRepository: NoticeboardRepository) : NoticeboardUseCase {

    override fun fetchAllNotices(status: String): Flow<NoticeListAction> =
        noticeboardRepository.fetchAllNotices(status).map {
            when (it) {
                is ApiResponse.Success -> {
                    NoticeListAction.Success(it.data)
                }
                is ApiResponse.Error -> {
                    NoticeListAction.Fail(it.errorResponse)
                }
                is ApiResponse.Exception -> {
                    NoticeListAction.Fail(ErrorResponse())
                }
            }
        }

    override fun deleteNotice(noticeId: Int): Flow<DeleteNoticeAction>  = noticeboardRepository.deleteNotice(noticeId).map {
        when (it) {
            is ApiResponse.Success -> {
                DeleteNoticeAction.Success(it.data)
            }
            is ApiResponse.Error -> {
                DeleteNoticeAction.Fail(it.errorResponse)
            }
            is ApiResponse.Exception -> {
                DeleteNoticeAction.Fail(ErrorResponse())
            }
        }
    }

    override fun updateNotice(noticeBoard: NoticeBoard, creation: Boolean): Flow<EditNoticeAction> = noticeboardRepository.updateNotice(noticeBoard, creation).map {
        when (it) {
            is ApiResponse.Success -> {
                EditNoticeAction.Success(it.data)
            }
            is ApiResponse.Error -> {
                EditNoticeAction.Fail(it.errorResponse)
            }
            is ApiResponse.Exception -> {
                EditNoticeAction.Fail(ErrorResponse())
            }
        }
    }
}