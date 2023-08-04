package com.pronted.presentation.timeline.birthday

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pronted.R
import com.pronted.databinding.RowBirthdayBinding
import com.pronted.dto.timeline.BirthDayModel
import com.pronted.utils.DateUtil.MMMM
import com.pronted.utils.DateUtil.d2
import com.pronted.utils.DateUtil.y4M2d2
import com.pronted.utils.convertToDate
import com.pronted.utils.toDateString

class BirthdayAdapter(
    private val mContext: Context,
    private val tabPage: Int
) : RecyclerView.Adapter<BirthdayAdapter.ChildViewHolder>() {
    var list = arrayListOf<BirthDayModel>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            parent.context,
            RowBirthdayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bindData(list[holder.bindingAdapterPosition])
        holder.binding.run {
            val model = list[holder.bindingAdapterPosition]
            var name = model.firstName
            model.lastName?.let {
                name = "${model.firstName} ${model.lastName}"
            }
            tvUserName.text = name

            tvClassName.text =
                if (tabPage == 0) "${model.courseName} ${model.classroomName}" else model.deptName

            model.birthday?.let {
                tvMonth.text = it.convertToDate(y4M2d2)?.toDateString(MMMM)
                tvDate.text = it.convertToDate(y4M2d2)?.toDateString(d2)
            }

            if (model.imageUrl.isNullOrBlank()) {
                ivProfile.visibility = View.GONE
                ivTextProfile.visibility = View.VISIBLE
                if(model.lastName.isNullOrBlank()) {
                    model.firstName?.let {
                        tvShortName.text = it.substring(0, 2).uppercase()
                    }
                }else{
                    "${model.firstName?.substring(0,1)?.uppercase()}${model.lastName.substring(0,1).uppercase()}".also {
                        tvShortName.text = it
                    }
                }

                when (position % 3) {
                    0 -> {
                        ivTextBg.setImageResource(R.color.light_pink)
                        tvShortName.setTextColor(mContext.getColor(R.color.pink))
                    }
                    1 -> {
                        ivTextBg.setImageResource(R.color.light_blue1)
                        tvShortName.setTextColor(mContext.getColor(R.color.light_blue))
                    }
                    2 -> {
                        ivTextBg.setImageResource(R.color.light_green1)
                        tvShortName.setTextColor(mContext.getColor(R.color.green_normal))
                    }
                }
            } else {
                ivProfile.visibility = View.VISIBLE
                ivTextProfile.visibility = View.GONE
            }
        }


    }

    override fun getItemCount(): Int = list.size


    /**
     * Child view holder
     *
     * @property binding
     */
    class ChildViewHolder(private val context: Context, val binding: RowBirthdayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind data
         *
         * @param birthDayModel
         */
        fun bindData(birthDayModel: BirthDayModel) {
            binding.run {
                this.birthday = birthDayModel
            }
        }
    }

}