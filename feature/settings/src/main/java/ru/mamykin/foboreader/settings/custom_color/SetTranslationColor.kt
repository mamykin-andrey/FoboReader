package ru.mamykin.foboreader.settings.custom_color

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import javax.inject.Inject

internal class SetTranslationColor @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository
) {
    fun execute(color: String) {
        appSettingsRepository.setTranslationColor(color)
    }
}