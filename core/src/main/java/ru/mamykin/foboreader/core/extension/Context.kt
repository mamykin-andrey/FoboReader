package ru.mamykin.foboreader.core.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.google.android.material.snackbar.Snackbar
import java.io.File
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

fun Context.showToast(messageResId: Int, long: Boolean = false) =
    showToast(getString(messageResId), long)

fun Context.showToast(message: String, long: Boolean = false) {
    val length = if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    Toast.makeText(this, message, length).show()
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

fun Context.getExternalMediaDir(): File? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        externalMediaDirs.first()
    } else {
        getExternalFilesDir(null)
    }

val Fragment.appCompatActivity: AppCompatActivity
    get() = activity as? AppCompatActivity
        ?: throw IllegalStateException("Activity isn't an instance of AppCompatActivity!")

var AppCompatActivity?.nightMode: Boolean
    get() = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    set(value) {
        this ?: return
        if (value != nightMode) {
            val newMode =
                if (value) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(newMode)
            this.takeIf { lifecycle.currentState > Lifecycle.State.CREATED }?.recreate()
        }
    }

fun Context.dpToPx(value: Int): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value.toFloat(),
        resources.displayMetrics
    )
}