package com.pronted.presentation.home

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.base.BaseActivity
import com.base.Trace
import com.base.inflateActivity
import com.pronted.R
import com.pronted.databinding.ActivityChildBinding
import com.pronted.presentation.listener.ChildMenuItemClickListener
import com.pronted.presentation.timeline.AppDataViewModel
import com.pronted.utils.extentions.*
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChildActivity : BaseActivity<ActivityChildBinding>(), PaymentResultWithDataListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val appDataViewModel: AppDataViewModel by viewModels()
    private lateinit var childMenuItemClickListener: ChildMenuItemClickListener
    private lateinit var paymentStatusListener:PaymentResultWithDataListener
    private val navInflater by lazy {
        findNavController(R.id.nav_host_fragment_content_child).navInflater
    }
    private val navGraph by lazy { navInflater.inflate(R.navigation.child_navigation) }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = inflateActivity(this, R.layout.activity_child) as ActivityChildBinding
        setContentView(binding.root)
        init()
    }

    private fun init() {
        intent.extras?.let {
            for (key in it.keySet()) {
                if (key == NAV_DESTINATION) {
                    navGraph.setStartDestination(it.getInt(key))
                    //Trace.i("Child layer destination: viewProfessional "+ (it.getInt(key) == R.id.fragmentViewProfessional))
                }
                if (key == NAV_SOURCE) {
                    navGraph.addArgument(
                        key,
                        NavArgument.Builder().setDefaultValue(it.getInt(NAV_SOURCE)).build()
                    )
                }
                if (key == NAV_OBJECT || key == NAV_OBJECT2 || key == NAV_OBJECT3 || key == NAV_OBJECT4
                    || key == NAV_OBJECT5 || key == NAV_OBJECT6
                ) {
                    it.get(key)?.let { navObject ->
                        Trace.i("Details nav object: $navObject")
                        navGraph.addArgument(
                            key,
                            NavArgument.Builder().setDefaultValue(navObject).build()
                        )
                    }
                }
            }
        }
        navController = findNavController(R.id.nav_host_fragment_content_child)
        navController.graph = navGraph
        binding.ivBack.setOnClickListener {
            onBack()
        }
        binding.ivDownload.setOnClickListener {
            childMenuItemClickListener.onMenuItemClicked(it)
        }
    }

    override fun title(title: String) {
        binding.tvTitle.text = title
    }

    fun getNavController(): NavController {
        return navController
    }

    fun showDownloadMenuItem(show: Boolean) {
        binding.ivDownload.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setChildMenuItemClickListener(listener: ChildMenuItemClickListener) {
        childMenuItemClickListener = listener
    }

    fun setPaymentStatusListener(listener:PaymentResultWithDataListener){
        paymentStatusListener = listener
    }
    fun onBack() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun onPaymentSuccess(response: String?, paymentData: PaymentData?) {
       paymentStatusListener.onPaymentSuccess(response, paymentData)
    }

    override fun onPaymentError(errorCode: Int, response: String?, paymentData: PaymentData?) {
       paymentStatusListener.onPaymentError(errorCode, response, paymentData)
    }
}