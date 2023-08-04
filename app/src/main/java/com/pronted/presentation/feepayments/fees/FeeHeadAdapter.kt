package com.pronted.presentation.feepayments.fees

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pronted.databinding.RowFeeHeadBinding
import com.pronted.dto.feepayments.data.FeeHeadData
import com.pronted.dto.feepayments.data.FeeTermData
import com.pronted.presentation.listener.PayAmountChangeListener
import com.pronted.utils.extentions.capitalizeWords

class FeeHeadAdapter(
    private val mContext: Context,
    private val isForPayment: Boolean = false,
    private val payAmountChangeListener: PayAmountChangeListener<Float, FeeTermData>? = null,
) : RecyclerView.Adapter<FeeHeadAdapter.ChildViewHolder>() {
    lateinit var adapter: FeeTermAdapter
    lateinit var paymentAdapter: FeePaymentAdapter
    var list = arrayListOf<FeeHeadData>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            parent.context,
            RowFeeHeadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bindData(list[holder.bindingAdapterPosition])
        holder.binding.run {
            rvFeeTerms.setHasFixedSize(true)
            rvFeeTerms.layoutManager = LinearLayoutManager(
                mContext,
                LinearLayoutManager.VERTICAL, false
            )
            if (isForPayment) {
                paymentAdapter = FeePaymentAdapter(mContext, payAmountChangeListener)
                rvFeeTerms.adapter = paymentAdapter
                paymentAdapter.list =
                    list[holder.bindingAdapterPosition].feeTerms.filter { feeTermData ->
                        feeTermData.dueAmount > 0
                    } as ArrayList<FeeTermData>
            } else {
                adapter = FeeTermAdapter(mContext)
                rvFeeTerms.adapter = adapter
                adapter.list = list[holder.bindingAdapterPosition].feeTerms
            }
            /*rlWhole.setOnClickListener {
                listener.onShowSummary(list[holder.bindingAdapterPosition])
            }
            tvPay.setOnClickListener {
                listener.onPayClick(list[holder.bindingAdapterPosition])
            }*/
        }


    }

    override fun getItemCount(): Int = list.size


    /**
     * Child view holder
     *
     * @property binding
     */
    class ChildViewHolder(private val context: Context, val binding: RowFeeHeadBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind data
         *
         * @param feeHead
         */
        fun bindData(feeHead: FeeHeadData) {
            binding.run {
                tvFeeHeader.text = feeHead.feeHead.capitalizeWords()
            }
        }
    }
}