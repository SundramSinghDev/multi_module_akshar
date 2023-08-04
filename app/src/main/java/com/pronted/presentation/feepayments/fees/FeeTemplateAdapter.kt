package com.pronted.presentation.feepayments.fees

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pronted.databinding.RowMenuItemBinding
import com.pronted.dto.employee.FeeTemp
import com.pronted.presentation.listener.ItemClickListener

class FeeTemplateAdapter(
    private val context: Context,
    private val listener: ItemClickListener<FeeTemp>
) : RecyclerView.Adapter<FeeTemplateAdapter.ChildViewHolder>() {

    var list = listOf<FeeTemp>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            parent.context,
            RowMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bindData(list[holder.absoluteAdapterPosition])
        holder.binding.run {
            clItem.setOnClickListener {
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
    inner class ChildViewHolder(private val context: Context, val binding: RowMenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /**
         * Bind data
         *
         * @param employee
         */
        fun bindData(feeTemp: FeeTemp) {
            binding.run {
                tvItemTitle.text = feeTemp.templateName
            }
        }
    }
}