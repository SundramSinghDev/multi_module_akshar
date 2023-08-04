package com.pronted.presentation.feepayments.fees

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.base.BaseDialogFragment
import com.base.Trace
import com.base.inflateFragment
import com.pronted.R
import com.pronted.databinding.FragmentPaymentFailureBinding
import com.pronted.dto.feepayments.PostPaymentAction
import com.pronted.dto.feepayments.data.RazorPayPaymentRequest
import com.pronted.dto.feepayments.data.RazorPayPaymentResponse
import com.pronted.presentation.feepayments.FeePaymentsViewModel
import com.pronted.utils.extentions.NAV_OBJECT
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentFailureDialogFragment : BaseDialogFragment<FragmentPaymentFailureBinding>() {


    private var paymentResponse: RazorPayPaymentRequest? = null
    private val feePaymentsViewModel: FeePaymentsViewModel by viewModel()

    override fun create(savedInstanceState: Bundle?) {
        paymentResponse = if (Build.VERSION.SDK_INT >= 33) {
            arguments?.getSerializable(NAV_OBJECT, RazorPayPaymentRequest::class.java)
        } else {
            arguments?.getSerializable(NAV_OBJECT) as RazorPayPaymentRequest
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater,
            container,
            R.layout.fragment_payment_failure
        ) as FragmentPaymentFailureBinding
        observeClick(root)
        return binding.root
    }

    override fun init() {
        binding.run {
            context?.let { context ->
                tvAmountPaid.text =
                    getString(com.pronted.R.string.rupee_symbol_format).format(paymentResponse?.amount)
                processOrderPostPayment()
            }

            imgBack.setOnClickListener {
            }
            tvGoBack.setOnClickListener {  }
            tvRetry.setOnClickListener {  }
        }
    }

    private fun processOrderPostPayment() {
        lifecycleScope.launchWhenResumed {
            paymentResponse?.let {
                feePaymentsViewModel.processOrderPostPayment(
                    RazorPayPaymentResponse(
                        it.orderId,
                        it.paymentId,
                        "",
                        it.errorCode
                    )
                ).collectLatest {
                        postPaymentAction ->
                    dismissLoader()
                    when (postPaymentAction) {
                        is PostPaymentAction.Success -> {
                            Trace.e("Post order payment: " + postPaymentAction.orderResponse)
                        }
                        is PostPaymentAction.Fail -> {
                            toast(
                                postPaymentAction.errorResponse.message ?: getString(
                                    R.string.something_went_wrong_try_again
                                )
                            )
                        }
                    }
                }
            }

        }
    }

    companion object {
        fun newInstance(
            model: RazorPayPaymentRequest
        ): PaymentFailureDialogFragment =
            PaymentFailureDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(NAV_OBJECT, model)
                }
            }
    }
}