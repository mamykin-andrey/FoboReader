package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsRepository
import javax.inject.Inject

internal class SetTextSize @Inject constructor(
    private val appSettings: AppSettingsRepository
) {
    fun execute(action: Action) {
        val newSize = when (action) {
            is Action.Increase -> appSettings.readTextSize + 1
            is Action.Decrease -> appSettings.readTextSize - 1
        }
        appSettings.readTextSize = newSize
    }

    sealed class Action {
        object Increase : Action()
        object Decrease : Action()
    }
}