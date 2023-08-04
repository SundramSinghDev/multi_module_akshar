package com.pronted.presentation.feepayments.fees

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Constraints
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.anychart.core.Base
import com.base.*
import com.pronted.R
import com.pronted.databinding.DialogEmailPhoneBinding
import com.pronted.databinding.FragmentProceedFeePaymentBinding
import com.pronted.dto.feepayments.OrderCreationAction
import com.pronted.dto.feepayments.PostPaymentAction
import com.pronted.dto.feepayments.data.*
import com.pronted.dto.login.UserAppList
import com.pronted.dto.student.StudentProfileResponse
import com.pronted.presentation.feepayments.FeePaymentsViewModel
import com.pronted.presentation.home.ChildActivity
import com.pronted.presentation.listener.PayAmountChangeListener
import com.pronted.utils.extentions.*
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import io.paperdb.Paper
import kotlinx.coroutines.flow.collectLatest
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProceedFeePaymentFragment : BaseFragment<FragmentProceedFeePaymentBinding>() {
    private val feePaymentsViewModel: FeePaymentsViewModel by viewModel()
    private lateinit var feeHeadAdapter: FeeHeadAdapter
    private var studentProfile: StudentProfileResponse? = null
    private var feesDetailModel: FeesDetailModel? = null
    private val args: ProceedFeePaymentFragmentArgs by navArgs()
    private var razorpayPaymentRequest = RazorPayPaymentRequest()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_proceed_fee_payment
        ) as FragmentProceedFeePaymentBinding
        return binding.root
    }

    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.fee_amp_payment))
    }

    override fun init() {
        studentProfile = args.studentProfile
        feesDetailModel = args.feeDetailModel

        if (studentProfile == null && feesDetailModel == null) {
            studentProfile = arguments?.getSerializable(NAV_OBJECT) as StudentProfileResponse
            feesDetailModel = arguments?.getSerializable(NAV_OBJECT2) as FeesDetailModel?
        }
        context?.let { context ->
            binding.run {
                Trace.i("StudentContact" + studentProfile?.studentContact)
                Trace.i("" + feesDetailModel?.academicYear)
                (context as ChildActivity).setPaymentStatusListener(paymentListener)
                feesDetailModel?.let {

                    tvPay.setOnClickListener {
                        validateAndProceedPayment()
                    }

                    //set fee head adapter
                    val filteredFeeHeadList = it.feeHeadData.filter { feeHeadData ->
                        (feeHeadData.feeTerms.filter { feeTermData ->
                            feeTermData.dueAmount > 0
                        } as ArrayList<FeeTermData>).size > 0
                    } as ArrayList<FeeHeadData>

                    hasFeeHeads = filteredFeeHeadList.isNotEmpty()
                    rvFeeHeads.setHasFixedSize(true)
                    rvFeeHeads.layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.VERTICAL, false
                    )
                    feeHeadAdapter = FeeHeadAdapter(context, true, payAmountChangeListener)
                    rvFeeHeads.adapter = feeHeadAdapter
                    feeHeadAdapter.list = filteredFeeHeadList
                }
            }

        }
    }


    private fun validateAndProceedPayment() {

        if (payTotal > 0F) {
            if (Paper.book().read<UserAppList>(USER_SELECTED_ROLE)?.appName == SMART_PARENT) {
                if (studentProfile?.studentContact?.primaryParentMobile.isNullOrEmpty() || studentProfile?.studentContact?.primaryParentEmail.isNullOrEmpty()) {
                    showEmailPhonePopup()
                } else {
                    studentProfile?.let {
                        val model = PaymentGatewayOrderRequest()
                        model.customerName = it.fullName
                        model.customerId = it.studentProfileId.toString()
                        model.customerMobile = it.studentContact?.primaryParentMobile ?: ""
                        model.amount = payTotal.toInt()
                        model.customerEmail = it.studentContact?.primaryParentEmail ?: ""
                        model.product = "FEE_PAYMENT"
                        for (data in selectedFeeTermList) {
                            val paymentSplits = PaymentSplits()
                            paymentSplits.name = it.fullName
                            paymentSplits.studentFeeId = data.studentFeeId.toLong()
                            paymentSplits.feeHead = data.feeHead.headName
                            paymentSplits.feeTerm = data.feeTerm.feeTerm
                            paymentSplits.feeHeadSetupId = data.feeHead.feeHeadSetupId.toLong()
                            paymentSplits.amount = data.paidAmount.toInt()
                            model.paymentSplits.add(paymentSplits)
                        }
                        createOrderForRazorPayPayment(model)
                    }
                }
            } else {
                studentProfile?.let {
                    feesDetailModel?.studentId = it.studentProfileId
                    feesDetailModel?.processingAmount = payTotal
                    val bundle = bundleOf(
                        Pair(NAV_OBJECT, feesDetailModel)
                    )
                    (context as ChildActivity).getNavController()
                        .navigate(R.id.proceedManualFeePayment, bundle)
                }
            }
        } else
            toast(getString(R.string.error_select_valid_amount))
    }


    private fun createOrderForRazorPayPayment(model: PaymentGatewayOrderRequest) {
        lifecycleScope.launchWhenResumed {
            showLoader()
            Preference.instance.putBoolean(DYNAMIC_HEADERS, false)
            feePaymentsViewModel.createRazorpayOrder(model).collectLatest { orderCreationAction ->
                dismissLoader()
                when (orderCreationAction) {
                    is OrderCreationAction.Success -> {
                        Trace.e("Razor Pay payment Request: " + orderCreationAction.paymentRequest)
                        razorpayPaymentRequest = orderCreationAction.paymentRequest
                        launchRazorPayPayment()
                    }
                    is OrderCreationAction.Fail -> {
                        toast(
                            orderCreationAction.errorResponse.message ?: getString(
                                R.string.something_went_wrong_try_again
                            )
                        )
                    }
                }
            }
        }
    }

    private fun launchRazorPayPayment() {
        val checkout = Checkout()
        checkout.setKeyID(RAZOR_TEST_KEY)
        checkout.setImage(R.drawable.aksharlogo)
        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            val options = JSONObject()
            options.put("name", "Akshar Schools")
            options.put("description", "Akshar Schools")
            //options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("order_id", razorpayPaymentRequest.orderId) //from response of step 3.
            //options.put("theme.color", "#3399cc")
            options.put("currency", razorpayPaymentRequest.currency)
            // amount can be fetched from orderId if provided
            options.put(
                "amount",
                razorpayPaymentRequest.amount * 100
            ) //pass amount in currency subunits
            options.put("prefill.email", razorpayPaymentRequest.customerEmail)
            options.put("prefill.contact", razorpayPaymentRequest.customerMobileNumber)
            checkout.open(context as BaseActivity<*>, options)
        } catch (e: Exception) {
            Log.e("Razorpay Tag", "Error in starting Razorpay Checkout", e)
        }
    }

    private val paymentListener = object : PaymentResultWithDataListener {
        override fun onPaymentSuccess(response: String?, paymentData: PaymentData?) {
            Log.e(
                "RazorPay",
                " - OnPaymentSuccess : ${paymentData?.paymentId}, ${paymentData?.orderId}, ${paymentData?.signature}"
            )
            paymentData?.let { paymentData ->
                razorpayPaymentRequest.paymentId = paymentData.paymentId
                razorpayPaymentRequest.signature = paymentData.signature
            }
            toast("Payment success")
            processOrderPostPayment(razorpayPaymentRequest)
            /*PaymentSuccessDialogFragment.newInstance(razorpayPaymentRequest).show(
                (context as ChildActivity).supportFragmentManager, "Fee Payment"
            )*/
        }

        override fun onPaymentError(errorCode: Int, response: String?, paymentData: PaymentData?) {
            try {
                paymentData?.let {
                    razorpayPaymentRequest.paymentId = it.paymentId
                    razorpayPaymentRequest.orderId = it.orderId
                }
                razorpayPaymentRequest.errorCode = errorCode
                razorpayPaymentRequest.errorResponse = response
               /* PaymentFailureDialogFragment.newInstance(razorpayPaymentRequest).show(
                    (context as BaseActivity<*>).supportFragmentManager, "Fee Payment"
                )*/
                processOrderPostPayment(razorpayPaymentRequest, false)
            } catch (ex: Exception) {
                Log.d("Tag", "Payment Exception:$ex")
                toast("Payment Failed")
            }
        }
    }


    private fun processOrderPostPayment(
        paymentResponse: RazorPayPaymentRequest,
        isPaymentSuccess: Boolean = true
    ) {
        lifecycleScope.launchWhenResumed {
            paymentResponse.let {
                feePaymentsViewModel.processOrderPostPayment(
                    if (isPaymentSuccess)
                        RazorPayPaymentResponse(
                            it.orderId,
                            it.paymentId,
                            it.signature
                        ) else RazorPayPaymentResponse(
                        it.orderId,
                        it.paymentId,
                        "",
                        it.errorCode
                    )
                ).collectLatest { postPaymentAction ->
                    dismissLoader()
                    when (postPaymentAction) {
                        is PostPaymentAction.Success -> {
                            Trace.e("Post order payment: " + postPaymentAction.orderResponse)
                            //invoiceUrl =  postPaymentAction.orderResponse.invoiceUrl
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


    private fun showEmailPhonePopup() {
        context?.let { context ->
            val contactModel: ContactModel by lazy {
                ContactModel()
            }
            val dialogBinding = DialogEmailPhoneBinding.inflate(layoutInflater)
            val dialog = Dialog(context).apply {
                setContentView(dialogBinding.root)
                window?.setLayout(
                    Constraints.LayoutParams.MATCH_PARENT,
                    Constraints.LayoutParams.WRAP_CONTENT
                )
            }
            dialog.show()
            dialogBinding.run {
                studentProfile?.let {
                    contactModel.setData(
                        it.studentContact?.primaryParentEmail,
                        it.studentContact?.primaryParentMobile
                    )
                }
                contact = contactModel
                imgCancel.setOnClickListener {
                    dialog.dismiss()
                }
                tvProceed.setOnClickListener {
                    if (contactModel.isValid(context)) {
                        dialog.dismiss()
                        studentProfile?.let {
                            it.studentContact?.primaryParentEmail = contactModel.email()
                            it.studentContact?.primaryParentMobile = contactModel.getNumber()
                        }
                        validateAndProceedPayment()
                    }
                }
            }
        }

    }

    private var payTotal = 0F
    private var selectedFeeTermList: ArrayList<FeeTermData> = arrayListOf()
    private val payAmountChangeListener = object : PayAmountChangeListener<Float, FeeTermData> {
        override fun onAddPayment(amount: Float, model: FeeTermData) {
            Trace.e("Add Amount $amount")
            feesDetailModel?.let {
                if (payTotal == 0F)
                    payTotal = amount
                else
                    payTotal += amount
                for (mModel in it.feeHeadData) {
                    if (mModel.feeTerms.contains(model)) {
                        model.isSelected = true
                        model.paidAmount = amount
                    }
                }
                selectedFeeTermList.add(model)
                binding.tvTotalAmount.text =
                    String.format(getString(R.string.rupee_symbol_format), payTotal.toInt())
            }

        }

        override fun onRemovePayment(amount: Float, model: FeeTermData) {
            Trace.e("Remove Amount $amount")
            feesDetailModel?.let {
                if (payTotal != 0F) {
                    payTotal -= amount
                }
                for (mModel in it.feeHeadData) {
                    if (mModel.feeTerms.contains(model)) {
                        model.isSelected = false
                        model.paidAmount = 0F
                    }
                }
                selectedFeeTermList.remove(model)
                binding.tvTotalAmount.text =
                    String.format(getString(R.string.rupee_symbol_format), payTotal.toInt())
            }
        }
    }


}