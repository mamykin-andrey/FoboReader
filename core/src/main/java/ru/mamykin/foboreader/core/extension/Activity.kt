package ru.mamykin.foboreader.core.extension

import android.app.Activity
import java.util.Locale

fun Activity.changeLocale(languageCode: String) {
    if (resources.configuration.locale.language == languageCode) return
    resources.apply {
        configuration.setLocale(Locale(languageCode))
        updateConfiguration(configuration, displayMetrics)
    }
    recreate()
}