/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2022 Vinay. All rights reserved.
 *
 */

package com.base

import android.app.Application
import android.content.Context
import androidx.core.content.edit


class Preference {
    private val pref by lazy {
        application.getSharedPreferences("govt_assets", Context.MODE_PRIVATE)
    }

    fun getInt(key: String): Int {
        return getInt(key, -1)
    }

    fun getInt(key: String, value: Int): Int {
        return pref.getInt(key, value)
    }

    fun putInt(key: String, value: Int) {
        pref.edit { putInt(key, value) }
    }

    fun getLong(key: String): Long? {
        return getLong(key, -1)
    }

    fun getLong(key: String, defValue: Long): Long {
        return pref.getLong(key, defValue)
    }

    fun putLong(key: String, value: Long) {
        pref.edit { putLong(key, value) }
    }

    fun getString(key: String): String? {
        return getString(key, null)
    }

    fun getString(key: String, value: String?): String? {
        return pref.getString(key, value)
    }

    fun putString(key: String, value: String) {
        pref.edit { putString(key, value) }
    }

    fun getBoolean(key: String): Boolean {
        return getBoolean(key, false)
    }

    fun getBoolean(key: String, value: Boolean): Boolean {
        return pref.getBoolean(key, value)
    }

    fun putBoolean(key: String, value: Boolean) {
        pref.edit { putBoolean(key, value) }
    }

    fun clear() {
        pref.edit { clear() }
    }

    fun remove(key: String) {
        pref.edit { remove(key) }
    }

    companion object {
        private lateinit var application: Application
        private lateinit var preference: Preference
        val instance: Preference
            @Synchronized get() {
                return preference
            }

        fun init(app: Application) {
            application = app
            preference = Preference()
        }
    }

}




