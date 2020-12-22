package ru.mamykin.foboreader.my_books.domain.usecase

import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import java.io.File

class RemoveBook(
    private val repository: BookInfoRepository
) {
    suspend operator fun invoke(id: Long) {
        val bookInfo = repository.getBookInfo(id)
        repository.removeBook(bookInfo.id)
            .also { File(bookInfo.filePath).delete() }
    }
}