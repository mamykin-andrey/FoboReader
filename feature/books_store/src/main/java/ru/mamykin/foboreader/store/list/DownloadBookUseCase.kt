package ru.mamykin.foboreader.store.list

import ru.mamykin.foboreader.common_book_info.data.repository.DownloadedBooksRepository
import javax.inject.Inject

internal class DownloadBookUseCase @Inject constructor(
    private val fileRepository: FileRepository,
    private val downloadedBooksRepository: DownloadedBooksRepository,
) {
    suspend fun execute(book: StoreBookEntity): Result<Unit> = runCatching {
        val outputFileName = getStorageFileName(book)
        val file = fileRepository.createFile(outputFileName).getOrThrow()
        fileRepository.downloadFile(book.link, file).getOrElse {
            file.delete()
            return Result.failure(it)
        }
        downloadedBooksRepository.saveBook(
            filePath = file.absolutePath,
            genre = book.genre,
            coverUrl = book.coverUrl,
            author = book.author,
            title = book.title,
            languages = book.languages,
            link = book.link,
            rating = book.rating,
            isRatedByUser = false,
        )
    }

    private fun getStorageFileName(book: StoreBookEntity): String {
        val fileName = book.link.substringAfterLast('/')
        if (fileName.isBlank()) {
            throw IllegalArgumentException("Invalid URL format: ${book.link}")
        }
        return fileName
    }
}