package com.pronted.presentation.feepayments

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.Trace
import com.pronted.R
import com.pronted.databinding.RowPaymentHistoryBinding
import com.pronted.dto.feepayments.PaymentHistoryModel
import com.pronted.presentation.listener.ItemClickListener
import com.pronted.utils.DateUtil
import com.pronted.utils.DateUtil.m3D2Y4
import com.pronted.utils.convertToDate
import com.pronted.utils.toDateString

class PaymentHistoryAdapter(
    private val mContext: Context,
    private val listener:ItemClickListener<PaymentHistoryModel>
) : RecyclerView.Adapter<PaymentHistoryAdapter.ChildViewHolder>() {
    var list = arrayListOf<PaymentHistoryModel>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            parent.context,
            RowPaymentHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bindData(list[holder.bindingAdapterPosition])
        holder.binding.run {
            rlWhole.setOnClickListener {
                listener.onClicked(list[holder.bindingAdapterPosition], holder.bindingAdapterPosition)
            }
        }


    }

    override fun getItemCount(): Int = list.size


    /**
     * Child view holder
     *
     * @property binding
     */
    class ChildViewHolder(private val context: Context, val binding: RowPaymentHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind data
         *
         * @param paymentModel
         */
        fun bindData(paymentModel: PaymentHistoryModel) {
            binding.run {
                tvPaymentDate.text = paymentModel.paymentDate.convertToDate(DateUtil.y4M2d2)
                    ?.toDateString(m3D2Y4)
                tvInvoice.text = String.format(
                    context.getString(R.string.invoice_format),
                    paymentModel.invoiceNumber
                )
                tvModeOfPayment.text = String.format(
                    context.getString(R.string.payment_mode_format),
                    paymentModel.paymentMethod
                )

                var paidAmount = 0F
                for (model in paymentModel.paymentsList) {
                    paidAmount += model.paymentAmount
                }

                tvAmount.text = String.format(
                    context.getString(R.string.rupee_symbol_format),
                    paidAmount.toInt()
                )

            }
        }
    }
}