package ru.mamykin.core.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.io.Serializable

fun Activity.showSnackbar(@StringRes messageRes: Int, long: Boolean = false) {
    showSnackbar(getString(messageRes), long)
}

fun Activity.showSnackbar(message: String, long: Boolean = false) {
    val duration = if (long) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
    findViewById<View>(android.R.id.content)
            ?.let { Snackbar.make(it, message, duration) }
}

fun Fragment.showSnackbar(@StringRes messageRes: Int, long: Boolean = false) {
    showSnackbar(getString(messageRes), long)
}

fun Fragment.showSnackbar(message: String, long: Boolean = false) {
    val duration = if (long) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
    activity?.findViewById<View>(android.R.id.content)
            ?.let { Snackbar.make(it, message, duration) }
}


inline fun <reified T : Activity> Context.startActivity(vararg params: Pair<String, Any>) {
    val intent = Intent(this, T::class.java).apply {
        putExtras(params.toList().toBundle())
    }
    startActivity(intent)
}

fun List<Pair<String, Any>>.toBundle(): Bundle = Bundle().apply {
    forEach { (k, v) ->
        when (v) {
            is Byte -> putByte(k, v)
            is Char -> putChar(k, v)
            is CharSequence -> putCharSequence(k, v)
            is Float -> putFloat(k, v)
            is Parcelable -> putParcelable(k, v)
            is Serializable -> putSerializable(k, v)
            is Short -> putShort(k, v)
            else -> throw IllegalArgumentException("$v is of a type that is not currently supported")
        }
    }
}