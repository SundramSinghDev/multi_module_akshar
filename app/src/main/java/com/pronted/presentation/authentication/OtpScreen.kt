package com.pronted.presentation.authentication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pronted.presentation.userapps.SwitchRoleActivity
import com.pronted.R
import com.pronted.databinding.FragmentOtpScreenBinding
import com.pronted.dto.login.LoginAction
import com.pronted.dto.login.OtpAction
import com.pronted.utils.DateUtil
import com.pronted.utils.extentions.*
import com.base.BaseFragment
import com.base.Preference
import com.base.inflateFragment
import io.paperdb.Paper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class OtpScreen : BaseFragment<FragmentOtpScreenBinding>() {

    private val viewModel: OtpViewModel by viewModel()
    private var mobileNumber: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater,
            container,
            R.layout.fragment_otp_screen
        ) as FragmentOtpScreenBinding
        observeClick(root)
        return binding.root
    }

    @SuppressLint("HardwareIds")
    override fun init() {
        super.init()
        mobileNumber = arguments?.getString(NAV_KEY_MOBILE_NUMBER)
        setInputListeners()
        binding.run {
            mobileNumber?.let {
                tvText.text = String.format(
                    Locale.getDefault(),
                    getString(R.string.we_have_sent_you_sms_on),
                    it
                )
            }
            llSubmit.isEnabled = false

            llSubmit.setOnClickListener {
                callValidateOtp()
            }

            tvGoBack.setOnClickListener {
                findNavController().navigateUp()
            }

            tvResendOtp.setOnClickListener {
                callLogin()
            }
        }
    }

    private fun callValidateOtp() {
        binding.run {
            lifecycleScope.launchWhenResumed {
                showLoader()
                val deviceId = context?.let { DateUtil.getDeviceID(it) }
                val otp = StringBuilder().append(opt1.text.toString())
                    .append(opt2.text.toString())
                    .append(opt3.text.toString())
                    .append(opt4.text.toString())
                    .append(opt5.text.toString())
                    .append(opt6.text.toString())

                viewModel.validateOtp(
                    mobileNumber = mobileNumber.orEmpty(), otp = otp.toString(), deviceId =
                    deviceId ?: ""
                ).flowOn(Dispatchers.IO).collectLatest { otpAction ->
                    dismissLoader()
                    when (otpAction) {
                        is OtpAction.Success -> {
                            context?.let {
                                Preference.instance.putString(USER_MOBILE, mobileNumber.orEmpty())
                                Paper.book().write(USER_AUTH_MODEL, otpAction.loginResponseModel)
                                Preference.instance.putBoolean(IS_LOGGED_IN, true)
                                context?.let {
                                    startNewActivity(
                                        it, SwitchRoleActivity::class.java, finish = true
                                    )
                                }
                            }
                        }
                        is OtpAction.Fail -> {
                            Log.d("Akshar", "Failed")
                            toast(
                                otpAction.errorResponse.message ?: getString(
                                    R.string
                                        .something_went_wrong_try_again
                                )
                            )
                        }
                    }
                }
            }
        }
    }



    private fun setInputListeners() {
        binding.run {
            opt1.onTextChanged {
                validateInputs()
                if (it.isNotEmpty()) {
                    opt2.requestFocus()
                }
                validateInputs()
            }
            opt2.onTextChanged {
                if (it.isNotEmpty()) {
                    opt3.requestFocus()
                } else {
                    opt1.requestFocus()
                }
                validateInputs()
            }
            opt3.onTextChanged {
                if (it.isNotEmpty()) {
                    opt4.requestFocus()
                } else {
                    opt2.requestFocus()
                }
                validateInputs()
            }
            opt4.onTextChanged {
                if (it.isNotEmpty()) {
                    opt5.requestFocus()
                } else {
                    opt3.requestFocus()
                }
                validateInputs()
            }
            opt5.onTextChanged {
                if (it.isNotEmpty()) {
                    opt6.requestFocus()
                } else {
                    opt4.requestFocus()
                }
                validateInputs()
            }
            opt6.onTextChanged {
                if (it.isEmpty()) {
                    opt5.requestFocus()
                }
                validateInputs()
            }

            opt6.setOnEditorActionListener { textView, actionId, keyEvent ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    callValidateOtp()
                    true
                }
                false
            }
        }
    }

    private fun validateInputs() {
        binding.run {
            llSubmit.isEnabled =
                opt1.text?.isNotEmpty() == true && opt2.text?.isNotEmpty() == true
                        && opt3.text?.isNotEmpty() == true && opt4.text?.isNotEmpty() == true &&
                        opt5.text?.isNotEmpty() == true && opt6.text?.isNotEmpty() == true
        }
    }

    private fun callLogin() {
        binding.run {
            lifecycleScope.launchWhenResumed {
                mobileNumber?.let {
                    showLoader()
                    viewModel.loginWithPhoneNumber(it, otpFor = "LOGIN")
                        .flowOn(Dispatchers.IO).collectLatest { loginAction ->
                            dismissLoader()
                            when (loginAction) {
                                is LoginAction.Success -> {
                                    toast(
                                        loginAction.otpResponseModel.message
                                            ?: getString(R.string.otp_sent_successfully)
                                    )
                                }
                                is LoginAction.Fail -> {
                                    toast(getString(R.string.something_went_wrong_try_again))
                                }
                            }
                        }
                }
            }
        }
    }

    /**
     * Fetch employee details
     */
    /*private fun fetchEmployeeDetails() {
        lifecycleScope.launchWhenResumed {
            Preference.instance.getString(USER_ID)?.let { employeeProfileId ->
                viewModel.fetchEmployeeDetails(employeeProfileId)
                    .collectLatest { employeeDetailAction ->
                        dismissLoader()
                        when (employeeDetailAction) {
                            is EmployeeDetailAction.Success -> {
                                context?.let {
                                    startNewActivity(
                                        it, HomeActivity::class.java, bundle = bundleOf(
                                            Pair(IS_THROUGH_LOGIN, true)
                                        ), finish = true
                                    )
                                }
                            }
                            is EmployeeDetailAction.Fail -> {
                                toast(
                                    employeeDetailAction.errorResponse.message ?: getString(
                                        R
                                            .string
                                            .something_went_wrong_try_again
                                    )
                                )
                            }
                        }
                    }
            }
        }
    }*/
}