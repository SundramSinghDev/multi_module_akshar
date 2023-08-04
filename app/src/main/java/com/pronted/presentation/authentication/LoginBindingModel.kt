package com.pronted.presentation.authentication

import android.content.Context
import androidx.databinding.ObservableField
import com.pronted.R
import com.pronted.utils.extentions.getNationalMobileNumber
import com.pronted.utils.extentions.isMobileNumber

class LoginBindingModel {
    private val mobileNumber = ObservableField<String>()

    val mobileNumberError = ObservableField<String>()


    fun getMobileNumber(): ObservableField<String> {
        mobileNumberError.set(null)
        return mobileNumber
    }

    private fun mobileNumber(): String {
        mobileNumber.get()?.let {
            return "+91${it}"
        }
        return ""
    }

    fun isValid(context: Context?): Boolean {
        mobileNumberError.set(null)
        if (mobileNumber().isNotBlank() && mobileNumber().isMobileNumber()
        ) {
            return true
        } else {
            if (mobileNumber().isBlank()) {
                mobileNumberError.set(context?.getString(R.string.empty_mobile_number))
            } else if (!mobileNumber().isMobileNumber()) {
                mobileNumberError.set(context?.getString(R.string.valid_mobile_number))
            }
        }
        return false
    }

    private fun nationalMobileNumber(number: String): String {
        return number.getNationalMobileNumber()
    }

    fun getNumber():String{
        return nationalMobileNumber(mobileNumber())
    }
}