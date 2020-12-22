package ru.mamykin.foboreader.read_book.domain.usecase

import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository

class UpdateBookInfo(
    private val bookInfoRepository: BookInfoRepository
) {
    suspend fun execute(bookId: Long, currentPage: Int, totalPages: Int) {
        bookInfoRepository.updateBookInfo(bookId, currentPage, totalPages)
    }
}