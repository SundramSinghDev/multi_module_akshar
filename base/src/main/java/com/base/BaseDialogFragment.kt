package com.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kaopiz.kprogresshud.KProgressHUD

open class BaseDialogFragment<T : ViewDataBinding> : BottomSheetDialogFragment() {
    protected lateinit var binding: T
    protected val root: View by lazy {
        binding.root
    }
    private lateinit var hud: KProgressHUD
    protected fun observeClick(view: View) {
        view.setOnTouchListener { _, _ -> true }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        displayBundleValues(arguments)

        create(savedInstanceState)

    }

    open fun create(savedInstanceState: Bundle?) {}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    open fun init() {}
    protected fun showLoader() {
        context?.let {
            hud = KProgressHUD.create(it)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setAnimationSpeed(2)
            hud.show()
        }
    }

    protected fun dismissLoader() {
        hud.dismiss()
    }

    fun toast(message: String) = Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}