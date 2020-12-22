package ru.mamykin.foboreader.read_book.domain.usecase

import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo

class GetBookInfo(
    private val bookInfoRepository: BookInfoRepository
) {
    suspend fun execute(id: Long): BookInfo {
        return bookInfoRepository.getBookInfo(id)
    }
}