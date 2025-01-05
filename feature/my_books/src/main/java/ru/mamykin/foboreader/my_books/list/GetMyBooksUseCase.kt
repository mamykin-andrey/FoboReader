package ru.mamykin.foboreader.my_books.list

import ru.mamykin.foboreader.common_book_info.data.repository.DownloadedBooksRepository
import ru.mamykin.foboreader.common_book_info.domain.model.DownloadedBookEntity
import javax.inject.Inject

internal class GetMyBooksUseCase @Inject constructor(
    private val bookInfoRepository: DownloadedBooksRepository,
) {
    suspend fun execute(): List<DownloadedBookEntity> {
        return bookInfoRepository.getBooks()
    }
}