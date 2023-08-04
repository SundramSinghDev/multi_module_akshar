package com.pronted.presentation.feepayments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.*
import com.pronted.R
import com.pronted.databinding.FragmentPaymentHistoryDetailsBinding
import com.pronted.databinding.FragmentStudentMarksBinding
import com.pronted.dto.employee.InvoiceAction
import com.pronted.dto.employee.ManualFeePaymentAction
import com.pronted.dto.feepayments.PaymentHistoryModel
import com.pronted.dto.feepayments.data.ManualFeePayment
import com.pronted.dto.student.StudentProfileResponse
import com.pronted.presentation.feepayments.fees.FeeTermAdapter
import com.pronted.presentation.home.ChildActivity
import com.pronted.utils.DateUtil
import com.pronted.utils.convertToDate
import com.pronted.utils.extentions.DYNAMIC_HEADERS
import com.pronted.utils.extentions.NAV_DESTINATION
import com.pronted.utils.extentions.NAV_OBJECT
import com.pronted.utils.extentions.startNewActivity
import com.pronted.utils.toDateString
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentHistoryDetailsFragment : BaseFragment<FragmentPaymentHistoryDetailsBinding>() {
    private var paymentHistory: PaymentHistoryModel? = null
    lateinit var adapter: FeeTermHistoryAdapter
    private var invoiceUrl: String? = null
    private val feePaymentsViewModel: FeePaymentsViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_payment_history_details
        ) as FragmentPaymentHistoryDetailsBinding
        return binding.root
    }

    override fun init() {
        paymentHistory = arguments?.getSerializable(NAV_OBJECT) as PaymentHistoryModel
        Trace.i("" + paymentHistory?.paymentDate)
        context?.let { context ->
            paymentHistory?.let {
                binding.run {
                    tvInvoiceValue.text = it.invoiceNumber.toString()
                    tvPaymentDate.text = it.paymentDate.convertToDate(DateUtil.y4M2d2)
                        ?.toDateString(DateUtil.m3D2Y4)
                    tvReceivedBy.text = it.receivedBy
                    tvPaymentMethod.text = it.paymentMethod
                    tvRefNo.text = it.paymentReferenceNbr ?: getString(R.string.empty)
                    tvCheque.text = it.checkNbr ?: getString(R.string.empty)
                    var totalPrice = 0F
                    for (model in it.paymentsList) {
                        totalPrice += model.paymentAmount
                    }
                    tvTotal.text = totalPrice.toString()
                    rvTerms.setHasFixedSize(true)
                    rvTerms.layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.VERTICAL, false
                    )
                    adapter = FeeTermHistoryAdapter(context)
                    rvTerms.adapter = adapter

                    adapter.list = it.paymentsList

                    printInvoice.setOnClickListener {
                        invoiceUrl?.let {
                            navigateInvoiceScreen(it)
                        }
                    }
                    fetchInvoiceUrl(it.invoiceNumber)
                }
            }
        }
    }

    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.payment_details))
    }

    private fun navigateInvoiceScreen(invoiceUrl: String) {
        context?.let {
            val bundle = bundleOf(
                Pair(NAV_DESTINATION, R.id.paymentInvoiceFragment),
                Pair(NAV_OBJECT, invoiceUrl)
            )
            if (it is ChildActivity) {
                (context as ChildActivity).getNavController()
                    .navigate(R.id.paymentInvoiceFragment, bundle)
            } else {
                startNewActivity(
                    it, ChildActivity::class.java, bundle = bundle
                )
            }
        }
    }

    /**
     * Add manual fee payments by employee
     */
    private fun fetchInvoiceUrl(invoiceId: Int) {
        lifecycleScope.launchWhenResumed {
            Preference.instance.putBoolean(DYNAMIC_HEADERS, false)
            feePaymentsViewModel.fetchInvoiceUrl(invoiceId)
                .collectLatest { invoiceAction ->
                    when (invoiceAction) {
                        is InvoiceAction.Success -> {
                            Trace.e("Payment invoice URL: " + invoiceAction.invoice.url)
                            invoiceUrl = invoiceAction.invoice.url
                        }
                        is InvoiceAction.Fail -> {
                            Trace.i(
                                invoiceAction.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                        }
                    }
                }
        }

    }
}