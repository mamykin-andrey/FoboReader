package ru.mamykin.foboreader.read_book.reader

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.core.data.AppSettingsRepository
import javax.inject.Inject

internal class GetBookUseCase @Inject constructor(
    private val bookContentRepository: BookContentRepository,
    private val bookInfoRepository: BookInfoRepository,
    private val appSettingsRepository: AppSettingsRepository,
) {
    suspend fun execute(
        bookId: Long,
        textSplitter: TextSplitter,
        screenSize: Pair<Int, Int>
    ): Book = withContext(Dispatchers.Default) {
        val fontSize = appSettingsRepository.getReadTextSize()
        val info = bookInfoRepository.getBookInfo(bookId)
        val pages = textSplitter.splitText(
            bookContentRepository.getBookContent(info.filePath).sentences.joinToString("\n"),
            screenSize,
            fontSize,
        )
        return@withContext Book(
            info = info,
            pages = pages,
            fontSize = fontSize,
        )
    }
}