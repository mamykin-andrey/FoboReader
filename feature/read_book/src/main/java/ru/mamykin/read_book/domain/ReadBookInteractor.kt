package ru.mamykin.read_book.domain

import ru.mamykin.read_book.data.model.FictionBook
import ru.mamykin.read_book.data.ReadBookRepository
import ru.mamykin.read_book.data.TranslateRepository

class ReadBookInteractor constructor(
        private val readBookRepository: ReadBookRepository,
        private val translateRepository: TranslateRepository,
        private val textToSpeechService: TextToSpeechService
) {
    private lateinit var book: FictionBook

    suspend fun getBookInfo(): FictionBook {
        val bookPath = ""
        return readBookRepository.getBook(bookPath)
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