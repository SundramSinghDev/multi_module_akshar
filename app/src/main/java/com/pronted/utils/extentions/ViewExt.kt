package com.pronted.utils.extentions

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
import com.bumptech.glide.Glide

fun AppCompatEditText.onTextChanged(onTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //
        }

        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            onTextChanged.invoke(s.toString())
        }

        override fun afterTextChanged(p0: Editable?) {
            //
        }
    })
}

/**
 * Visible
 */
fun View.visible() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}

/**
 * In visible
 */
fun View.inVisible() {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
}

/**
 * Gone
 */
fun View.gone() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

/**
 * Display
 *
 * @param isVisible
 */
fun View.display(isVisible: Boolean) {
    if (isVisible) {
        visible()
    } else {
        gone()
    }
}

fun View.loadImage(url: String?, defaultImage: Int) {
    Glide.with(this.context)
        .load(url)
        .error(defaultImage)
        .into(this as ImageView)
}