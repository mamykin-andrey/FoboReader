package ru.mamykin.foboreader.app.presentation

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.core.extension.setCurrentLocale

internal class AppLanguageDelegate(
    private val activity: AppCompatActivity,
    private val appSettingsRepository: AppSettingsRepository,
) {
    fun init() {
        appSettingsRepository.appLanguageFlow()
            .onEach { activity.setCurrentLocale(it) }
            .launchIn(activity.lifecycleScope)
    }
}