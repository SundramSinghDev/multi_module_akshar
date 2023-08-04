package com.pronted.presentation.attendance

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pronted.databinding.RowShiftDropDownBinding
import com.pronted.presentation.listener.ItemClickListener

class MarkAsAllDropDownAdapter(private val itemClickListener: ItemClickListener<String>) :
    RecyclerView.Adapter<MarkAsAllDropDownAdapter.ChildViewHolder>() {

    var list = listOf<Pair<String, String>>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            parent.context,
            RowShiftDropDownBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bindData(list[position])
        holder.binding.tvName.setOnClickListener {
            itemClickListener.onClicked(list[position].first, position)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ChildViewHolder(
        private val context: Context,
        val binding: RowShiftDropDownBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        /**
         * Bind data
         *
         * @param value
         */
        fun bindData(pair: Pair<String, String>) {
            binding.run {
                tvName.text = pair.second
            }
        }
    }
}