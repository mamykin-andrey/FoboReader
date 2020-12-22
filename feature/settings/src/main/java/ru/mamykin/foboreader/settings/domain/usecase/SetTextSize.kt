package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage

class SetTextSize(
    private val appSettings: AppSettingsStorage
) {
    operator fun invoke(action: Action) {
        val newSize = when (action) {
            is Action.Increase -> appSettings.readTextSizeField.get() + 1
            is Action.Decrease -> appSettings.readTextSizeField.get() - 1
        }
        appSettings.readTextSizeField.set(newSize)
    }

    sealed class Action {
        object Increase : Action()
        object Decrease : Action()
    }
}