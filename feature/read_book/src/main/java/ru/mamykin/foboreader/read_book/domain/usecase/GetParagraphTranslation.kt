package ru.mamykin.foboreader.read_book.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.UseCase
import ru.mamykin.foboreader.read_book.data.BookContentRepository
import javax.inject.Inject

class GetParagraphTranslation @Inject constructor(
    private val repository: BookContentRepository
) : UseCase<String, String?>() {

    override fun execute(param: String): String? {
        return repository.getParagraphTranslation(param)
    }
}