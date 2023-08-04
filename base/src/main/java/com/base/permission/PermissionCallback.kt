package com.base.permission

/**
 * Created by Vinay
 * Copyright (c) 2022 Vinay. All rights reserved.
 */

interface PermissionCallback {
    fun onPermissionResult(granted: Boolean, neverAsk: Boolean)
}