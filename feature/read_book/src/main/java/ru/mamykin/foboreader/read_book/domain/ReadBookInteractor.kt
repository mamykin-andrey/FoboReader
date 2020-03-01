package ru.mamykin.foboreader.read_book.domain

import ru.mamykin.foboreader.core.data.BookInfoRepository
import ru.mamykin.foboreader.core.domain.model.BookInfo
import ru.mamykin.foboreader.read_book.data.TranslateRepository

class ReadBookInteractor constructor(
        private val bookInfoRepository: BookInfoRepository,
        private val translateRepository: TranslateRepository,
        private val textToSpeechService: TextToSpeechService,
        private val bookTextParser: BookTextParser
) {
    private lateinit var bookInfo: BookInfo
    private lateinit var bookText: Pair<List<String>, List<String>>

    suspend fun getBookInfo(id: Long): BookInfo = bookInfoRepository.getBook(id)
            ?.also { this.bookInfo = it }!!

    suspend fun getBookText(): Pair<String, Int> = bookTextParser.parse(bookInfo.filePath)
            .also { bookText = it }
            .first
            .joinToString() to 1

    suspend fun getParagraphTranslation(paragraph: String): String? = runCatching {
        // TODO
        translateRepository.getTextTranslation(paragraph)
    }.getOrNull()

    suspend fun getWordTranslation(word: String): String? = runCatching {
        translateRepository.getTextTranslation(word)
    }.getOrNull()

    fun voiceWord(word: String) {
        textToSpeechService.voiceWord(word)
    }
}