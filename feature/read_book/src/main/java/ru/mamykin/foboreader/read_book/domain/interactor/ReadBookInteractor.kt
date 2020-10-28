package ru.mamykin.foboreader.read_book.domain.interactor

import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.data.storage.SettingsStorage
import ru.mamykin.foboreader.read_book.data.TranslateRepository
import ru.mamykin.foboreader.read_book.domain.helper.BookContentParser
import ru.mamykin.foboreader.read_book.domain.helper.TextToSpeechService
import ru.mamykin.foboreader.read_book.domain.model.BookContent
import ru.mamykin.foboreader.read_book.domain.model.ReadPreferences

class ReadBookInteractor(
    private val bookInfoRepository: BookInfoRepository,
    private val translateRepository: TranslateRepository,
    private val textToSpeechService: TextToSpeechService,
    private val bookContentParser: BookContentParser,
    private val settingsStorage: SettingsStorage
) {
    private lateinit var bookInfo: BookInfo
    private lateinit var bookContent: BookContent

    suspend fun getBookInfo(id: Long): BookInfo {
        return bookInfoRepository.getBookInfo(id)!!
            .also { bookInfo = it }
    }

    suspend fun getBookContent(filePath: String): BookContent {
        return bookContentParser.parse(filePath)
            .also { bookContent = it }
    }

    suspend fun getParagraphTranslation(sentence: String): String? {
        return bookContent.getTranslation(sentence)
            ?: getOnlineTextTranslation(sentence)
    }

    suspend fun getWordTranslation(word: String): String? =
        getOnlineTextTranslation(word)

    private suspend fun getOnlineTextTranslation(text: String): String? =
        runCatching { translateRepository.getTextTranslation(text) }
            .getOrNull()

    fun voiceWord(word: String) {
        textToSpeechService.voiceWord(word)
    }

    suspend fun updateBookInfo(bookId: Long, currentPage: Int, totalPages: Int) {
        bookInfoRepository.updateBookInfo(bookId, currentPage, totalPages)
    }

    suspend fun getReadPreferences(): ReadPreferences {
        return ReadPreferences(settingsStorage.readTextSize)
    }
}