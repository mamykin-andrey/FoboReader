package ru.mamykin.foboreader.book_details.domain.usecase

import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.domain.usecase.base.SuspendUseCase
import javax.inject.Inject

class GetBookDetails @Inject constructor(
    private val repository: BookInfoRepository
) : SuspendUseCase<Long, BookInfo>() {

    override suspend fun execute(param: Long): BookInfo {
        return repository.getBookInfo(param)
    }
}