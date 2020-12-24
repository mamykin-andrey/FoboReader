package ru.mamykin.foboreader.my_books.domain.usecase

import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.core.domain.SuspendUseCase
import java.io.File

class RemoveBook(
    private val repository: BookInfoRepository
) : SuspendUseCase<Long, Unit>() {

    override suspend fun execute(param: Long) {
        val bookInfo = repository.getBookInfo(param)
        repository.removeBook(bookInfo.id)
            .also { File(bookInfo.filePath).delete() }
    }
}