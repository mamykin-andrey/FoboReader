package ru.mamykin.foboreader.read_book.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.Result
import ru.mamykin.foboreader.read_book.data.TranslationRepository
import ru.mamykin.foboreader.read_book.domain.entity.TranslationEntity
import javax.inject.Inject

class GetWordTranslation @Inject constructor(
    private val translationRepository: TranslationRepository
) {
    suspend fun execute(text: String): Result<TranslationEntity> {
        return runCatching {
            Result.success(translationRepository.getTranslation(text))
        }.getOrElse { Result.error(it) }
    }
}