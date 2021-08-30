package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.UseCase
import ru.mamykin.foboreader.settings.domain.model.supportedAppLanguages
import javax.inject.Inject

class GetAppLanguages  @Inject constructor() : UseCase<Unit, List<Pair<String, String>>>() {

    override fun execute(param: Unit): List<Pair<String, String>> {
        return supportedAppLanguages
    }
}