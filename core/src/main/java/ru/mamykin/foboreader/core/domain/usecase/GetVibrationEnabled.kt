package ru.mamykin.foboreader.core.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.domain.usecase.base.UseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetVibrationEnabled @Inject constructor(
    private val settingsStorage: AppSettingsStorage
) : UseCase<Unit, Boolean>() {

    override fun execute(param: Unit): Boolean {
        return settingsStorage.useVibrationField.get()
    }
}