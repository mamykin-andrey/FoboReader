package ru.mamykin.foboreader.settings.all_settings

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.settings.common.CustomColorType
import javax.inject.Inject

internal class GetCurrentColorUseCase @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository
) {
    fun execute(colorType: CustomColorType): String = when (colorType) {
        CustomColorType.TRANSLATION -> appSettingsRepository.getTranslationColor()
        CustomColorType.BACKGROUND -> appSettingsRepository.getBackgroundColor()
    }
}