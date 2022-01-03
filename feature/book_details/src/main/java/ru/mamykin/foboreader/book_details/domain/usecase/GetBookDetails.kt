package ru.mamykin.foboreader.book_details.domain.usecase

import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.domain.usecase.base.Result
import javax.inject.Inject

class GetBookDetails @Inject constructor(
    private val repository: BookInfoRepository
) {
    suspend fun execute(param: Long): Result<BookInfo> {
        return runCatching {
            Result.success(repository.getBookInfo(param))
        }.getOrElse { Result.error(it) }
    }
}