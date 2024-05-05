package ru.mamykin.foboreader.settings.all_settings

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import javax.inject.Inject

internal class SetTranslationColor @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository
) {
    fun execute(color: String) {
        appSettingsRepository.setTranslationColor(color)
    }
}