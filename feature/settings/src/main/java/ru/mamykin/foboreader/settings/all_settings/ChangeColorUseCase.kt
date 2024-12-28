package ru.mamykin.foboreader.settings.all_settings

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.settings.common.CustomColorType
import javax.inject.Inject

internal class ChangeColorUseCase @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository
) {
    fun execute(colorType: CustomColorType, colorCode: String) {
        when (colorType) {
            CustomColorType.TRANSLATION -> appSettingsRepository.setTranslationColor(colorCode)
            CustomColorType.BACKGROUND -> appSettingsRepository.setBackgroundColor(colorCode)
        }
    }
}