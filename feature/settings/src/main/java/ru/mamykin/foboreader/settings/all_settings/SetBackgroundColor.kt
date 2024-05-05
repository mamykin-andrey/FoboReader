package ru.mamykin.foboreader.settings.all_settings

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import javax.inject.Inject

internal class SetBackgroundColor @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository
) {
    fun execute(color: String) {
        appSettingsRepository.setBackgroundColor(color)
    }
}