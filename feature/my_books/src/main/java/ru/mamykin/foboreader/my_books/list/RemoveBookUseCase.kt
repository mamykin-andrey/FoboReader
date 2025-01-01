package ru.mamykin.foboreader.my_books.list

import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import java.io.File
import javax.inject.Inject

class RemoveBookUseCase @Inject constructor(
    private val repository: BookInfoRepository
) {
    suspend fun execute(bookId: Long): Result<Unit> {
        return runCatching {
            val bookInfo = repository.getBookInfo(bookId)
            repository.removeBook(bookInfo.id)
                .also { File(bookInfo.filePath).delete() }
        }
    }
}