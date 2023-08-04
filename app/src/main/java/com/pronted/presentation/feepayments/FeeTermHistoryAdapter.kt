package com.pronted.presentation.feepayments

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.Trace
import com.pronted.R
import com.pronted.databinding.RowFeeTermBinding
import com.pronted.databinding.RowPaymentHistoryFeeTermBinding
import com.pronted.dto.feepayments.Payment
import com.pronted.dto.feepayments.data.FeeHeadData
import com.pronted.dto.feepayments.data.FeeTermData
import com.pronted.utils.DateUtil
import com.pronted.utils.convertToDate
import com.pronted.utils.extentions.capitalizeWords
import com.pronted.utils.toDateString

class FeeTermHistoryAdapter(
    private val mContext: Context
) : RecyclerView.Adapter<FeeTermHistoryAdapter.ChildViewHolder>() {
    var list = arrayListOf<Payment>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            parent.context,
            RowPaymentHistoryFeeTermBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
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
    class ChildViewHolder(
        private val context: Context,
        val binding: RowPaymentHistoryFeeTermBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind data
         *
         * @param feeTermData
         */
        fun bindData(feeTermData: Payment) {
            binding.run {
                tvFeeTermHeader.text = String.format(
                    context.getString(R.string.class_section_format),
                    feeTermData.feeHead,
                    feeTermData.feeTerm
                )
                tvAmountPaid.text = feeTermData.paymentAmount.toString()

            }
        }
    }
}