package com.pronted.utils.databinding

import android.graphics.drawable.Drawable
import android.text.Html
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.pronted.R
import com.pronted.dto.student.AddressModel


@BindingAdapter("app:has_schools", "app:has_children")
fun setAppVisibilityWarning(
    view: ConstraintLayout,
    hasSchools: Boolean = false,
    hasChildren: Boolean = false
) {
    view.visibility = if (hasSchools || hasChildren) View.GONE else View.VISIBLE
}

@BindingAdapter("app:image_uri", "app:default_image")
fun loadImage(view: ImageView, url: String?, defaultImage: Drawable) {
    Glide.with(view.context)
        .load(url)
        .placeholder(defaultImage)
        .error(defaultImage)
        .into(view)
}

@BindingAdapter("app:text_format", "app:text_value")
fun parseTextWithValue(view: AppCompatTextView, textFormat: String, value: String?) {
    var valueStr = view.context.getString(R.string.empty)
    if (!value.isNullOrEmpty())
        valueStr = value
    value.let {
        view.text = Html.fromHtml("$textFormat <b>$valueStr</b>", Html.FROM_HTML_MODE_LEGACY)
    }
}


@BindingAdapter("app:address")
fun parseAddress(view: AppCompatTextView, address: AddressModel?) {
    val text = buildAddressString(address, ",")
    if (text.isEmpty())
        view.text = view.context.getString(R.string.empty)
    else
        view.text = text
}

fun buildAddressString(address: AddressModel?, separator: String): String {
    var addressString = ""
    address?.let {
        if (!address.addressLine1.isNullOrEmpty())
            addressString += "${address.addressLine1}${separator}"

        if (!address.addressLine2.isNullOrEmpty())
            addressString += "${address.addressLine2}${separator}"

        if (!address.city.isNullOrEmpty())
            addressString += "${address.city}${separator}"

        if (!address.district.isNullOrEmpty())
            addressString += "${address.district}${separator}"

        if (!address.state.isNullOrEmpty())
            addressString += "${address.state}${separator}"

        if (!address.country.isNullOrEmpty())
            addressString += "${address.country}${separator}"

        if (!address.postalcode.isNullOrEmpty())
            addressString += "${address.postalcode}${separator}"
    }
    return addressString
}
