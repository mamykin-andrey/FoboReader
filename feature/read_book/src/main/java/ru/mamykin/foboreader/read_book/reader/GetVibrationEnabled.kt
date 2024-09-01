package ru.mamykin.foboreader.read_book.reader

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import javax.inject.Inject

internal class GetVibrationEnabled @Inject constructor(
    private val settingsRepository: AppSettingsRepository
) {
    fun execute(): Boolean {
        return settingsRepository.isUseVibration()
    }
}