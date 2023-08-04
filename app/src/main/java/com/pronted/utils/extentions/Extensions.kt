package com.pronted.utils.extentions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.pronted.presentation.authentication.AuthenticationActivity
import com.base.Preference
import com.base.Trace
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.pronted.presentation.home.ActivityImageCrop
import io.paperdb.Paper
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.text.NumberFormat
import java.util.*

fun <T> Fragment.startNewActivity(
    context: Context, cls: Class<T>, bundle: Bundle? = null, clearTop: Boolean = false, finish:
    Boolean = false
) {
    val intent = Intent(context, cls)
    bundle?.let { intent.putExtras(it) }
    if (clearTop)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
    if (finish) {
        (context as Activity).finish()
    }
}

fun <T> Activity.startNewActivity(
    context: Context, cls: Class<T>, bundle: Bundle? = null, clearTop: Boolean = false, finish:
    Boolean = false
) {
    val intent = Intent(context, cls)
    bundle?.let { intent.putExtras(it) }
    if (clearTop)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
    if (finish) {
        (context as Activity).finish()
    }
}

fun logout(context: Context, finish: Boolean = false) {
    clearData()
    val intent = Intent(context, AuthenticationActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
    if (finish) {
        (context as Activity).finishAffinity()
    }
}

fun clearData() {
    // use co routines in future
    Preference.instance.clear()
    Paper.book().destroy()
}

fun cropImage(
    fragment: Fragment,
    bundle: Bundle? = null,
    finish: Boolean = false,
    requestForResult: Boolean = false,
    launcher: ActivityResultLauncher<Intent>
) {
    val intent = Intent(fragment.activity, ActivityImageCrop::class.java)
    bundle?.let { intent.putExtras(it) }
    if (requestForResult)
        launcher.launch(intent)
    else
        fragment.startActivity(intent)
    if (finish) {
        (fragment.activity)?.finish()
    }
}

/**
 * Creates rectangular bounds to the given latlong with specified radius
 */
/*fun LatLng?.toBounds(radiusInMeters: Double): RectangularBounds {
    this?.let {
        val center = this
        val distanceFromCenterToCorner = radiusInMeters * sqrt(2.0)

        val ne = SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0)
        val sw = SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0)
        return RectangularBounds.newInstance(sw, ne)
    }
    return RectangularBounds.newInstance(
        LatLng(6.4626999, 68.1097),
        LatLng(35.513327, 97.39535869999999)
    )
}*/

/**
 * String To model
 *
 * @param T
 * @return T
 * @see "{}".toModel<UserModel>()
 */
inline fun <reified T> String.toModel(): T? {
    return try {
        Gson().fromJson(this, object : TypeToken<T>() {}.type)
    } catch (jsonException: JSONException) {
        null
    }
}

/**
 * To json object
 *
 * @return
 */
fun String.toJsonObject(): JSONObject {
    return try {
        JSONObject(this)
    } catch (jsonException: JSONException) {
        JSONObject()
    }
}

fun String.isEmailAddress(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

/**
 * Verifies mobile number format and validates
 */
fun String.isMobileNumber(): Boolean {
    try {
        val mPhoneUtil = PhoneNumberUtil.getInstance()
        val mNumber = mPhoneUtil.parse(this, "+91")
        return mPhoneUtil.isValidNumber(mNumber)
    } catch (ignored: Exception) {
        Trace.i("Exception: " + ignored.message)
    }
    return false
}

fun String.getNationalMobileNumber(): String {
    try {
        val mPhoneUtil = PhoneNumberUtil.getInstance()
        val mNumber = mPhoneUtil.parse(this, "")
        return mNumber.nationalNumber.toString()
    } catch (ignored: Exception) {
    }
    return ""
}

fun String.capitalizeWords(delimiter: String = " "): String =
    split(delimiter).joinToString(" "){
        it.lowercase().replaceFirstChar { char -> char.titlecase() }
    }

fun Int.getFormattedAmount(): String {
    return try {
        NumberFormat.getNumberInstance(Locale("en", "in")).format(this.toDouble())
    } catch (ex: Exception) {
        "0"
    }
}

fun getFormatedNumber(number: Int?): String? {
    return if (!number.toString().isEmpty()) {
        val `val` = number.toString().toDouble()
        NumberFormat.getNumberInstance(Locale("en", "in")).format(`val`)
    } else {
        "0"
    }
}


fun File.imageContentType():String = "image/${path.split(".").last()}"
