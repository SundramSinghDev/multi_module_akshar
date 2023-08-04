package com.pronted.presentation.feepayments.fees

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.base.BaseDialogFragment
import com.base.Trace
import com.base.inflateFragment
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.pronted.R
import com.pronted.databinding.FragmentPaymentSuccessBinding
import com.pronted.dto.feepayments.PostPaymentAction
import com.pronted.dto.feepayments.data.RazorPayPaymentRequest
import com.pronted.dto.feepayments.data.RazorPayPaymentResponse
import com.pronted.presentation.feepayments.FeePaymentsViewModel
import com.pronted.utils.extentions.NAV_OBJECT
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PaymentSuccessDialogFragment : BaseDialogFragment<FragmentPaymentSuccessBinding>() {


    private var paymentResponse: RazorPayPaymentRequest? = null
    private val feePaymentsViewModel: FeePaymentsViewModel by viewModel()
    private var invoiceUrl: String? = null

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
            R.layout.fragment_payment_success
        ) as FragmentPaymentSuccessBinding
        observeClick(root)
        return binding.root
    }

    override fun init() {
        binding.run {
            context?.let { context ->
                tvAmountPaid.text =
                    getString(R.string.rupee_symbol_format).format(paymentResponse?.amount)
                tvDownloadReceipt.setOnClickListener {
                    invoiceUrl?.let {
                        val rootDir: String = context.filesDir.absolutePath
                        val fileName =
                            "FeeReceipt_" + paymentResponse?.orderId + ".pdf"
                        val file = File("$rootDir/$fileName")
                        if (file.exists()) readInvoiceFile(
                            rootDir,
                            fileName
                        ) else getBookingInvoice(
                            it,
                            rootDir,
                            fileName
                        )
                    }
                }
                updateDownloadButtonUI()
                tvPaymentId.text = paymentResponse?.paymentId
                processOrderPostPayment()
            }
        }
    }

    private fun processOrderPostPayment() {
        lifecycleScope.launchWhenResumed {
            paymentResponse?.let {
                feePaymentsViewModel.processOrderPostPayment(
                    RazorPayPaymentResponse(
                        it.orderId,
                        it.paymentId,
                        it.signature
                    )
                ).collectLatest {
                        postPaymentAction ->
                    dismissLoader()
                    when (postPaymentAction) {
                        is PostPaymentAction.Success -> {
                            Trace.e("Post order payment: " + postPaymentAction.orderResponse)
                            invoiceUrl =  postPaymentAction.orderResponse.invoiceUrl
                            updateDownloadButtonUI()
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


    private fun dismissDialog() {
        this.dismiss()
    }

    private fun updateDownloadButtonUI() {
        binding.run {
            if (invoiceUrl != null) {
                tvDownloadReceipt.isEnabled = false
                tvDownloadReceipt.background = ResourcesCompat.getDrawable(
                    resources, R.drawable.btn_yellow_background_disabled, context?.theme
                )
            } else {
                tvDownloadReceipt.isEnabled = true
                tvDownloadReceipt.background = ResourcesCompat.getDrawable(
                    resources, R.drawable.btn_yellow_background, context?.theme
                )
            }
        }
    }

    private fun readInvoiceFile(rootDir: String, fileName: String) {
        context?.let {
            val file = File("$rootDir/$fileName")
            if (file.exists()) {
                val uri = FileProvider.getUriForFile(
                    it,
                    it.packageName + ".provider",
                    file
                )
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(uri, "application/pdf")
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_GRANT_READ_URI_PERMISSION
                startActivity(intent)
            } else {
                Log.i("Downloading Invoice", "File does not exist")
            }
        }
    }

    private fun getBookingInvoice(url: String, rootDir: String, fileName: String) {
        // showProgressDialog("Getting invoice...")
        showLoader()
        PRDownloader.download(
            url,
            rootDir,
            fileName
        ).build().setOnProgressListener { progress ->
            // Update the progress
            //progressBar.max = it.totalBytes.toInt()
            //progressBar.progress = it.currentBytes.toInt()
            Log.i(
                "Downloading Invoice",
                "progress total -" + progress.totalBytes.toString() + "," +
                        " progress current: " + progress.currentBytes
            )
        }.start(object : OnDownloadListener {
            override fun onDownloadComplete() {
                // Update the progress bar to show the completeness
                //progressBar.max = 100
                // progressBar.progress = 100
                dismissLoader()
                // Read the file
                readInvoiceFile(rootDir, fileName)
                Log.i(
                    "Downloading Invoice",
                    "Completed and downloaded at: " + context?.filesDir?.absolutePath.toString() + "/" + fileName
                )
            }

            override fun onError(error: com.downloader.Error?) {
                //dismissProgress()
                toast(
                    "Failed to download the Invoice"
                )
            }
        })
    }

    companion object {
        fun newInstance(
            model: RazorPayPaymentRequest
        ): PaymentSuccessDialogFragment =
            PaymentSuccessDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(NAV_OBJECT, model)
                }
            }
    }
}