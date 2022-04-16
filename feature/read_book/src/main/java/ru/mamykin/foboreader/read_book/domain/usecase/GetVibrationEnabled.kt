package ru.mamykin.foboreader.read_book.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsRepository
import javax.inject.Inject

internal class GetVibrationEnabled @Inject constructor(
    private val settingsRepository: AppSettingsRepository
) {
    fun execute(): Boolean {
        return settingsRepository.useVibration
    }
}