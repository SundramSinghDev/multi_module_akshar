package com.pronted.presentation.userapps

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pronted.databinding.RowClassBinding
import com.pronted.dto.student.ClassDropDownModel
import com.pronted.dto.student.SectionItem
import com.pronted.presentation.listener.ClassSectionClickListener

class ClassDropDownAdapter(
    private val context: Context,
    private val selectedItem: ArrayList<String>,
    private val listener: ClassSectionClickListener<ClassDropDownModel, SectionItem>
) : RecyclerView.Adapter<ClassDropDownAdapter.ChildViewHolder>() {

    var list = listOf<ClassDropDownModel>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            parent.context,
            RowClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bindData(list[holder.absoluteAdapterPosition])
        holder.binding.run {
            rvSections.setHasFixedSize(true)
            rvSections.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )
            val adapter = SectionAdapter(
                context,
                classModel = list[holder.absoluteAdapterPosition],
                list[holder.absoluteAdapterPosition].sections,
                selectedItem,
                listener
            )
            rvSections.adapter = adapter
        }
    }

    override fun getItemCount(): Int = list.size

    /**
     * Child view holder
     *
     * @property binding
     */
    inner class ChildViewHolder(private val context: Context, val binding: RowClassBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /**
         * Bind data
         *
         * @param classModel
         */
        fun bindData(classModel: ClassDropDownModel) {
            binding.run {
                tvClassName.text = classModel.courseName

            }
        }
    }
}