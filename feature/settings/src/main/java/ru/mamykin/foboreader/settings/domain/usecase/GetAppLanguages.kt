package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.settings.domain.model.supportedAppLanguages
import javax.inject.Inject

internal class GetAppLanguages @Inject constructor() {

    fun execute(): List<Pair<String, String>> {
        return supportedAppLanguages
    }
}