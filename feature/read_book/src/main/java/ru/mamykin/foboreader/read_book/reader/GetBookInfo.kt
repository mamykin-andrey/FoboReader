package ru.mamykin.foboreader.read_book.reader

import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import javax.inject.Inject

class GetBookInfo @Inject constructor(
    private val bookInfoRepository: BookInfoRepository
) {
    suspend fun execute(bookId: Long): BookInfo {
        return bookInfoRepository.getBookInfo(bookId)
    }
}