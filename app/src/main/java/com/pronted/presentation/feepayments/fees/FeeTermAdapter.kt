package com.pronted.presentation.feepayments.fees

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.Trace
import com.pronted.R
import com.pronted.databinding.RowFeeTermBinding
import com.pronted.dto.feepayments.data.FeeHeadData
import com.pronted.dto.feepayments.data.FeeTermData
import com.pronted.utils.DateUtil
import com.pronted.utils.convertToDate
import com.pronted.utils.extentions.capitalizeWords
import com.pronted.utils.toDateString

class FeeTermAdapter(
    private val mContext: Context
) : RecyclerView.Adapter<FeeTermAdapter.ChildViewHolder>() {
    var list = arrayListOf<FeeTermData>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            parent.context,
            RowFeeTermBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bindData(list[holder.bindingAdapterPosition])
        holder.binding.run {

        }


    }

    override fun getItemCount(): Int = list.size


    /**
     * Child view holder
     *
     * @property binding
     */
    class ChildViewHolder(private val context: Context, val binding: RowFeeTermBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind data
         *
         * @param feeHead
         */
        fun bindData(feeTermData: FeeTermData) {
            binding.run {
                tvFeeTermHeader.text = feeTermData.feeTerm.feeTerm.capitalizeWords()
                Trace.e("fee term data: "+feeTermData)
                if (!feeTermData.dueDate.isNullOrEmpty())
                    tvFeeTermDueDate.text = feeTermData.dueDate.convertToDate(DateUtil.y4M2d2)?.toDateString(DateUtil.m3D2Y4)
                else
                    tvFeeTermDueDate.text = context.getString(R.string.empty)
                tvActualAmount.text = String.format(context.getString(R.string.rupee_symbol_format), feeTermData.feeAmount.toInt())
                tvConcessionAmount.text = String.format(context.getString(R.string.rupee_symbol_format), feeTermData.concessionAmount.toInt())
                tvFinalAmount.text = String.format(context.getString(R.string.rupee_symbol_format), feeTermData.finalAmount.toInt())
                tvDueAmount.text = String.format(context.getString(R.string.rupee_symbol_format), feeTermData.dueAmount.toInt())
            }
        }
    }
}