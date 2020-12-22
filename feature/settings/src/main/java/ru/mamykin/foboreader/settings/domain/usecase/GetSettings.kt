package ru.mamykin.foboreader.settings.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.settings.domain.model.SettingsItem

@ExperimentalCoroutinesApi
class GetSettings(
    private val settingsStorage: AppSettingsStorage
) {
    operator fun invoke(): Flow<List<SettingsItem>> {
        return combine(
            settingsStorage.nightThemeField.flow,
            settingsStorage.brightnessField.flow.map { it!! },
            settingsStorage.readTextSizeField.flow,
            settingsStorage.translationColorCodeField.flow
        ) { nightTheme, brightness, textSize, translationColor ->
            listOf(
                SettingsItem.NightTheme(nightTheme),
                SettingsItem.Brightness(brightness),
                SettingsItem.ReadTextSize(textSize),
                SettingsItem.TranslationColor(translationColor),
            )
        }
    }
}