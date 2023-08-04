package com.pronted.presentation.feepayments.fees

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.base.BaseActivity
import com.base.BaseFragment
import com.base.Trace
import com.base.inflateFragment
import com.pronted.R
import com.pronted.databinding.FragmentPaymentInvoiceBinding
import com.pronted.presentation.home.ChildActivity
import com.pronted.presentation.listener.ChildMenuItemClickListener
import com.pronted.utils.extentions.NAV_OBJECT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class PaymentInvoiceFragment : BaseFragment<FragmentPaymentInvoiceBinding>(),
    CoroutineScope by MainScope() {
    private var invoiceUrl: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater, container, R.layout.fragment_payment_invoice
        ) as FragmentPaymentInvoiceBinding
        return binding.root
    }

    override fun init() {
        invoiceUrl = arguments?.getString(NAV_OBJECT)
        invoiceUrl?.let {
            (context as ChildActivity).showDownloadMenuItem(true)
            (context as ChildActivity).setChildMenuItemClickListener(childMenuItemClickListener)
            loadInvoiceFromUrl(it)
        }

    }

    override fun resume() {
        (activity as BaseActivity<*>).title(getString(R.string.invoice))
    }

    private val childMenuItemClickListener = object : ChildMenuItemClickListener {
        override fun onMenuItemClicked(menuItem: View) {
            if (menuItem.id == R.id.iv_download) {
                context?.let {
                    invoiceUrl?.let { url ->
                        Trace.i("Downloading invoice")
                        downloadFeeInvoice(it, url)
                        toast("Pronted App - Fee invoice downloading..")
                    }
                }
            }
        }
    }

    private fun loadInvoiceFromUrl(pdfUrl: String) {
        lifecycleScope.launchWhenResumed {
            //Run asynchronously on the main Thread.
            showLoader()
            val result = withContext(Dispatchers.IO) {
                var inputStream: InputStream? = null
                try {
                    val url = URL(pdfUrl)
                    val urlConnection: HttpURLConnection =
                        url.openConnection() as HttpsURLConnection
                    if (urlConnection.responseCode == 200) {
                        // on below line we are initializing our input stream
                        // if the response is successful.
                        inputStream = BufferedInputStream(urlConnection.inputStream)
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                inputStream
            }
            dismissLoader()
            binding.pdfView.fromStream(result).defaultPage(0).enableSwipe(true)
                .swipeHorizontal(false).enableDoubletap(true)
                .enableAnnotationRendering(false).password(null).scrollHandle(null)
                .onLoad {
                    //loadContentListener.onLoadFinished(true);
                }
                .onPageChange { page, pageCount ->
                    //titleBar.setTitle(titleName + "(" + (page + 1) + "/" + pageCount + ")");
                }
                .onError { t ->
                    //loadContentListener.onLoadFinished(false);
                    t.printStackTrace()
                }.load()
        }
    }

    private fun downloadFeeInvoice(context: Context, urlStr: String) {
        val url = Uri.parse(urlStr)
        val downloadManager =
            (context as BaseActivity<*>).getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(url)
        request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_WIFI or
                    DownloadManager.Request.NETWORK_MOBILE
        )

        // set title and description
        request.setTitle("Invoice Download")
        request.setDescription("Pronted App - Fee invoice downloading..")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        //set the local destination for download file to a path within the application's external files directory
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "${url.lastPathSegment}"
        )
        request.setMimeType("*/*")
        downloadManager.enqueue(request)
    }
}