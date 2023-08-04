package com.pronted.presentation.feepayments.fees

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import com.pronted.R
import com.pronted.utils.extentions.getNationalMobileNumber
import com.pronted.utils.extentions.isEmailAddress
import com.pronted.utils.extentions.isMobileNumber

class ContactModel : BaseObservable() {
    private val email = ObservableField<String>()
    private val mobile = ObservableField<String>()


    val emailError = ObservableField<String>()
    val mobileError = ObservableField<String>()



    fun getEmail(): ObservableField<String> {
        emailError.set(null)
        return email
    }

     fun email(): String {
        email.get()?.let {
            return it
        }
        return ""
    }

    fun getMobile(): ObservableField<String> {
        mobileError.set(null)
        return mobile
    }

    private fun mobile(): String {
        mobile.get()?.let {
            return "+91${it}"
        }
        return ""
    }

    private fun nationalMobileNumber(number: String): String {
        return number.getNationalMobileNumber()
    }

    fun getNumber():String{
        return nationalMobileNumber(mobile())
    }


    fun setData(email:String?, phone:String?){
        this.email.set(email)
        this.mobile.set(phone)
    }
    fun isValid(context: Context?): Boolean {
        emailError.set(null)
        mobileError.set(null)

        return if (email().isNotBlank() && email().isEmailAddress() &&
            mobile().isNotBlank() && mobile().isMobileNumber()
        ) {
            true
        } else {
            if (email().isEmpty()) {
                emailError.set(context?.getString(R.string.enter_email))
            } else if (!email().isEmailAddress()) {
                emailError.set(context?.getString(R.string.valid_email))
            }
            if (mobile().isBlank()) {
                mobileError.set(context?.getString(R.string.empty_mobile_number))
            } else if (!mobile().isMobileNumber()) {
                mobileError.set(context?.getString(R.string.valid_mobile_number))
            }
            false
        }
    }
}