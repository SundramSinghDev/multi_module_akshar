/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2022 Vinay. All rights reserved.
 *
 */

package com.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.localbroadcastmanager.content.LocalBroadcastManager


fun displayBundleValues(bundle: Bundle?) {
    val keys = bundle?.keySet()
    if (keys != null) {
        val stringBuilder = StringBuilder()
        stringBuilder.append("IntentDump \n")
        stringBuilder.append("-------------------------------------------------------------\n")
        for (key in keys) {
            stringBuilder.append(key).append("=").append(bundle.get(key)).append("\n")
        }
        stringBuilder.append("-------------------------------------------------------------")
        Trace.i(stringBuilder.toString())
    } else {
        Trace.i("\n-------------------------\nIntentDump is null\n-------------------------")
    }
}

fun displayIntentAction(intent: Intent?) {
    intent?.action?.let {
        Trace.i("\n-------------------------\nIntent Action is $it \n-------------------------")
    }
}

fun inflateFragment(
    inflater: LayoutInflater,
    container: ViewGroup?,
    layout: Int
): ViewDataBinding? {
    return DataBindingUtil.inflate(inflater, layout, container, false);
}

fun inflateActivity(activity: Activity, layout: Int): ViewDataBinding? {
    return DataBindingUtil.setContentView(activity, layout)
}

fun inflateDialog(context: Context, layout: Int): ViewDataBinding? {
    return DataBindingUtil.inflate(LayoutInflater.from(context), layout, null, false)
}

fun hideKeyboard(activity: Activity?) {
    if (activity != null && activity.window != null) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
    }
}

fun showKeyboard(view: View?) {
    if (view != null) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }
}

fun genericMessage(type: String, result: Boolean, send: Boolean = false) {
    val intent = Intent()
    intent.action = GENERIC
    intent.putExtra(RESULT, type)
    intent.putExtra(STATUS, result)
    LocalBroadcastManager.getInstance(BaseApp.getApplication())
        .sendBroadcast(intent)
    if (send) {
        message(type, result)
    }
}

fun message(action: String, result: Boolean = true) {
    val intent = Intent()
    intent.action = action
    intent.putExtra(STATUS, result)
    LocalBroadcastManager.getInstance(BaseApp.getApplication())
        .sendBroadcast(intent)
}

const val PERMISSION_REQUEST_CODE = 27
const val REQUEST_LOCATION = 225
