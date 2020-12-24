package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.domain.UseCase

class SetTextSize(
    private val appSettings: AppSettingsStorage
) : UseCase<SetTextSize.Action, Unit>() {

    override fun execute(param: Action) {
        val newSize = when (param) {
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