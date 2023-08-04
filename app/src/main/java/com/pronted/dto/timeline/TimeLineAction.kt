package com.pronted.dto.timeline

import com.pronted.dto.ErrorResponse
import com.pronted.dto.feepayments.data.FeesDetailModel
import com.pronted.dto.noticeboard.NoticeBoard
import com.pronted.dto.noticeboard.NoticeBoardModel

sealed class TimetableAction {
    class Success(val response: ArrayList<TimetableData>) : TimetableAction()

    class Fail(val errorResponse: ErrorResponse) : TimetableAction()
}

sealed class BirthdaysAction {
    class Success(val response: BirthdaysData) : BirthdaysAction()

    class Fail(val errorResponse: ErrorResponse) : BirthdaysAction()
}

sealed class FinanceSummaryAction {
    class Success(val response: FinanceModel) : FinanceSummaryAction()

    class Fail(val errorResponse: ErrorResponse) : FinanceSummaryAction()
}
sealed class FeeDetailsAction {
    class Success(val response: ArrayList<FeesDetailModel>) : FeeDetailsAction()

    class Fail(val errorResponse: ErrorResponse) : FeeDetailsAction()
}

sealed class CollectionSummaryAction {
    class Success(val response: ArrayList<FeePayment>) : CollectionSummaryAction()

    class Fail(val errorResponse: ErrorResponse) : CollectionSummaryAction()
}

sealed class ExpensesSummaryAction {
    class Success(val response: ArrayList<FeePayment>) : ExpensesSummaryAction()

    class Fail(val errorResponse: ErrorResponse) : ExpensesSummaryAction()
}

sealed class NoticeListAction {
    class Success(val response: NoticeBoardModel) : NoticeListAction()

    class Fail(val errorResponse: ErrorResponse) : NoticeListAction()
}

sealed class DeleteNoticeAction {
    class Success(val response: Boolean) : DeleteNoticeAction()

    class Fail(val errorResponse: ErrorResponse) : DeleteNoticeAction()
}

sealed class EditNoticeAction {
    class Success(val response: NoticeBoard) : EditNoticeAction()

    class Fail(val errorResponse: ErrorResponse) : EditNoticeAction()
}