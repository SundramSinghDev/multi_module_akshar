package com.pronted.data.noticeboard

import com.pronted.domain.noticeboard.NoticeboardApi
import com.pronted.domain.noticeboard.NoticeboardRepository
import com.pronted.domain.timeline.TimelineApi
import com.pronted.domain.timeline.TimelineRepository
import com.pronted.dto.ApiResponse
import com.pronted.dto.noticeboard.NoticeBoard
import com.pronted.dto.noticeboard.NoticeBoardModel
import com.pronted.utils.extentions.handleApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NoticeboardRepositoryImpl(private val noticeboardApi: NoticeboardApi) :
    NoticeboardRepository {

    override fun fetchAllNotices(
        status: String
    ): Flow<ApiResponse<NoticeBoardModel>> = flow {
        emit(handleApi {
            noticeboardApi.fetchAllNotices(
                status = status
            )
        })
    }

    override fun deleteNotice(noticeId: Int): Flow<ApiResponse<Boolean>> = flow {
        emit(handleApi {
            noticeboardApi.deleteNotice(noticeId)
        })
    }

    override fun updateNotice(
        notice: NoticeBoard,
        isCreation: Boolean
    ): Flow<ApiResponse<NoticeBoard>> = flow {
        emit(handleApi {
            if(isCreation){
                noticeboardApi.createNotice(notice)
            }else
                noticeboardApi.updateNotice(notice.id, notice)
        })
    }
}