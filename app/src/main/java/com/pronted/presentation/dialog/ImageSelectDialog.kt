package com.pronted.presentation.dialog

import android.content.Context
import android.os.Bundle
import android.view.Window
import com.base.BaseDialog
import com.base.inflateDialog
import com.pronted.R
import com.pronted.databinding.DialogImagePickBinding
import com.pronted.presentation.listener.ImageSelectListener

/**
 * Created by Vinay.
 * Copyright (c) 2021  EzeeTech . All rights reserved.
 */

class ImageSelectDialog(context: Context, private var imageSelectListener: ImageSelectListener) : BaseDialog<DialogImagePickBinding>(context) {

    private var isl: ImageSelectListener = object : ImageSelectListener {
        override fun delete() {
            imageSelectListener.delete()
            dismiss()
        }

        override fun picker() {
            imageSelectListener.picker()
            dismiss()
        }

        override fun camera() {
            imageSelectListener.camera()
            dismiss()
        }
    }

    init {
        this.setCancelable(true)
        this.setCanceledOnTouchOutside(true)
    }

    public override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = inflateDialog(context, R.layout.dialog_image_pick) as DialogImagePickBinding
        setContentView(root)
        //binding.callback = isl
    }

    fun imageAvailable(available: Boolean){
        binding.imageAvailable = available
    }
}