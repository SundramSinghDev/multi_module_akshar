package com.base.handler

import com.base.permission.Permission
import com.base.permission.PermissionCallback

interface PermissionHandler {
    fun request(permission: Permission, permissionCallback: PermissionCallback)
    fun permissionGranted(permission: Permission)
}