package com.pronted.presentation.scheduleexam

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pronted.databinding.RowExamDropDownBinding
import com.pronted.dto.exam.ExamDropDownModel
import com.pronted.presentation.listener.ItemClickListener

class ExamDropDownAdapter(private val itemClickListener: ItemClickListener<ExamDropDownModel>) : RecyclerView.Adapter<ExamDropDownAdapter.ChildViewHolder>() {

    var list = listOf<ExamDropDownModel>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            parent.context,
            RowExamDropDownBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bindData(list[position])
        holder.binding.tvExamName.setOnClickListener {
            itemClickListener.onClicked(list[position], position)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class ChildViewHolder(private val context: Context, val binding: RowExamDropDownBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /**
         * Bind data
         *
         * @param examDropDownModel
         */
        fun bindData(examDropDownModel: ExamDropDownModel) {
            binding.run {
                tvExamName.text = examDropDownModel.examName
            }
        }
    }
}