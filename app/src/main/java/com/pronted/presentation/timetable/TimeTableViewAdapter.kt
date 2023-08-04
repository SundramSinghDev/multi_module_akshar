package com.pronted.presentation.timetable

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pronted.databinding.RowTimetableViewBinding
import com.pronted.dto.timeline.PeriodWiseTimetable
import java.util.*

class TimeTableViewAdapter(
    private val mContext: Context,
    private val isForEmployee:Boolean = false
) : RecyclerView.Adapter<TimeTableViewAdapter.ChildViewHolder>() {
    private lateinit var timeTablePeriodAdapter: TimeTablePeriodAdapter
    var list = arrayListOf<PeriodWiseTimetable>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {

        return ChildViewHolder(
            parent.context,
            RowTimetableViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bindData(list[position])
        holder.binding.run {
            rvTimetable.setHasFixedSize(true)
            rvTimetable.layoutManager = LinearLayoutManager(
                mContext,
                LinearLayoutManager.VERTICAL, false
            )
            timeTablePeriodAdapter =
                TimeTablePeriodAdapter(mContext,isForEmployee)
            timeTablePeriodAdapter.list = list[position].timetable
            rvTimetable.adapter = timeTablePeriodAdapter
        }

    }

    override fun getItemCount(): Int = list.size


    /**
     * Child view holder
     *
     * @property binding
     */
    class ChildViewHolder(private val context: Context, val binding: RowTimetableViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        data class ItemUIResource(
            val bgDrawable: Drawable?,
            val bgColor: Int
        )

        /**
         * Bind data
         *
         * @param schoolTask
         */
        fun bindData(timeTableItem: PeriodWiseTimetable) {
            binding.run {
                timeTableItem.timetable.let { list ->
                    if (list.size >= 1) {
                        val timeTable = list[0]
                        "${timeTable.startTime} - ${timeTable.endTime}".also { tvTime.text = it }
                    }
                }

            }
        }

    }

}