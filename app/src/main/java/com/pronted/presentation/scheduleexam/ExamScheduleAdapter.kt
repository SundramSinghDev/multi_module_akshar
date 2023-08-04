package com.pronted.presentation.scheduleexam

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aitsuki.swipe.SwipeLayout
import com.pronted.R
import com.pronted.databinding.RowExamScheduleBinding
import com.pronted.dto.exam.ScheduleModel
import com.pronted.presentation.listener.ScheduleExamListener
import com.pronted.utils.DateUtil
import com.pronted.utils.convertToDate
import com.pronted.utils.toDateString

class ExamScheduleAdapter(private val isParent: Boolean = false , private val scheduleExamListener: ScheduleExamListener<ScheduleModel>? = null) :
    RecyclerView.Adapter<ExamScheduleAdapter.ChildViewHolder>() {

    var list = arrayListOf<ScheduleModel>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            parent.context,
            RowExamScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.run {
            bindData(list[position])
            binding.swipeLayout.addListener(object : SwipeLayout.Listener {

                override fun onSwipe(menuView: View, swipeOffset: Float) {
                    super.onSwipe(menuView, swipeOffset)
                }
            })
            if(isParent) {
                binding.swipeLayout.swipeFlags = 0 //Disable swipe
            }
            binding.tvEdit.setOnClickListener {
                scheduleExamListener?.onEdit(list[position], position)
            }

            binding.tvDelete.setOnClickListener {
                scheduleExamListener?.onDelete(list[position], position)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    inner class ChildViewHolder(private val context: Context, val binding: RowExamScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /**
         * Bind data
         *
         * @param testModel
         */
        fun bindData(scheduleModel: ScheduleModel) {
            binding.run {

                val examSubject = when (scheduleModel.subjectName) {
                    ExamSubject.TELUGU.subject -> ExamSubject.TELUGU
                    ExamSubject.HINDI.subject -> ExamSubject.HINDI
                    ExamSubject.ENGLISH.subject -> ExamSubject.ENGLISH
                    ExamSubject.MATHS.subject -> ExamSubject.MATHS
                    ExamSubject.SCIENCE.subject -> ExamSubject.SCIENCE
                    ExamSubject.ENVIRONMENTAL_STUDIES.subject -> ExamSubject.ENVIRONMENTAL_STUDIES
                    ExamSubject.BIOLOGY.subject -> ExamSubject.BIOLOGY
                    ExamSubject.PHYSICS.subject -> ExamSubject.PHYSICS
                    ExamSubject.SOCIAL_STUDIES.subject -> ExamSubject.SOCIAL_STUDIES
                    ExamSubject.CIVICS.subject -> ExamSubject.CIVICS
                    ExamSubject.COMMERCE.subject -> ExamSubject.COMMERCE
                    ExamSubject.ECONOMICS.subject -> ExamSubject.ECONOMICS
                    ExamSubject.CHEMISTRY.subject -> ExamSubject.CHEMISTRY
                    else -> ExamSubject.OTHER
                }

                llSubject.backgroundTintList =
                    context.getColorStateList(examSubject.backgroundColor)
                ivSubject.setImageResource(examSubject.src)
                tvSubjectName.text = scheduleModel.subjectName
                tvDuration.text = context.getString(R.string.format_mins).format(scheduleModel.duration)
                tvTime.text =
                    scheduleModel.startTime.convertToDate(DateUtil.H2m2)?.toDateString(DateUtil.h2m2a)
                tvDate.text = scheduleModel.date.convertToDate(DateUtil.y4M2d2)
                    ?.toDateString(DateUtil.m3D2Y4)
            }
        }
    }
}