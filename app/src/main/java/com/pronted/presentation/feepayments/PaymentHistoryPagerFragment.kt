package com.pronted.presentation.feepayments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.BaseFragment
import com.base.Trace
import com.base.inflateFragment
import com.pronted.R
import com.pronted.databinding.FragmentPaymentHistoryPagerBinding
import com.pronted.dto.employee.Employee
import com.pronted.dto.feepayments.PaymentHistoryAction
import com.pronted.dto.feepayments.PaymentHistoryModel
import com.pronted.dto.feepayments.StudentFeeDetailsAction
import com.pronted.dto.student.StudentProfileResponse
import com.pronted.presentation.home.ChildActivity
import com.pronted.presentation.listener.ItemClickListener
import com.pronted.utils.DateUtil
import com.pronted.utils.extentions.*
import com.pronted.utils.toDateString
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class PaymentHistoryPagerFragment : BaseFragment<FragmentPaymentHistoryPagerBinding>() {

    private var tabPosition = 0
    private var studentProfile: StudentProfileResponse? = null
    private val feePaymentsViewModel: FeePaymentsViewModel by viewModel()
    private lateinit var paymentHistoryAdapter: PaymentHistoryAdapter
    private val currentDate by lazy {
        Calendar.getInstance().time.toDateString(DateUtil.y4M2d2)
    }

    companion object {
        fun newInstance(page: Int, studentProfile: StudentProfileResponse?): Fragment {
            val fragment = PaymentHistoryPagerFragment()
            val bundle = Bundle()
            bundle.putInt("var_arg", page)
            studentProfile?.let {
                bundle.putSerializable(STUDENT_PROFILE, it)
            }
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_payment_history_pager
        ) as FragmentPaymentHistoryPagerBinding
        return binding.root
    }

    override fun init() {
        tabPosition = arguments?.getInt("var_arg") ?: 0
        studentProfile = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getSerializable(STUDENT_PROFILE, StudentProfileResponse::class.java)
        }else{
            arguments?.getSerializable(STUDENT_PROFILE) as StudentProfileResponse
        }
        binding.run {
            studentProfile?.let {
                fetchPaymentHistory(it.studentProfileId)
            }
            hasPaymentHistory = false
            rvPaymentHistory.setHasFixedSize(true)
            rvPaymentHistory.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            context?.let {
                paymentHistoryAdapter = PaymentHistoryAdapter(it, historyClickListener)
                rvPaymentHistory.adapter = paymentHistoryAdapter
            }
        }

    }

    private val historyClickListener =
        object : ItemClickListener<PaymentHistoryModel> {
            override fun onClicked(item: PaymentHistoryModel, positoin:Int) {
                Trace.i("payment summary open")
                context?.let {
                    val bundle = bundleOf(
                        Pair(NAV_DESTINATION, R.id.paymentHistoryDetails),
                        Pair(NAV_OBJECT, item)
                    )
                    if(it is ChildActivity){
                        (context as ChildActivity).getNavController().navigate(R.id.paymentHistoryDetails,bundle)
                    }else {
                        startNewActivity(
                            it, ChildActivity::class.java, bundle = bundle
                        )
                    }
                }
            }
        }
    private fun fetchPaymentHistory(studentProfileId: Int) {
        lifecycleScope.launchWhenResumed {
            feePaymentsViewModel.fetchFeePaymentHistory(studentProfileId)
                .collectLatest { paymentsHistoryAction ->
                    when (paymentsHistoryAction) {
                        is PaymentHistoryAction.Success -> {
                            Trace.e("Student fee payment history: " + paymentsHistoryAction.paymentsHistory)
                            paymentHistoryAdapter.list = paymentsHistoryAction.paymentsHistory
                            binding.hasPaymentHistory = paymentsHistoryAction.paymentsHistory.isNotEmpty()
                        }
                        is PaymentHistoryAction.Fail -> {
                            Trace.i(
                                paymentsHistoryAction.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                            binding.hasPaymentHistory = false
                        }
                    }
                }
        }
    }

}