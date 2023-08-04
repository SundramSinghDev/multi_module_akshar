package com.pronted.presentation.timeline.finance


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pronted.R
import com.pronted.databinding.RowCollectionBinding
import com.pronted.dto.timeline.FeePayment
import com.pronted.utils.extentions.getFormattedAmount

class CollectionAdapter(
    private val mContext: Context
) : RecyclerView.Adapter<CollectionAdapter.ChildViewHolder>() {
    var list = arrayListOf<FeePayment>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private val colorList = mContext.resources.getIntArray(R.array.colors_chart_collections)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            parent.context,
            RowCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bindData(list[position])
        holder.binding.run {
            holder.binding.tvCollectionColor.setBackgroundColor(colorList[holder.absoluteAdapterPosition % 12])
        }
    }

    override fun getItemCount(): Int = list.size


    /**
     * Child view holder
     *
     * @property binding
     */
    class ChildViewHolder(private val context: Context, val binding: RowCollectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind data
         *
         * @param item
         */
        fun bindData(item: FeePayment) {
            binding.run {
                String.format(
                    context.getString(R.string.rupee_symbol_format),
                    item.value.getFormattedAmount()
                ).run {
                    tvDueAmountCl.text = this
                }
                tvDueCollection.text = item.key
            }
        }
    }

}