package ru.mamykin.foboreader.read_book.reader

import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import javax.inject.Inject

class UpdateBookInfo @Inject constructor(
    private val bookInfoRepository: BookInfoRepository
) {
    suspend fun execute(
        bookId: Long,
        currentPage: Int,
        totalPages: Int,
    ) {
        bookInfoRepository.updateBookInfo(
            bookId,
            currentPage,
            totalPages
        )
    }
}