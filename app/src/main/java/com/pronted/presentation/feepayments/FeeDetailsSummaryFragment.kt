package com.pronted.presentation.feepayments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.BaseActivity
import com.base.BaseFragment
import com.base.Trace
import com.base.inflateFragment
import com.pronted.R
import com.pronted.databinding.FragmentFeeDetailsSummaryBinding
import com.pronted.dto.feepayments.data.FeesDetailModel
import com.pronted.dto.login.UserAppList
import com.pronted.dto.student.StudentProfileResponse
import com.pronted.presentation.feepayments.fees.FeeHeadAdapter
import com.pronted.presentation.home.ChildActivity
import com.pronted.utils.SecurityResponseLabel
import com.pronted.utils.extentions.*
import io.paperdb.Paper
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeeDetailsSummaryFragment : BaseFragment<FragmentFeeDetailsSummaryBinding>() {
    private val feePaymentsViewModel: FeePaymentsViewModel by viewModel()
    private lateinit var feeHeadAdapter: FeeHeadAdapter
    private var studentProfile: StudentProfileResponse? = null
    private var feesDetailModel: FeesDetailModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_fee_details_summary
        ) as FragmentFeeDetailsSummaryBinding
        return binding.root
    }

    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.fee_details_summary))
    }

    override fun init() {
        studentProfile = arguments?.getSerializable(NAV_OBJECT) as StudentProfileResponse
        feesDetailModel = arguments?.getSerializable(NAV_OBJECT2) as FeesDetailModel?
        context?.let { context ->
            Trace.e("is child activity: " + (context is ChildActivity))
            binding.run {
                Trace.i("" + studentProfile?.studentProfileId)
                Trace.i("" + feesDetailModel?.academicYear)
                feesDetailModel?.let {
                    var finalAmount = 0F
                    var dueAmount = 0F
                    var overDueAmount = 0F
                    for (data in it.feeHeadData) {
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
                                    tvPay.visibility =
                                        if (securityGroupList.contains(SecurityResponseLabel.ME_PAYMENTS_ADD)
                                            || securityGroupList.contains(SecurityResponseLabel.ME_FEES_ADD)
                                        ) View.VISIBLE else View.GONE
                                }
                        }
                    }
                    clProceedPay.visibility = if (dueAmount.toInt() == 0) View.GONE else View.VISIBLE
                    tvPay.setOnClickListener {
                        val action = FeeDetailsSummaryFragmentDirections.actionFeeSummaryToPayment()
                        action.feeDetailModel = feesDetailModel
                        action.studentProfile = studentProfile
                        (context as ChildActivity).getNavController().navigate(action)

                    }

                    //set fee head adapter
                    hasFeeHeads = it.feeHeadData.isNotEmpty()
                    rvFeeHeads.setHasFixedSize(true)
                    rvFeeHeads.layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.VERTICAL, false
                    )
                    feeHeadAdapter = FeeHeadAdapter(context)
                    rvFeeHeads.adapter = feeHeadAdapter
                    feeHeadAdapter.list = it.feeHeadData
                }
            }
        }
    }

}