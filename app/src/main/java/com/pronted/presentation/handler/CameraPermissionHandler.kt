package com.pronted.presentation.handler

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.base.permission.Permission
import com.base.permission.PermissionCallback
import com.base.permission.PermissionUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pronted.R


class CameraPermissionHandler constructor(
    private val context: Context?,
    private val permissionHandler: PermissionHandler
) : LifecycleObserver {

    fun isPermissionGranted(): Boolean =
        context?.let {
            PermissionUtils.isGranted(it, Permission.CAMERA)
                    &&
                    PermissionUtils.isGranted(it, Permission.WRITE_STORAGE)
        } == true

    var isPresenting = false
        private set

    private var cameraPermissionDialog: AlertDialog? = null
    private var storagePermissionDialog: AlertDialog? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun bind() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun unbind() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        cameraPermissionDialog?.dismiss()
        storagePermissionDialog?.dismiss()
    }

    private val cameraPermissionCallback = object : PermissionCallback {
        override fun onPermissionResult(granted: Boolean, neverAsk: Boolean) {
            if (!granted) {
                reRequestCameraPermission(neverAsk)
            } else {
                isPresenting = false
                requestStoragePermission()
            }
        }
    }

    private val storagePermissionCallback = object : PermissionCallback {
        override fun onPermissionResult(granted: Boolean, neverAsk: Boolean) {
            if (!granted) {
                reRequestStoragePermission(neverAsk)
            } else {
                isPresenting = false
                permissionHandler.permissionGranted(Permission.WRITE_STORAGE)
            }
        }
    }

    private fun reRequestCameraPermission(neverAsk: Boolean) {
        cameraPermissionDialog?.dismiss()
        cameraPermissionDialog = null
        context?.let {
            val dialog =
                MaterialAlertDialogBuilder(context).setTitle(context.getString(R.string.permission_required))
                    .setMessage(context.getString(R.string.camera_for_picture))
            if (neverAsk) {
                dialog.setMessage(
                    "${context.getString(R.string.camera_for_picture)}\n${context.getString(
                        R.string.turn_on_permissions_at_setting
                    )}"
                )
            }
            dialog.setCancelable(false)
            dialog.setPositiveButton(
                if (neverAsk) context.getString(R.string.settings) else context.getString(R.string.request)
            ) { view, _ ->
                if (neverAsk) {
                    view.dismiss()
                    PermissionUtils.openApplicationSettings(context)
                    isPresenting = false
                } else {
                    requestCameraPermission()
                }
            }
            dialog.setNegativeButton(context.getString(R.string.cancel)) { view, _ ->
                view.dismiss()
                isPresenting = false
            }
            cameraPermissionDialog = dialog.show()
        }
    }

    private fun reRequestStoragePermission(neverAsk: Boolean) {
        storagePermissionDialog?.dismiss()
        storagePermissionDialog = null
        context?.let {
            val dialog =
                MaterialAlertDialogBuilder(context).setTitle(context.getString(R.string.permission_required))
                    .setMessage(context.getString(R.string.storage_for_picture))
            if (neverAsk) {
                dialog.setMessage(
                    "${context.getString(R.string.storage_for_picture)}\n${context.getString(
                        R.string.turn_on_permissions_at_setting
                    )}"
                )
            }
            dialog.setCancelable(false)
            dialog.setPositiveButton(
                if (neverAsk) context.getString(R.string.settings) else context.getString(R.string.request)
            ) { view, _ ->
                if (neverAsk) {
                    view.dismiss()
                    PermissionUtils.openApplicationSettings(context)
                    isPresenting = false
                } else {
                    requestStoragePermission()
                }
            }
            dialog.setNegativeButton(context.getString(R.string.cancel)) { view, _ ->
                view.dismiss()
                isPresenting = false
            }
            storagePermissionDialog = dialog.show()
        }
    }

    private fun requestCameraPermission() {
        isPresenting = true
        permissionHandler.request(Permission.CAMERA, cameraPermissionCallback)
    }

    private fun requestStoragePermission() {
        isPresenting = true
        permissionHandler.request(Permission.WRITE_STORAGE, storagePermissionCallback)
    }

    fun requestPermission() {
        if (!isPresenting) {
            requestCameraPermission()
        }
    }
}