package ru.mamykin.foboreader.my_books.domain.usecase

import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import java.io.File
import javax.inject.Inject

class RemoveBook @Inject constructor(
    private val repository: BookInfoRepository
) {
    suspend fun execute(param: Long): Result<Unit> {
        return runCatching {
            val bookInfo = repository.getBookInfo(param)
            repository.removeBook(bookInfo.id)
                .also { File(bookInfo.filePath).delete() }
        }
    }
}