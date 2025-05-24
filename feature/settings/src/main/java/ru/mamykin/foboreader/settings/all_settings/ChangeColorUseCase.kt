package ru.mamykin.foboreader.settings.all_settings

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.core.navigation.AppScreen
import javax.inject.Inject

internal class ChangeColorUseCase @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository
) {
    fun execute(colorType: AppScreen.ChooseColor.CustomColorType, colorCode: String) {
        when (colorType) {
            AppScreen.ChooseColor.CustomColorType.TRANSLATION -> appSettingsRepository.setTranslationColor(colorCode)
            AppScreen.ChooseColor.CustomColorType.BACKGROUND -> appSettingsRepository.setBackgroundColor(colorCode)
            AppScreen.ChooseColor.CustomColorType.TEXT -> appSettingsRepository.setTextColor(colorCode)
        }
    }
}