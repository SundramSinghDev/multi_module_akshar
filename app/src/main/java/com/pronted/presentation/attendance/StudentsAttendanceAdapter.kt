package com.pronted.presentation.attendance

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.pronted.R
import com.pronted.databinding.RowAttendanceEntryBinding
import com.pronted.dto.attendance.AttendanceModel
import com.pronted.utils.extentions.display
import com.pronted.utils.extentions.gone
import com.pronted.utils.extentions.visible

class StudentsAttendanceAdapter() :
    RecyclerView.Adapter<StudentsAttendanceAdapter.ChildViewHolder>() {

    var list = listOf<AttendanceModel>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            parent.context,
            RowAttendanceEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val attendanceModel = list[position]
        holder.bindData(position.plus(1), attendanceModel)
        holder.binding.run {
            txtAbsent.setOnClickListener {
                attendanceModel.attendanceInd = holder.ABSENT
                attendanceModel.lateEntryFlag = ""
                notifyItemChanged(position)
                llAttendance.gone()
            }
            txtLeave.setOnClickListener {
                attendanceModel.attendanceInd = holder.LEAVE
                attendanceModel.lateEntryFlag = ""
                notifyItemChanged(position)
                llAttendance.gone()
            }
            txtLate.setOnClickListener {
                attendanceModel.attendanceInd = holder.PRESENT
                attendanceModel.lateEntryFlag = "Y"
                notifyItemChanged(position)
                llAttendance.gone()
            }
            txtWeekOff.setOnClickListener {
                attendanceModel.attendanceInd = holder.WEEK_OFF
                attendanceModel.lateEntryFlag = ""
                notifyItemChanged(position)
                llAttendance.gone()
            }
            txtHoliday.setOnClickListener {
                attendanceModel.attendanceInd = holder.HOLIDAY
                attendanceModel.lateEntryFlag = ""
                notifyItemChanged(position)
                llAttendance.gone()
            }
            txtAttendanceSwitch.setOnClickListener {
                if (attendanceModel.attendanceInd.isNullOrBlank() || txtAttendanceSwitch.isChecked) {
                    attendanceModel.attendanceInd = holder.PRESENT
                    notifyItemChanged(position)
                }
                llAttendance.display(txtAttendanceSwitch.isChecked.not())
            }
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ChildViewHolder(
        val context: Context,
        val binding: RowAttendanceEntryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        val PRESENT = "P"
        val ABSENT = "A"
        val LEAVE = "L"
        val WEEK_OFF = "W"
        val HOLIDAY = "H"

        /**
         * Bind data
         *
         * @param attendanceModel
         */
        fun bindData(position: Int, attendanceModel: AttendanceModel) {
            binding.run {
                txtRollNo.text = position.toString().plus(".")
                txtStudentName.text = context.getString(R.string.firstname_lastname_format)
                    .format(attendanceModel.firstName, attendanceModel.lastName)
                txtAttendanceSwitch.isChecked = false
                txtAttendanceStatus.gone()
                when (attendanceModel.attendanceInd) {
                    PRESENT -> {
                        txtAttendanceSwitch.isChecked = true
                        if (attendanceModel.lateEntryFlag == "Y") {
                            setAttendanceStatus(
                                R.drawable.orange_rounded_radius_2_bg,
                                R.string.late
                            )
                        } else {
                            setAttendanceStatus(
                                R.drawable.green_radius_2_bg,
                                R.string.present
                            )
                        }
                    }
                    ABSENT -> {
                        setAttendanceStatus(
                            R.drawable.light_red_rounded_radius_2_bg,
                            R.string.absent
                        )
                    }
                    LEAVE -> {
                        setAttendanceStatus(
                            R.drawable.yellow_rounded_radius_2_bg,
                            R.string.leave
                        )
                    }
                    WEEK_OFF -> {
                        setAttendanceStatus(
                            R.drawable.blue_radius_2_bg,
                            R.string.week_off
                        )
                    }
                    HOLIDAY -> {
                        setAttendanceStatus(
                            R.drawable.light_blue_radius_attendance,
                            R.string.holiday
                        )
                    }
                }
            }
        }

        private fun setAttendanceStatus(background: Int, @StringRes string: Int) {
            binding.txtAttendanceStatus.run {
                setBackgroundResource(background)
                text = context.getString(string)
                visible()
            }
        }
    }
}