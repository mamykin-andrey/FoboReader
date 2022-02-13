package ru.mamykin.foboreader.core.extension

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.io.File

fun Fragment.showSnackbar(@StringRes messageRes: Int, long: Boolean = false) {
    showSnackbar(getString(messageRes), long)
}

fun Fragment.showSnackbar(message: String, long: Boolean = false) {
    val duration = if (long) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
    val contentView = requireActivity().findViewById<View>(android.R.id.content)
    Snackbar.make(contentView, message, duration).show()
}

@Deprecated("Use externalMediaDirs.first() direcly")
fun Context.getExternalMediaDir(): File? = externalMediaDirs.first()

fun setNightModeEnabled(enabled: Boolean) {
    val oldEnabled = isNightModeEnabled()
    if (enabled != oldEnabled) {
        val newMode = if (enabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(newMode)
    }
}

private fun isNightModeEnabled(): Boolean {
    return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
}

fun Context.dpToPx(value: Int): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value.toFloat(),
        resources.displayMetrics
    )
}