package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import javax.inject.Inject

internal class SetTranslationColor @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository
) {
    fun execute(color: String) {
        appSettingsRepository.setTranslationColor(color)
    }
}