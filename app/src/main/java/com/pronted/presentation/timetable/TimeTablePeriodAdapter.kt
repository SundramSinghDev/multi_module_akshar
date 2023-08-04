package com.pronted.presentation.timetable

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.pronted.R
import com.pronted.databinding.RowPeriodTimetableBinding
import com.pronted.dto.timeline.TimeTableSchedule

class TimeTablePeriodAdapter(
    private val mContext: Context,
    private var isForEmployee : Boolean = false
) : RecyclerView.Adapter<TimeTablePeriodAdapter.ChildViewHolder>() {

    var list = arrayListOf<TimeTableSchedule>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            parent.context,
            RowPeriodTimetableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bindData(list[position], isForEmployee)

    }

    override fun getItemCount(): Int = list.size


    /**
     * Child view holder
     *
     * @property binding
     */
    class ChildViewHolder(private val context: Context, val binding: RowPeriodTimetableBinding) :
        RecyclerView.ViewHolder(binding.root) {

        data class ItemUIResource(
            val bgDrawable: Drawable?,
            val bgColor:Int
        )
        /**
         * Bind data
         *
         * @param timeTableItem
         */
        fun bindData(timeTableItem: TimeTableSchedule, isForEmployee: Boolean) {
            binding.run {
                if(isForEmployee){
                   tvClassName.text = "${timeTableItem.classroom?.courseName} ${timeTableItem.classroom?.classroomName}"
                }else{
                    tvClassName.text =timeTableItem.teacher?.fullName
                }
                tvSubjectName.text = timeTableItem.subjectName
                clImage.setBackgroundColor(getBackGroundDrawable(context, timeTableItem.subjectName).bgColor)
                ivProfile.background = getBackGroundDrawable(context, timeTableItem.subjectName).bgDrawable
            }
        }
        private fun getBackGroundDrawable(context: Context, subject:String): ItemUIResource {
            if(subject.contains("Telugu",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.timetable_telugu),
                context.getColor(R.color.timetable_telugu))
            else if (subject.contains("Hindi",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.timetable_hindi),
                    context.getColor(R.color.timetable_hindi))
            else if (subject.contains("English",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.timetable_english),
                    context.getColor(R.color.timetable_english))
            else if (subject.contains("Mathematics",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.timetable_math),
                    context.getColor(R.color.timetable_math))
            else if (subject.contains("Science",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.timetable_science),
                    context.getColor(R.color.timetable_science))
            else if (subject.contains("Environmental Science",true) || subject.contains("EVS",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.timetable_environmental_study),
                    context.getColor(R.color.timetable_environmental))
            else if (subject.contains("Biology",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.timetable_bio),
                    context.getColor(R.color.timetable_bio))
            else if (subject.contains("Physics",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.timetable_physics),
                    context.getColor(R.color.timetable_physics))
            else if (subject.contains("Social Studies",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.timetable_social_study),
                    context.getColor(R.color.timetable_socialstudy))
            else if (subject.contains("Civics",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.timetable_civics),
                    context.getColor(R.color.timetable_civics))
            else if (subject.contains("Commerce",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.timetable_commerce),
                    context.getColor(R.color.timetable_commerce))
            else if (subject.contains("Economics",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.timetable_economics),
                    context.getColor(R.color.timetable_economics))
            else if (subject.contains("Chemistry",true))
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.timetable_chemistry),
                    context.getColor(R.color.timetable_chemistry))
            else
                return ItemUIResource(AppCompatResources.getDrawable(context, R.drawable.timetableother),
                    context.getColor(R.color.timetable_other))
        }
    }

}