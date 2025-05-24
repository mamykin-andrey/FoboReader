package ru.mamykin.foboreader.settings.all_settings

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.core.navigation.AppScreen
import javax.inject.Inject

internal class GetCurrentColorUseCase @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository
) {
    fun execute(colorType: AppScreen.ChooseColor.CustomColorType): String = when (colorType) {
        AppScreen.ChooseColor.CustomColorType.TRANSLATION -> appSettingsRepository.getTranslationColor()
        AppScreen.ChooseColor.CustomColorType.BACKGROUND -> appSettingsRepository.getBackgroundColor()
        AppScreen.ChooseColor.CustomColorType.TEXT -> appSettingsRepository.getTextColor()
    }
}