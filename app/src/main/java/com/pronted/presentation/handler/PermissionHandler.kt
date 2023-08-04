/*
 *  Created by Vinay on 1-2-2021 for EzeeTech.
 *  Copyright (c) 2021  EzeeTech . All rights reserved.
 *
 */

package com.pronted.presentation.handler

import com.base.permission.Permission
import com.base.permission.PermissionCallback

interface PermissionHandler {
    fun request(permission: Permission, permissionCallback: PermissionCallback)
    fun permissionGranted(permission: Permission)
}