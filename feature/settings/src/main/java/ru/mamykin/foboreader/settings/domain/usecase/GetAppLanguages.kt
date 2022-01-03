package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.Result
import ru.mamykin.foboreader.settings.domain.model.supportedAppLanguages
import javax.inject.Inject

class GetAppLanguages @Inject constructor() {

    fun execute(): Result<List<Pair<String, String>>> {
        return runCatching {
            Result.success(supportedAppLanguages)
        }.getOrElse { Result.error(it) }
    }
}