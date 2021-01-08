package ru.mamykin.foboreader.settings.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.domain.usecase.base.FlowUseCase
import ru.mamykin.foboreader.settings.domain.model.SettingsItem

class GetSettings(
    private val settingsStorage: AppSettingsStorage
) : FlowUseCase<Unit, List<SettingsItem>>() {

    override fun execute(): Flow<List<SettingsItem>> {
        return with(settingsStorage) {
            combine(
                nightThemeField.flow.map { SettingsItem.NightTheme(it) },
                brightnessField.flow.map { SettingsItem.Brightness(it) },
                readTextSizeField.flow.map { SettingsItem.ReadTextSize(it) },
                translationColorCodeField.flow.map { SettingsItem.TranslationColor(it) },
                appLanguageField.flow.map { SettingsItem.AppLanguage(it) },
                useVibrationField.flow.map { SettingsItem.UseVibration(it) }
            ) { it.toList() }
        }
    }
}