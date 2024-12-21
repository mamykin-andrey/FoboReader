package ru.mamykin.foboreader.my_books.list

import android.content.Context
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.extension.isBookSupported
import javax.inject.Inject

internal class BookFilesScanner @Inject constructor(
    private val context: Context,
    private val myBooksRepository: MyBooksRepository,
    private val bookInfoParser: BookInfoParser,
) {
    suspend fun scan(): List<BookInfo> {
        val dir = context.externalMediaDirs.first() ?: throw IllegalStateException("Can't open media directory!")

        val addedFiles = myBooksRepository.getBooks().map { it.filePath }

        return dir.listFiles()
            ?.filter { it.isBookSupported }
            ?.map { it.absolutePath }
            ?.filterNot { addedFiles.contains(it) }
            ?.mapNotNull { bookInfoParser.parse(it) }
            ?: emptyList()
    }
}