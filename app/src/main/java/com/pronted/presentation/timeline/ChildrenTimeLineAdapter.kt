package com.pronted.presentation.timeline

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.pronted.R
import com.pronted.databinding.RowChildrenBinding
import com.pronted.databinding.RowTimelineChildrenBinding
import com.pronted.dto.login.UserAppList
import com.pronted.presentation.listener.ItemClickListener
import com.pronted.utils.extentions.USER_SELECTED_ROLE
import io.paperdb.Paper

class ChildrenTimeLineAdapter(
    private val context: Context,
    private val listener: ItemClickListener<UserAppList>
) : RecyclerView.Adapter<ChildrenTimeLineAdapter.ChildViewHolder>() {
    var selectedItemPos = -1
        @SuppressLint("NotifyDataSetChanged")
        set

    var list = listOf<UserAppList>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            parent.context,
            RowTimelineChildrenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bindData(list[holder.absoluteAdapterPosition])
        holder.binding.run {
            clImage.background = when (selectedItemPos) {
                -1 -> {
                    val selectedRole = Paper.book().read<UserAppList>(USER_SELECTED_ROLE)
                    selectedRole?.let {
                        if (it.id == list[holder.absoluteAdapterPosition].id) {
                            imgDone.visibility = View.VISIBLE
                            selectedItemPos = holder.absoluteAdapterPosition
                            AppCompatResources.getDrawable(
                                context, R.drawable.circle_border_green
                            )
                        } else {
                            imgDone.visibility = View.GONE
                            AppCompatResources.getDrawable(
                                context, R.drawable
                                    .btn_no_boarder
                            )
                        }
                    }
                }
                holder.absoluteAdapterPosition -> {
                    imgDone.visibility = View.VISIBLE
                    AppCompatResources.getDrawable(context, R.drawable.circle_border_green)
                }
                else -> {
                    imgDone.visibility = View.GONE
                    AppCompatResources.getDrawable(
                        context, R.drawable
                            .btn_no_boarder
                    )
                }
            }

            if (list[holder.absoluteAdapterPosition].profileImage.isNullOrBlank()) {
                ivProfile.visibility = View.GONE
                ivTextProfile.visibility = View.VISIBLE
                tvShortName.text = list[holder.absoluteAdapterPosition].studentName.substring(0, 2).uppercase()
                when (position % 3) {
                    0 -> {
                        ivTextBg.setImageResource(R.color.light_pink)
                        tvShortName.setTextColor(context.getColor(R.color.pink))
                    }
                    1 -> {
                        ivTextBg.setImageResource(R.color.light_blue1)
                        tvShortName.setTextColor(context.getColor(R.color.light_blue))
                    }
                    2 -> {
                        ivTextBg.setImageResource(R.color.light_green1)
                        tvShortName.setTextColor(context.getColor(R.color.green_normal))
                    }
                }
            } else {
                ivProfile.visibility = View.VISIBLE
                ivTextProfile.visibility = View.GONE
            }

            //
            rlWhole.setOnClickListener {
                notifyItemChanged(selectedItemPos)
                selectedItemPos = holder.absoluteAdapterPosition
                notifyItemChanged(selectedItemPos)
                listener.onClicked(list[holder.absoluteAdapterPosition], holder.absoluteAdapterPosition)
            }
        }


    }

    override fun getItemCount(): Int = list.size

    /**
     * Child view holder
     *
     * @property binding
     */
    inner class ChildViewHolder(private val context: Context, val binding: RowTimelineChildrenBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /**
         * Bind data
         *
         * @param userApp
         */
        fun bindData(userApp: UserAppList) {
            binding.run {
                this.childrenModel = userApp
                /* if (selectedItemPos == absoluteAdapterPosition) {
                     cvEmployee.background = AppCompatResources.getDrawable(
                         context, R.drawable
                             .btn_primary_boarder
                     )
                 } else {
                     cvEmployee.background = AppCompatResources.getDrawable(
                         context, R.drawable
                             .btn_no_boarder
                     )
                 }*/
            }
        }
    }
}