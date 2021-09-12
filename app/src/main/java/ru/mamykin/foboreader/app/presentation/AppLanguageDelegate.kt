package ru.mamykin.foboreader.app.presentation

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.extension.setCurrentLocale

class AppLanguageDelegate(
    private val activity: AppCompatActivity,
    private val appSettingsStorage: AppSettingsStorage
) {
    fun init() {
        appSettingsStorage.appLanguageField.flow
            .onEach { activity.setCurrentLocale(it) }
            .launchIn(activity.lifecycleScope)
    }
}