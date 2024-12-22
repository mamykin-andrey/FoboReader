package ru.mamykin.foboreader.app.presentation

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.mamykin.foboreader.core.data.AppSettingsRepository
import java.util.Locale

// TODO: Move to Settings
internal class AppLanguageDelegate(
    private val activity: AppCompatActivity,
    private val appSettingsRepository: AppSettingsRepository,
) {
    fun init() {
        appSettingsRepository.appLanguageFlow()
            .onEach { setCurrentLocale(it) }
            .launchIn(activity.lifecycleScope)
    }

    private fun setCurrentLocale(languageCode: String) {
        if (activity.resources.configuration.locale.language == languageCode) return
        activity.resources.apply {
            configuration.setLocale(Locale(languageCode))
            updateConfiguration(configuration, displayMetrics)
        }
        activity.recreate()
    }
}