/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2022 Vinay . All rights reserved.
 *
 */

package com.base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleObserver
import com.base.handler.LocationPermissionHandler
import com.base.handler.PermissionHandler
import com.google.android.material.snackbar.Snackbar
import com.base.permission.Permission
import com.base.permission.PermissionCallback
import com.base.permission.PermissionUtils
import com.kaopiz.kprogresshud.KProgressHUD
import java.util.*

open class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    protected lateinit var binding: T
    private val dialogs = HashSet<Dialog>()
    private val observers = HashSet<LifecycleObserver>()
    private var permissionCallback: PermissionCallback? = null
    private var currentLocationModel: CurrentLocationModel? = null
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var activityResultListener: ActivityResultListener? = null
    private lateinit var hud: KProgressHUD

    private val activityResultCallback: ActivityResultCallback<ActivityResult> =
        ActivityResultCallback {
            Trace.i("activity Result: " + it.resultCode)
            activityResultListener?.onActivityResultCallback(it)
        }

    //Location permission handling
    private val locationPermissionHandler: LocationPermissionHandler by lazy {
        LocationPermissionHandler(this, locationHandler)
    }
    private var permissionListener: PermissionListener? = null
    private val locationHandler = object : PermissionHandler {
        override fun request(
            permission: Permission,
            permissionCallback: PermissionCallback
        ) {
            requestPermission(permission, permissionCallback)
        }

        override fun permissionGranted(permission: Permission) {
            permissionListener?.permissionGranted(permission)
        }
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        displayBundleValues(intent.extras)
        displayIntentAction(intent)
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(), activityResultCallback
        )

        create(savedInstanceState)
    }

    open fun create(savedInstanceState: Bundle?) {}
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        displayBundleValues(intent?.extras)
        displayIntentAction(intent)
        newIntent(intent)
    }

    open fun newIntent(intent: Intent?) {}
    override fun onResume() {
        super.onResume()
        hideKeyboard(this)
        resume()
    }

    open fun resume() {}

    override fun onPause() {
        hideKeyboard(this)
        if (isFinishing) {
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
    override fun onDestroy() {
        destroy()
        clean()
        super.onDestroy()
    }

    open fun title(title: String) {

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

    open fun destroy() {}

    protected fun addLifecycleObserver(lifecycleObserver: LifecycleObserver) {
        lifecycle.addObserver(lifecycleObserver)
        observers.add(lifecycleObserver)
    }

    protected fun addDialog(dialog: Dialog): Dialog {
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

    fun setPermissionListener(listener: PermissionListener) {
        this.permissionListener = listener
    }

    fun requestForLocation() {
        locationPermissionHandler.requestPermission()
    }

    fun requestPermission(permission: Permission, permissionCallback: PermissionCallback?) {
        this.permissionCallback = permissionCallback
        if (permissionCallback != null && !isFinishing) {
            if (!PermissionUtils.isGranted(this, permission)) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(permission.toString()),
                    PERMISSION_REQUEST_CODE
                )
            } else {
                permissionCallback.onPermissionResult(true, false)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        onPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun onPermissionsResult(
        requestCode: Int,
        permissions: Array<String>?,
        grantResults: IntArray?
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults != null && permissions != null) {
            for (i in grantResults.indices) {
                if (permissionCallback != null) {
                    permissionCallback!!.onPermissionResult(
                        grantResults[i] == PackageManager.PERMISSION_GRANTED,
                        !PermissionUtils.shouldShowRequestPermissionRationale(this, permissions[i])
                    )
                }
            }
        }
    }

    fun showSnackBar(message: String) {
        snackBar(message).show()
    }

    private fun snackBar(message: String): Snackbar =
        Snackbar.make(
            this.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG
        )

    fun isPermissionGranted(permission: Permission): Boolean {
        return PermissionUtils.isGranted(this, permission)
    }

    fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    fun parseAddressFromLocation(location: Location) {
        if (location.hasAccuracy())
            Trace.i("Location accuracy: ${location.accuracy}")
        val gcd = Geocoder(this, Locale.getDefault())
        if (Build.VERSION.SDK_INT >= 33) {
            gcd.getFromLocation(
                location.latitude,
                location.longitude,
                1, object : Geocoder.GeocodeListener {
                    override fun onGeocode(it: MutableList<Address>) {
                        setCurrentLocation(it, location)
                    }

                    override fun onError(errorMessage: String?) {
                        super.onError(errorMessage)
                    }
                }
            )
        } else {
            val addresses = gcd.getFromLocation(location.latitude, location.longitude, 1)
            addresses?.let { setCurrentLocation(it, location) }
        }
    }

    private fun setCurrentLocation(it: MutableList<Address>, location: Location) {
        it.let {
            val address: String? = it[0].getAddressLine(0)
            val city: String? = it[0].locality
            val state: String? = it[0].adminArea
            val country: String? = it[0].countryName
            val postalCode: String? = it[0].postalCode
            val knownName: String? = it[0].featureName

            Trace.i(
                "Current Address: address - $address, city - $city, state - $state," +
                        " country - $country, postalCode - $postalCode, known name - $knownName"
            )
            currentLocationModel = CurrentLocationModel(
                address, postalCode, city, state,
                country, location.latitude, location.longitude, location.accuracy
            )
        }
    }

    fun setActivityResultListener(listener: ActivityResultListener) {
        this.activityResultListener = listener
    }

    fun launchActivityForResult(intent: Intent) {
        activityResultLauncher.launch(intent)
    }


    fun getCurrentLocation(): CurrentLocationModel? {
        return currentLocationModel
    }

    fun showLoader(context:Context) {
        hud = KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setAnimationSpeed(2)
        hud.show()

    }

    fun dismissLoader() {
        hud.dismiss()
    }
}