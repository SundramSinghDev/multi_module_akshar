package com.pronted.presentation.noticeboard

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.pronted.R
import com.pronted.databinding.RowNoticeBoardBinding
import com.pronted.databinding.RowNoticeViewBinding
import com.pronted.dto.noticeboard.NoticeBoard
import com.pronted.presentation.listener.NoticeListener
import com.pronted.utils.DateUtil
import com.pronted.utils.convertToDate
import com.pronted.utils.extentions.SECURITY_GROUP_LIST
import com.pronted.utils.toDateString
import io.paperdb.Paper

class NoticeViewAdapter(
    private val mContext: Context,
    private val tabPosition: Int = 0,
    private val noticeListener: NoticeListener<NoticeBoard>
) : RecyclerView.Adapter<NoticeViewAdapter.ChildViewHolder>() {
    var selectedItemPos = -1
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var list = arrayListOf<NoticeBoard>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            parent.context,
            RowNoticeViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bindData(list[holder.bindingAdapterPosition], tabPosition)
        holder.binding.run {

            when (selectedItemPos) {
                holder.absoluteAdapterPosition -> {
                    Paper.book().read<ArrayList<String>>(SECURITY_GROUP_LIST)
                        ?.let { securityList ->
                            if(tabPosition == 1){
                                tvEditNotice.visibility = View.GONE
                            }
                            if (securityList.contains("ME_NOTICE_BOARD_DELETE") && securityList.contains(
                                    "ME_NOTICE_BOARD_EDIT"
                                )
                            ) {
                                clEditLayout.visibility = View.VISIBLE
                            } else if (securityList.contains("ME_NOTICE_BOARD_DELETE")) {
                                tvEditNotice.visibility = View.GONE
                                tvDeleteNotice.visibility = View.VISIBLE
                                clEditLayout.visibility = View.VISIBLE
                            } else if (securityList.contains("ME_NOTICE_BOARD_EDIT")) {
                                tvEditNotice.visibility = View.VISIBLE
                                tvDeleteNotice.visibility = View.GONE
                                clEditLayout.visibility = View.VISIBLE
                            } else {
                                clEditLayout.visibility = View.GONE
                            }

                        }
                }
                else -> {
                    clEditLayout.visibility = View.GONE
                }
            }

            clNotice.setOnLongClickListener {
                notifyItemChanged(selectedItemPos)
                selectedItemPos = holder.absoluteAdapterPosition
                notifyItemChanged(selectedItemPos)
                true
            }
            imgCancel.setOnClickListener {
                clEditLayout.visibility = View.GONE
            }
            tvEditNotice.setOnClickListener {
                noticeListener.onEdit(list[holder.bindingAdapterPosition])
            }
            tvDeleteNotice.setOnClickListener {
                noticeListener.onDelete(list[holder.bindingAdapterPosition])
            }
        }

    }


    override fun getItemCount(): Int = list.size


    /**
     * Child view holder
     *
     * @property binding
     */
    class ChildViewHolder(private val context: Context, val binding: RowNoticeViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind data
         *
         * @param noticeModel
         */
        fun bindData(noticeModel: NoticeBoard, tabPosition: Int) {
            binding.run {
                if (tabPosition == 1) {
                    clParent.background = ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.notice_grey_bg,
                        context.theme
                    )
                }else {
                    when (bindingAdapterPosition % 3) {
                        0 -> {
                            clParent.background = ResourcesCompat.getDrawable(
                                context.resources,
                                R.drawable.notice_blue_bg,
                                context.theme
                            )
                        }
                        1 -> {
                            clParent.background = ResourcesCompat.getDrawable(
                                context.resources,
                                R.drawable.notice_pink_bg,
                                context.theme
                            )
                        }
                        else -> {
                            clParent.background = ResourcesCompat.getDrawable(
                                context.resources,
                                R.drawable.notice_purple_bg,
                                context.theme
                            )
                        }

                    }
                }
                this.notice = noticeModel
                val startDate = noticeModel.startDate.convertToDate(DateUtil.y4M2d2)
                    ?.toDateString(DateUtil.m3D2Y4)
                val endDate = noticeModel.endDate.convertToDate(DateUtil.y4M2d2)
                    ?.toDateString(DateUtil.m3D2Y4)
                tvNoticeDate.text =
                    String.format(context.getString(R.string.from_to_date), startDate, endDate)
            }
        }
    }

}