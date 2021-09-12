package ru.mamykin.foboreader.core.extension

import android.app.Activity
import ru.mamykin.foboreader.core.di.api.ApiHolder
import ru.mamykin.foboreader.core.di.api.ApiHolderProvider
import java.util.*

fun Activity.apiHolder(): ApiHolder {
    return (application as ApiHolderProvider).apiHolder
}

fun Activity.setCurrentLocale(languageCode: String) {
    if (resources.configuration.locale.language == languageCode) return
    resources.apply {
        configuration.setLocale(Locale(languageCode))
        updateConfiguration(configuration, displayMetrics)
    }
    recreate()
}