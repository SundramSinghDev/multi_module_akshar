/**
 * Created by Vinay
 * Copyright (c) 2022 Vinay. All rights reserved.
 */

package com.base.permission

import android.Manifest


enum class Permission constructor(private var permission: String) {
    CAMERA(Manifest.permission.CAMERA),
    FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION),
    WRITE_STORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    override fun toString(): String {
        return permission
    }

    companion object {
        fun stringToPermission(stringPermission: String): Permission? {
            for (permission in Permission.values()) {
                if (stringPermission.equals(permission.toString(), ignoreCase = true))
                    return permission
            }

            return null
        }
    }
}