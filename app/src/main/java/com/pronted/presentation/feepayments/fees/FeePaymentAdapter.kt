package com.pronted.presentation.feepayments.fees

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.base.Trace
import com.pronted.R
import com.pronted.databinding.RowFeePaymentBinding
import com.pronted.dto.feepayments.data.FeeTermData
import com.pronted.presentation.listener.PayAmountChangeListener
import com.pronted.utils.DateUtil
import com.pronted.utils.convertToDate
import com.pronted.utils.extentions.capitalizeWords
import com.pronted.utils.toDateString

class FeePaymentAdapter(
    private val mContext: Context,
    private val payAmountChangeListener: PayAmountChangeListener<Float, FeeTermData>?= null
) : RecyclerView.Adapter<FeePaymentAdapter.ChildViewHolder>() {
    var list = arrayListOf<FeeTermData>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            parent.context,
            RowFeePaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bindData(list[holder.bindingAdapterPosition], payAmountChangeListener)
        holder.binding.run {

        }


    }

    override fun getItemCount(): Int = list.size


    /**
     * Child view holder
     *
     * @property binding
     */
    class ChildViewHolder(private val context: Context, val binding: RowFeePaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind data
         *
         * @param feeTermData
         * @param paymentListener
         */
        fun bindData(feeTermData: FeeTermData, paymentListener:PayAmountChangeListener<Float, FeeTermData>?) {
            binding.run {
                tvFeeTermHeader.text = feeTermData.feeTerm.feeTerm.capitalizeWords()
                Trace.e("fee term data: "+feeTermData)
                if (!feeTermData.dueDate.isNullOrEmpty())
                    tvFeeTermDueDate.text = feeTermData.dueDate.convertToDate(DateUtil.y4M2d2)?.toDateString(DateUtil.m3D2Y4)
                else
                    tvFeeTermDueDate.text = context.getString(R.string.empty)
                etAmount.setText(feeTermData.dueAmount.toInt().toString())
                tvDueAmountLabel.text = String.format(context.getString(R.string.pay_due_amount), feeTermData.dueAmount.toInt())

                etAmount.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {
                        android.os.Handler().postDelayed({
                            if( p0!!.toString().isEmpty() || p0.toString() == "0"){
                                Toast.makeText(context, "Please enter valid amount", Toast.LENGTH_LONG).show()
                                if (cbPay.isChecked) {
                                    cbPay.isChecked = false
                                }
                            }
                        }, 100)

                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                })
                cbPay.setOnCheckedChangeListener { buttonView, isChecked ->
                    if(isChecked){
                        if(etAmount.text.toString().isNotEmpty()){
                            val enteredValue = etAmount.text.toString().trim().toFloat()
                            if (enteredValue > feeTermData.dueAmount) {
                                Toast.makeText(context,"Amount cannot be greater than actual due amount",Toast.LENGTH_LONG).show()
                                buttonView.isEnabled = false
                                buttonView.isChecked = false
                                buttonView.isEnabled = true
                                return@setOnCheckedChangeListener
                            }
                            paymentListener?.onAddPayment(enteredValue, feeTermData)
                        }else{
                            etAmount.setText(feeTermData.dueAmount.toInt().toString())
                            paymentListener?.onAddPayment(feeTermData.dueAmount, feeTermData)
                        }
                        etAmount.isEnabled = false
                    }else{
                        etAmount.isEnabled = true
                        if (etAmount.text.toString().trim().isNotEmpty()) {
                            val enteredValue = etAmount.text.toString().trim().toFloat()
                            paymentListener?.onRemovePayment(enteredValue, feeTermData)
                        } else {
                           etAmount.setText(feeTermData.dueAmount.toInt().toString())
                            paymentListener?.onRemovePayment(feeTermData.dueAmount, feeTermData)
                        }
                    }
                }
            }
        }
    }
}