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
        fileRepository.downloadFile(book.link, file)
        downloadedBooksRepository.saveBook(
            file.absolutePath,
            book.genre,
            book.coverUrl,
            book.author,
            book.title,
            book.languages,
        )
    }

    private fun getStorageFileName(book: StoreBookEntity): String {
        val transliteratedName = StringTransliterator.transliterate(book.title)
        return "$transliteratedName.${book.format}"
    }
}