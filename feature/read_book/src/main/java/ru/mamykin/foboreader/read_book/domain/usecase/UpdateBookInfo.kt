package ru.mamykin.foboreader.read_book.domain.usecase

import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.core.domain.usecase.base.SuspendUseCase
import javax.inject.Inject

class UpdateBookInfo @Inject constructor(
    private val bookInfoRepository: BookInfoRepository
) : SuspendUseCase<UpdateBookInfo.Param, Unit>() {

    override suspend fun execute(param: Param) {
        bookInfoRepository.updateBookInfo(
            param.bookId,
            param.currentPage,
            param.totalPages
        )
    }

    data class Param(
        val bookId: Long,
        val currentPage: Int,
        val totalPages: Int
    )
}