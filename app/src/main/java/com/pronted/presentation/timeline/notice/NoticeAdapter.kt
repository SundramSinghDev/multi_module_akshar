package com.pronted.presentation.timeline.notice

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.pronted.R
import com.pronted.databinding.RowNoticeBoardBinding
import com.pronted.dto.noticeboard.NoticeBoard
import com.pronted.utils.DateUtil
import com.pronted.utils.convertToDate
import com.pronted.utils.toDateString

class NoticeAdapter(
    private val mContext: Context,
    private val showNotice: Boolean = false
) : RecyclerView.Adapter<NoticeAdapter.ChildViewHolder>() {
    var list = arrayListOf<NoticeBoard>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            parent.context,
            RowNoticeBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bindData(list[holder.bindingAdapterPosition])
        holder.binding.showNoticeDate = showNotice
    }

    override fun getItemCount(): Int = list.size


    /**
     * Child view holder
     *
     * @property binding
     */
    class ChildViewHolder(private val context: Context, val binding: RowNoticeBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind data
         *
         * @param noticeModel
         */
        fun bindData(noticeModel: NoticeBoard) {
            binding.run {
                this.notice = noticeModel
                val startDate = noticeModel.startDate.convertToDate(DateUtil.y4M2d2)
                    ?.toDateString(DateUtil.m3D2Y4)
                val endDate = noticeModel.endDate.convertToDate(DateUtil.y4M2d2)
                    ?.toDateString(DateUtil.m3D2Y4)
                tvNoticeDate.text =
                    String.format(context.getString(R.string.from_to_date), startDate, endDate)
                when (bindingAdapterPosition % 3) {
                    0 -> {
                        clParent.background = ResourcesCompat.getDrawable(context.resources, R.drawable.notice_blue_bg, context.theme)
                    }
                    1 -> {
                        clParent.background = ResourcesCompat.getDrawable(context.resources, R.drawable.notice_pink_bg, context.theme)
                    }
                    else -> {
                        clParent.background = ResourcesCompat.getDrawable(context.resources, R.drawable.notice_purple_bg, context.theme)
                    }

                }
            }
        }
    }

}