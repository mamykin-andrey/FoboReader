package ru.mamykin.foboreader.my_books.list

import android.content.Context
import ru.mamykin.foboreader.common_book_info.data.database.BookInfoDao
import ru.mamykin.foboreader.common_book_info.data.model.toDatabaseModel
import ru.mamykin.foboreader.core.extension.getExternalMediaDir
import ru.mamykin.foboreader.core.extension.isFictionBook
import javax.inject.Inject

internal class BookFilesScanner @Inject constructor(
    private val context: Context,
    private val bookInfoDao: BookInfoDao,
    private val bookInfoParser: BookInfoParser,
) {
    suspend fun scan() {
        val dir = context.getExternalMediaDir() ?: throw IllegalStateException("Can't open media directory!")

        val addedFiles = bookInfoDao.getBooks().map { it.filePath }

        dir.listFiles()
            ?.filter { it.isFictionBook }
            ?.map { it.absolutePath }
            ?.filterNot { addedFiles.contains(it) }
            ?.mapNotNull { bookInfoParser.parse(it)?.toDatabaseModel() }
            ?.let { bookInfoDao.insertAll(it) }
    }
}