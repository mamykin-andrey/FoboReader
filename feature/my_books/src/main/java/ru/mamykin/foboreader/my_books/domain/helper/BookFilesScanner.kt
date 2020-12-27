package ru.mamykin.foboreader.my_books.domain.helper

import android.content.Context
import ru.mamykin.foboreader.common_book_info.data.database.BookInfoDao
import ru.mamykin.foboreader.common_book_info.data.model.toDatabaseModel
import ru.mamykin.foboreader.core.extension.getExternalMediaDir
import ru.mamykin.foboreader.core.extension.isFictionBook
import ru.mamykin.foboreader.core.platform.Log

class BookFilesScanner(
    private val context: Context,
    private val bookInfoDao: BookInfoDao,
    private val bookInfoParser: BookInfoParser
) {
    suspend fun scan() {
        val dir = context.getExternalMediaDir() ?: run {
            Log.error("Can't open media directory!")
            return
        }

        val addedFiles = bookInfoDao.getBooks().map { it.filePath }

        dir.listFiles()
            ?.filter { it.isFictionBook }
            ?.map { it.absolutePath }
            ?.filterNot { addedFiles.contains(it) }
            ?.map { bookInfoParser.parse(it).toDatabaseModel() }
            ?.let { bookInfoDao.insertAll(it) }
    }
}