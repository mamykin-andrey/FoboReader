package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.domain.UseCase
import ru.mamykin.foboreader.settings.domain.model.supportedAppLanguages

class GetAppLanguages : UseCase<Unit, List<Pair<String, String>>>() {

    override fun execute(param: Unit): List<Pair<String, String>> {
        return supportedAppLanguages
    }
}