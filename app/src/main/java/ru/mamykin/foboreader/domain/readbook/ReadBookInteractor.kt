package ru.mamykin.foboreader.domain.readbook

import ru.mamykin.foboreader.core.di.qualifiers.BookPath
import ru.mamykin.foboreader.data.repository.books.BooksRepository
import ru.mamykin.foboreader.data.repository.translate.TranslateRepository
import ru.mamykin.foboreader.domain.entity.FictionBook
import javax.inject.Inject

class ReadBookInteractor @Inject constructor(
        private val booksRepository: BooksRepository,
        private val translateRepository: TranslateRepository,
        private val textToSpeechService: TextToSpeechService,
        @BookPath private val bookPath: String
) {
    private lateinit var book: FictionBook

    suspend fun getBookInfo(): FictionBook {
        return booksRepository.getBook(bookPath)
                .also { this.book = it }
    }

    suspend fun getParagraphTranslation(paragraph: String): String {
        return book.transMap[paragraph]
                ?: translateRepository.getTextTranslation(paragraph)
    }

    suspend fun getWordTranslation(word: String): String {
        return translateRepository.getTextTranslation(word)
    }

    fun voiceWord(word: String) {
        return textToSpeechService.voiceWord(word)
    }
}