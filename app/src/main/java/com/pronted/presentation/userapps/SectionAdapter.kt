package com.pronted.presentation.userapps
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.pronted.R
import com.pronted.databinding.RowSectionBinding
import com.pronted.dto.student.ClassDropDownModel
import com.pronted.dto.student.SectionItem
import com.pronted.presentation.listener.ClassSectionClickListener


class SectionAdapter(
    private val context: Context,
    private val classModel: ClassDropDownModel,
    private val list: ArrayList<SectionItem>,
    private val selectedItem: ArrayList<String>,
    private val listener: ClassSectionClickListener<ClassDropDownModel, SectionItem>
) : RecyclerView.Adapter<SectionAdapter.ChildViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            parent.context,
            RowSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bindData(list[holder.absoluteAdapterPosition])
        holder.binding.run {
            if (selectedItem.isNotEmpty()) {
                if (selectedItem[0] == classModel.courseName && selectedItem[1] == list[holder.absoluteAdapterPosition].classroomName){
                    imgCheck.visibility = View.VISIBLE
                    val typeface: Typeface = context.resources.getFont(R.font.open_sans_bold)
                    tvSectionName.typeface = typeface
                }else{
                    imgCheck.visibility = View.GONE
                    val typeface: Typeface = context.resources.getFont(R.font.open_sans_regular
                    )
                    tvSectionName.typeface = typeface
                }
            }
            clSection.setOnClickListener {
                listener.onSectionClick(classModel, list[holder.absoluteAdapterPosition])
            }
        }
    }

    override fun getItemCount(): Int = list.size

    /**
     * Child view holder
     *
     * @property binding
     */
    inner class ChildViewHolder(private val context: Context, val binding: RowSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /**
         * Bind data
         *
         * @param classModel
         */
        fun bindData(classModel: SectionItem) {
            binding.run {
                tvSectionName.text = classModel.classroomName
            }
        }
    }
}