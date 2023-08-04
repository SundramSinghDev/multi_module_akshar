package com.pronted.presentation.timeline

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.text.format.Time
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.anychart.core.Base
import com.base.BaseActivity
import com.pronted.R
import com.pronted.databinding.RowTimetableBinding
import com.pronted.dto.login.UserAppList
import com.pronted.dto.timeline.PeriodWiseTimetable
import com.pronted.dto.timeline.TimetableData
import com.pronted.utils.extentions.SMART_PARENT
import com.pronted.utils.extentions.USER_SELECTED_ROLE
import io.paperdb.Paper
import java.util.*

class TimeTableAdapter(
    private val mContext: Context
) : RecyclerView.Adapter<TimeTableAdapter.ChildViewHolder>() {
    private var screenWidth = 0
    var list = arrayListOf<PeriodWiseTimetable>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        screenWidth = Resources.getSystem().displayMetrics.widthPixels;
        return ChildViewHolder(
            parent.context,
            RowTimetableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bindData(list[position])
        if (list.size > 1) {
            val itemWidth = screenWidth / 1.20

            val lp = holder.binding.cvTimetable.layoutParams
            lp.height = lp.height
            lp.width = itemWidth.toInt()
            holder.binding.cvTimetable.layoutParams = lp
        }

        /* holder.binding.ivSort.setOnTouchListener { view, motionEvent ->
             if (motionEvent?.action == MotionEvent.ACTION_DOWN) {
                 mStartDragListener.requestDrag(holder);
             }
             false
         }
         holder.binding.clMyDayTask.setOnClickListener {
             mStartDragListener.onClick(list[position])
         }*/
    }

    override fun getItemCount(): Int = list.size


    /**
     * Child view holder
     *
     * @property binding
     */
    class ChildViewHolder(private val context: Context, val binding: RowTimetableBinding) :
        RecyclerView.ViewHolder(binding.root) {

        data class ItemUIResource(
            val bgDrawable: Drawable?,
            val bgColor:Int
        )
        /**
         * Bind data
         *
         * @param schoolTask
         */
        fun bindData(timeTableItem: PeriodWiseTimetable) {
            binding.run {

                timeTableItem.timetable?.let { list ->
                    if (list.size >= 1) {
                        val timeTable = list[0]
                        Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.let { selectedRole ->
                            if(selectedRole.appName == SMART_PARENT){
                                tvClassSection.text = timeTable.teacher?.fullName
                            }else{
                                timeTable.classroom?.let { classRoom ->
                                    "${classRoom.courseName} ${classRoom.classroomName}".also { tvClassSection.text = it }
                                }
                            }
                        }
                        tvSubjectName.text = timeTable.subjectName
                        "${timeTable.startTime} - ${timeTable.endTime}".also { tvTime.text = it }
//                        clTimetable.background = getBackGroundDrawable(context, timeTable.subjectName).bgDrawable
//                        btnHomeWork.setTextColor(getBackGroundDrawable(context, timeTable.subjectName).bgColor)
                    }
                }
            }
        }
        private fun getBackGroundDrawable(context: Context, subject:String): ItemUIResource {
            if(subject.contains("Telugu",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.subject_telugu),
                context.getColor(R.color.telugu_blue))
            else if (subject.contains("Hindi",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.subject_hindi),
                    context.getColor(R.color.hindi_orange))
            else if (subject.contains("English",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.subject_english),
                    context.getColor(R.color.english_pink))
            else if (subject.contains("Mathematics",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.subject_math),
                    context.getColor(R.color.math_purple))
            else if (subject.contains("Science",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.subject_science),
                    context.getColor(R.color.science_blue))
            else if (subject.contains("Environmental Science",true) || subject.contains("EVS",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.subject_environmentalstudy),
                    context.getColor(R.color.environment_green))
            else if (subject.contains("Biology",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.subject_bio),
                    context.getColor(R.color.bio_purple))
            else if (subject.contains("Physics",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.subject_physics),
                    context.getColor(R.color.physics_pink))
            else if (subject.contains("Social Studies",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.subject_socialstudy),
                    context.getColor(R.color.socialstudy_purple))
            else if (subject.contains("Civics",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.subject_civics),
                    context.getColor(R.color.civics_green))
            else if (subject.contains("Commerce",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.subject_commerce),
                    context.getColor(R.color.commerce_peach))
            else if (subject.contains("Economics",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.subject_economics),
                    context.getColor(R.color.economics_purple))
            else if (subject.contains("Chemistry",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.subject_chemistry),
                    context.getColor(R.color.chemistry_blue))
            else
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.undefined_subject_green),
                    context.getColor(R.color.undefined_green))
        }
    }

}