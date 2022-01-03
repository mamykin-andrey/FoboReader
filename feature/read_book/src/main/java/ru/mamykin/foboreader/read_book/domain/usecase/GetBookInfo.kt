package ru.mamykin.foboreader.read_book.domain.usecase

import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.domain.usecase.base.Result
import javax.inject.Inject

class GetBookInfo @Inject constructor(
    private val bookInfoRepository: BookInfoRepository
) {
    suspend fun execute(param: Long): Result<BookInfo> {
        return runCatching {
            Result.success(bookInfoRepository.getBookInfo(param))
        }.getOrElse { Result.error(it) }
    }
}