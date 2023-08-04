/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2022 Vinay. All rights reserved.
 *
 */

package com.base;

import android.app.Application

object BaseApp {
    private lateinit var application: Application

    fun init(app: Application) {
        application = app
        Preference.init(application)
        //Paper.init(application)
    }

    fun getApplication(): Application {
        return application
    }
}

