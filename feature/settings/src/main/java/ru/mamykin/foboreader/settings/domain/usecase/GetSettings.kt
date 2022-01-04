package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.settings.domain.model.SettingsItem
import javax.inject.Inject

class GetSettings @Inject constructor(
    private val settingsStorage: AppSettingsStorage,
) {
    fun execute(): List<SettingsItem> {
        return with(settingsStorage) {
            listOf(
                SettingsItem.NightTheme(nightThemeField.get()),
                SettingsItem.Brightness(brightnessField.get()),
                SettingsItem.ReadTextSize(readTextSizeField.get()),
                SettingsItem.TranslationColor(translationColorCodeField.get()),
                SettingsItem.AppLanguage(appLanguageField.get()),
                SettingsItem.UseVibration(useVibrationField.get()),
            )
        }
    }
}