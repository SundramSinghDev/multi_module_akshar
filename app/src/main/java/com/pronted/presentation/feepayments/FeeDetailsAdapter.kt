package com.pronted.presentation.feepayments

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.base.Trace
import com.pronted.R
import com.pronted.databinding.RowFeeDetailsBinding
import com.pronted.dto.feepayments.data.FeesDetailModel
import com.pronted.dto.login.UserAppList
import com.pronted.presentation.listener.FeeDetailsClickListener
import com.pronted.utils.SecurityResponseLabel
import com.pronted.utils.extentions.SECURITY_GROUP_LIST
import com.pronted.utils.extentions.SPECTRUM_ROLE
import com.pronted.utils.extentions.USER_SELECTED_ROLE
import io.paperdb.Paper

class FeeDetailsAdapter(
    private val mContext: Context,
    private val listener: FeeDetailsClickListener<FeesDetailModel>
) : RecyclerView.Adapter<FeeDetailsAdapter.ChildViewHolder>() {
    var list = arrayListOf<FeesDetailModel>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder(
            parent.context,
            RowFeeDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bindData(list[holder.bindingAdapterPosition])
        holder.binding.run {
            rlWhole.setOnClickListener {
                listener.onShowSummary(list[holder.bindingAdapterPosition])
            }
            tvPay.setOnClickListener {
                listener.onPayClick(list[holder.bindingAdapterPosition])
            }
        }


    }

    override fun getItemCount(): Int = list.size


    /**
     * Child view holder
     *
     * @property binding
     */
    class ChildViewHolder(private val context: Context, val binding: RowFeeDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind data
         *
         * @param feesDetailModel
         */
        fun bindData(feesDetailModel: FeesDetailModel) {
            binding.run {
                tvYear.text = String.format(
                    context.getString(R.string.academic_year),
                    feesDetailModel.academicYear
                )
                var finalAmount = 0F
                var dueAmount = 0F
                var overDueAmount = 0F
                for (data in feesDetailModel.feeHeadData) {
                    for (price in data.feeTerms) {
                        finalAmount += price.finalAmount
                        dueAmount += price.dueAmount
                        overDueAmount += price.overdueAmount
                    }
                }

                tvFinalAmount.text =
                    String.format(
                        context.getString(R.string.rupee_symbol_format),
                        finalAmount.toInt()
                    )
                tvDueAmount.text =
                    String.format(
                        context.getString(R.string.rupee_symbol_format),
                        dueAmount.toInt()
                    )
                tvOverDueAmount.text =
                    String.format(
                        context.getString(R.string.rupee_symbol_format),
                        overDueAmount.toInt()
                    )
                Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.let { userApp ->
                    if (userApp.appName == SPECTRUM_ROLE) {
                        Paper.book().read<ArrayList<String>>(SECURITY_GROUP_LIST)
                            ?.let { securityGroupList ->

                                Trace.e(
                                    "pay enable: " + (securityGroupList.contains(
                                        SecurityResponseLabel.ME_PAYMENTS_ADD
                                    ))
                                )
                                tvPay.visibility =
                                    if (securityGroupList.contains(SecurityResponseLabel.ME_PAYMENTS_ADD)
                                        || securityGroupList.contains(SecurityResponseLabel.ME_FEES_ADD)
                                    ) View.VISIBLE else View.GONE

                            }
                    }
                }
                tvPay.visibility = if (dueAmount.toInt() == 0) View.GONE else View.VISIBLE
            }
        }
    }
}