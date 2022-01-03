package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import javax.inject.Inject

class SetTextSize @Inject constructor(
    private val appSettings: AppSettingsStorage
) {
    fun execute(action: Action) {
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