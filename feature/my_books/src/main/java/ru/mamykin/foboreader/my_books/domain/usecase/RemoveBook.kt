package ru.mamykin.foboreader.my_books.domain.usecase

import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.core.domain.usecase.base.Result
import java.io.File
import javax.inject.Inject

class RemoveBook @Inject constructor(
    private val repository: BookInfoRepository
) {
    suspend fun execute(param: Long): Result<Unit> {
        return runCatching {
            Result.success(run {
                val bookInfo = repository.getBookInfo(param)
                repository.removeBook(bookInfo.id)
                    .also { File(bookInfo.filePath).delete() }
            })
        }.getOrElse { Result.error(it) }
    }
}