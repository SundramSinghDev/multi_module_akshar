package com.pronted.presentation.authentication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pronted.R
import com.pronted.databinding.FragmentLoginWithPhoneNumberBinding
import com.pronted.dto.login.LoginAction
import com.pronted.utils.extentions.NAV_KEY_MOBILE_NUMBER
import com.pronted.utils.extentions.onTextChanged
import com.base.BaseFragment
import com.base.inflateFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginScreen : BaseFragment<FragmentLoginWithPhoneNumberBinding>() {

    private val viewModel: LoginViewModel by viewModel()
    val loginBindingModel: LoginBindingModel by lazy {
        LoginBindingModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateFragment(
            inflater,
            container,
            R.layout.fragment_login_with_phone_number
        ) as FragmentLoginWithPhoneNumberBinding
        observeClick(root)
        return binding.root
    }

    override fun init() {
        super.init()
        binding.run {
            model = loginBindingModel
            phoneNumber.setOnEditorActionListener { textView, actionId, keyEvent ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    context?.let {
                        if (loginBindingModel.isValid(it))
                            callLogin()
                    }

                    true
                }
                false
            }
            phoneNumber.onTextChanged {
                if (it.isEmpty())
                    mobileInputLayout.hint = getString(R.string.phone_number)
                else
                    mobileInputLayout.hint = ""
            }
            llContinue.setOnClickListener {
                context?.let {
                    if (loginBindingModel.isValid(it))
                        callLogin()
                }

            }
        }
    }

    /**
     * Call login
     */
    private fun callLogin() {
        binding.run {
            lifecycleScope.launchWhenResumed {
                showLoader()
                val mobileNumber = loginBindingModel.getNumber()
                viewModel.loginWithPhoneNumber(mobileNumber, "LOGIN")
                    .flowOn(Dispatchers.IO).collectLatest {
                        dismissLoader()
                        when (it) {
                            is LoginAction.Success -> {
                                toast(it.otpResponseModel.message ?: getString(R.string.otp_sent_successfully))
                                findNavController().navigate(
                                    R.id.action_loginScreen_otpScreen,
                                    bundleOf(NAV_KEY_MOBILE_NUMBER to mobileNumber)
                                )
                            }
                            is LoginAction.Fail -> {
                                Log.d("Akshar", "Failed")
                                toast(
                                    it.errorResponse.message ?: getString(
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

    override fun onDestroyView() {
        super.onDestroyView()
    }
}