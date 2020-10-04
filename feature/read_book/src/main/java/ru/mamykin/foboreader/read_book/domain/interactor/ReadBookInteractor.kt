package ru.mamykin.foboreader.read_book.domain.interactor

import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.read_book.data.TranslateRepository
import ru.mamykin.foboreader.read_book.domain.helper.BookTextParser
import ru.mamykin.foboreader.read_book.domain.helper.TextToSpeechService
import ru.mamykin.foboreader.read_book.domain.model.BookContent

class ReadBookInteractor(
    private val bookInfoRepository: BookInfoRepository,
    private val translateRepository: TranslateRepository,
    private val textToSpeechService: TextToSpeechService,
    private val bookTextParser: BookTextParser
) {
    private lateinit var bookInfo: BookInfo
    private lateinit var bookContent: BookContent

    suspend fun getBookInfo(id: Long): BookInfo {
        return bookInfoRepository.getBookInfo(id)!!
            .also { bookInfo = it }
    }

    suspend fun getBookContent(filePath: String): BookContent {
        return bookTextParser.parse(filePath)
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

    suspend fun saveCurrentPage(bookId: Long, currentPage: Int) {
        bookInfoRepository.updateCurrentPage(bookId, currentPage)
    }
}