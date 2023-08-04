package com.pronted.presentation.userapps

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pronted.databinding.RowMenuItemBinding
import com.pronted.dto.feepayments.BankAccount
import com.pronted.presentation.listener.ItemClickListener

class BankAccountAdapter(
    private val context: Context,
    private val listener: ItemClickListener<BankAccount>
) : RecyclerView.Adapter<BankAccountAdapter.ChildViewHolder>() {

    var list = listOf<BankAccount>()
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
        fun bindData(bank: BankAccount) {
            binding.run {
                tvItemTitle.text = bank.bankName
            }
        }
    }
}