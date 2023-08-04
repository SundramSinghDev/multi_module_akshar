package com.pronted.presentation.employee

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pronted.databinding.RowDialogEmployeeBinding
import com.pronted.dto.employee.Employee
import com.pronted.presentation.listener.ItemClickListener

class EmployeesDialogAdapter(
    private val context: Context,
    private val listener: ItemClickListener<Employee>
) : RecyclerView.Adapter<EmployeesDialogAdapter.ChildViewHolder>() {

    var list = listOf<Employee>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            parent.context,
            RowDialogEmployeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bindData(list[holder.absoluteAdapterPosition])
        holder.binding.run {
            clEmployee.setOnClickListener {
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
    inner class ChildViewHolder(
        private val context: Context,
        val binding: RowDialogEmployeeBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        /**
         * Bind data
         *
         * @param employee
         */
        fun bindData(employee: Employee) {
            binding.run {
                tvEmployeeName.text = employee.fullName
                tvEmployeeDept.text = employee.department
                tvEmployeeId.text = if (absoluteAdapterPosition < 9) {
                    "0${(absoluteAdapterPosition + 1)}."
                } else {
                    "${(absoluteAdapterPosition + 1)}."
                }
            }
        }
    }
}