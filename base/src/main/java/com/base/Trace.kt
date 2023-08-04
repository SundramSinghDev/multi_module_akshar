/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2022 Vinay. All rights reserved.
 *
 */

package com.base;

import java.util.logging.Level
import java.util.logging.Logger



object Trace {
    private const val FINAL_CONSTANT_IS_LOCAL = true
    private var debug = true // false - Release, true - Debug
    private val logger = Logger.getLogger("[app]")

    private val logTagWithMethod: String
        get() {
            try {
                if (FINAL_CONSTANT_IS_LOCAL) {
                    val stack = Throwable().fillInStackTrace()
                    val trace = stack.stackTrace
                    return trace[2].className + "." + trace[2].methodName
                }
            } catch (ignored: Exception) {
            }

            return " "
        }

    init {
        debug = BuildConfig.BUILD_TYPE.contains("debug")
    }

    fun a() {
        if (debug) {
            logger.log(Level.ALL, logTagWithMethod)
        }
    }

    fun i(msg: String?) {
        if (debug) {
            logger.log(Level.INFO, "$logTagWithMethod - $msg")
        }
    }

    fun w(msg: String?) {
        if (debug) {
            logger.log(Level.WARNING, "$logTagWithMethod - $msg")
        }
    }

    fun e(msg: String?) {
        if (debug) {
            logger.log(Level.SEVERE, "$logTagWithMethod - $msg")
        }
    }
}