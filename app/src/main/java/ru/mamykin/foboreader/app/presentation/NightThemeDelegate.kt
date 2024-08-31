package ru.mamykin.foboreader.app.presentation

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.core.platform.Log

internal class NightThemeDelegate(
    private val activity: AppCompatActivity,
    private val appSettingsRepository: AppSettingsRepository,
) {
    fun init() {
        setNightModeEnabled(appSettingsRepository.isNightThemeEnabled())
        appSettingsRepository.nightThemeFlow()
            .onEach {
                Log.debug("Night mode changed to: $it")
                setNightModeEnabled(it)
            }
            .launchIn(activity.lifecycleScope)
    }

    private fun setNightModeEnabled(enabled: Boolean) {
        val newMode = if (enabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(newMode)
    }
}