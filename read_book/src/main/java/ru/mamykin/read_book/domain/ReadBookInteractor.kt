package ru.mamykin.read_book.domain

import ru.mamykin.core.data.model.FictionBook
import ru.mamykin.read_book.data.BooksRepository
import ru.mamykin.read_book.data.TranslateRepository
import javax.inject.Inject

class ReadBookInteractor @Inject constructor(
        private val booksRepository: BooksRepository,
        private val translateRepository: TranslateRepository,
        private val textToSpeechService: TextToSpeechService
) {
    private lateinit var book: FictionBook

    suspend fun getBookInfo(): FictionBook {
        val bookPath = ""
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