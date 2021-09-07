package ru.mamykin.foboreader.core.extension

import android.content.Context
import android.os.Build
import android.util.TypedValue
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import java.io.File

fun Fragment.showSnackbar(@StringRes messageRes: Int, long: Boolean = false) {
    showSnackbar(getString(messageRes), long)
}

fun Fragment.showSnackbar(message: String, long: Boolean = false) {
    val duration = if (long) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
    activity?.findViewById<View>(android.R.id.content)
        ?.let { Snackbar.make(it, message, duration) }
}

fun Context.getExternalMediaDir(): File? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        externalMediaDirs.first()
    } else {
        getExternalFilesDir(null)
    }

//var AppCompatActivity?.nightMode: Boolean
//    get() = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
//    set(value) {
//        if (this != null && value != nightMode) {
//            val newMode = if (value)
//                AppCompatDelegate.MODE_NIGHT_YES
//            else
//                AppCompatDelegate.MODE_NIGHT_NO
//            AppCompatDelegate.setDefaultNightMode(newMode)
//        }
//    }

//fun Activity.setCurrentLocale(languageCode: String) {
//    if (resources.configuration.locale.language == languageCode) return
//    resources.apply {
//        configuration.setLocale(Locale(languageCode))
//        updateConfiguration(configuration, displayMetrics)
//    }
//    recreate()
//}

fun Context.dpToPx(value: Int): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value.toFloat(),
        resources.displayMetrics
    )
}

//@Suppress("deprecation")
//fun Context.getCurrentLocaleName(): String {
//    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//        resources.configuration.locales[0].displayLanguage
//    } else {
//        resources.configuration.locale.displayLanguage
//    }
//}