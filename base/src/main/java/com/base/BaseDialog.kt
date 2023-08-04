/*
 * *
 *  * Created by Vinay.
 *  * Copyright (c) 2022 Vinay. All rights reserved.
 *
 */

package com.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.databinding.ViewDataBinding


open class BaseDialog<T : ViewDataBinding> : Dialog {
    protected lateinit var binding: T
    protected val root: View by lazy {
        binding.root
    }

    protected constructor(context: Context) : super(context) {
    }

    protected constructor(context: Context, themeResId: Int) : super(context, themeResId) {
    }

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {
    }
}