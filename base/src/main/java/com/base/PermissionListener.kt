package com.base

import com.base.permission.Permission


interface PermissionListener {
    fun permissionGranted(permission: Permission)
}