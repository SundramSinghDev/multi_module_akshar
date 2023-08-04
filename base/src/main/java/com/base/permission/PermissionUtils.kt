package com.base.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

/**
 * Created by Vinay
 * Copyright (c) 2022 Vinay. All rights reserved.
 */

object PermissionUtils {

    fun isGranted(context: Context, permission: Permission): Boolean {
        return ContextCompat.checkSelfPermission(context, permission.toString()) == PackageManager.PERMISSION_GRANTED
    }

    fun shouldShowRequestPermissionRationale(activity: Activity, permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) && !isGranted(activity, Objects.requireNonNull<Permission>(Permission.stringToPermission(permission)))
    }

    fun openApplicationSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + context.packageName)
        context.startActivity(intent)
    }
}