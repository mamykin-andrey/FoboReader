package ru.mamykin.foboreader.read_book.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.SuspendUseCase
import ru.mamykin.foboreader.read_book.data.TranslationRepository
import ru.mamykin.foboreader.read_book.domain.entity.TranslationEntity

class GetWordTranslation(
    private val translationRepository: TranslationRepository
) : SuspendUseCase<String, TranslationEntity>() {

    override suspend fun execute(param: String): TranslationEntity {
        return translationRepository.getTranslation(param)
    }
}