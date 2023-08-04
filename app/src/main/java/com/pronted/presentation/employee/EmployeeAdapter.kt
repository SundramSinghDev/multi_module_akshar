package com.pronted.presentation.employee

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.pronted.R
import com.pronted.databinding.RowChildrenBinding
import com.pronted.databinding.RowEmployeeBinding
import com.pronted.databinding.RowStudentBinding
import com.pronted.dto.employee.Employee
import com.pronted.dto.login.UserAppList
import com.pronted.dto.student.StudentProfileResponse
import com.pronted.presentation.listener.ItemClickListener
import com.pronted.utils.extentions.USER_SELECTED_ROLE
import com.pronted.utils.extentions.capitalizeWords
import io.paperdb.Paper

class EmployeeAdapter(
    private val context: Context,
    private var fragment : Fragment,
    private val listener: ItemClickListener<Employee>
) : RecyclerView.Adapter<EmployeeAdapter.ChildViewHolder>() {

    var list = listOf<Employee>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            parent.context,
            RowEmployeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bindData(list[holder.absoluteAdapterPosition])
        holder.binding.run {
            clStudent.setOnClickListener {
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
    inner class ChildViewHolder(private val context: Context, val binding: RowEmployeeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /**
         * Bind data
         *
         * @param employee
         */
        fun bindData(employee: Employee) {
            binding.run {
                this.employee = employee

               /* val name = if(employee.lastName != null){
                    employee.firstName +" "+employee.lastName
                }else if(employee.firstName!= null){
                    employee.firstName
                }else{
                    "N/A"
                }*/

                tvStudentName.text = employee.fullName.capitalizeWords()
                tvSerialNumber.text = if(absoluteAdapterPosition < 9){
                    "0${(absoluteAdapterPosition+1)}."
                }else{
                    "${(absoluteAdapterPosition+1)}."
                }
            }
        }
    }
}