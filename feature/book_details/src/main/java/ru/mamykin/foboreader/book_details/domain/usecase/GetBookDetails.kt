package ru.mamykin.foboreader.book_details.domain.usecase

import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo

class GetBookDetails(
    private val repository: BookInfoRepository
) {
    suspend operator fun invoke(id: Long): BookInfo? {
        return repository.getBookInfo(id)
    }
}