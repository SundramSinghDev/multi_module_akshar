package com.base

import androidx.activity.result.ActivityResult

interface ActivityResultListener {
    fun onActivityResultCallback(activityResult: ActivityResult)
}