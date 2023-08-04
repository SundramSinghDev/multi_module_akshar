/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2022 Vinay. All rights reserved.
 *
 */

package com.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import com.base.permission.Permission
import com.base.permission.PermissionCallback
import com.base.permission.PermissionUtils
import com.google.android.material.snackbar.Snackbar
import com.kaopiz.kprogresshud.KProgressHUD


open class BaseFragment<T : ViewDataBinding> : Fragment() {
    protected lateinit var binding: T
    protected val root: View by lazy {
        binding.root
    }
    private val dialogs = HashSet<Dialog>()
    private val observers = HashSet<LifecycleObserver>()
    private var permissionCallback: PermissionCallback? = null
    private lateinit var hud: KProgressHUD

    protected fun observeClick(view: View) {
        view.setOnTouchListener { _, _ -> true }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        attach(context)
    }
    open fun attach(context: Context) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        displayBundleValues(arguments)
        // This callback will only be called when MyFragment is at least Started.
        /*val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true *//* enabled by default *//*) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            }*/
        // requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        create(savedInstanceState)

    }

    open fun create(savedInstanceState: Bundle?) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    open fun init() {}

    override fun onResume() {
        super.onResume()
        resume()
    }

    open fun resume() {}

    override fun onPause() {
        if (isRemoving) {
            clean()
        }
        pause()
        super.onPause()
    }

    open fun pause() {}

    override fun onStart() {
        super.onStart()
        start()
    }

    open fun start() {}

    override fun onStop() {
        stop()
        super.onStop()
    }

    open fun stop() {}

    open fun destroy() {}
    open fun destroyView() {}

    override fun onDestroyView() {
        destroyView()
        super.onDestroyView()
    }

    override fun onDestroy() {
        destroy()
        clean()
        super.onDestroy()
        System.gc()
        Runtime.getRuntime().gc()
    }

    private fun clean() {
        for (dialog in dialogs) {
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }
        dialogs.clear()
        for (observer in observers) {
            lifecycle.removeObserver(observer)
        }
        observers.clear()
    }

    protected fun addLifecycleObserver(lifecycleObserver: LifecycleObserver) {
        lifecycle.addObserver(lifecycleObserver)
        observers.add(lifecycleObserver)
    }

    fun addDialog(dialog: Dialog): Dialog {
        if (!dialogs.add(dialog) && dialog.isShowing) {
            dialog.dismiss()
        }
        return dialog
    }

    protected fun removeDialog(dialog: Dialog) {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
        dialogs.remove(dialog)
    }

    protected fun showLoader() {
        context?.let {
            hud = KProgressHUD.create(it)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setAnimationSpeed(2)
            hud.show()
        }
    }

    protected fun dismissLoader() {
        if (this::hud.isInitialized) {
            hud.dismiss()
        }

    }

    fun requestPermission(permission: Permission, permissionCallback: PermissionCallback?) {
        this.permissionCallback = permissionCallback
        context?.let {
            if (permissionCallback != null && !isRemoving) {
                if (!PermissionUtils.isGranted(it, permission)) {
                    permReqLauncher.launch(
                        arrayOf(permission.toString())
                    )
                } else {
                    this.permissionCallback?.onPermissionResult(true, false)
                }
            }
        }
    }

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            onPermissionsResult(permissions)
            /*val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                displayCameraFragment()
            }*/
        }

    private fun onPermissionsResult(permissions: Map<String, Boolean>) {
        for ((permission, result) in permissions) {
            activity?.let {
                permissionCallback?.onPermissionResult(
                    result,
                    !PermissionUtils.shouldShowRequestPermissionRationale(
                        it,
                        permission
                    )
                )
            }
        }
    }

    fun showSnackBar(message: String, anchorView: View) {
        snackBar(
            message,
            anchorView
        ).show()
    }

    fun showSnackBar(message: String) {
        snackBar(message).show()
    }

    fun showSnackBar(message: String, duration: Int) {
        snackBar(message, duration).show()
    }

    fun snackBar(message: String, anchorView: View): Snackbar =
        Snackbar.make(
            (context as Activity).findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        ).setAnchorView(anchorView)

    fun snackBar(message: String): Snackbar =
        Snackbar.make(
            (context as Activity).findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG
        )

    fun snackBar(message: String, duration: Int): Snackbar =
        Snackbar.make((context as Activity).findViewById(android.R.id.content), message, duration)

    fun toast(message: String) = Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}