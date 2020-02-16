package ru.mamykin.foboreader.my_books.domain.my_books

import android.content.Context
import ru.mamykin.foboreader.core.extension.getExternalMediaDir
import ru.mamykin.foboreader.core.extension.isFictionBook
import ru.mamykin.foboreader.core.platform.Log
import ru.mamykin.foboreader.my_books.data.database.BookInfoDao
import ru.mamykin.foboreader.my_books.data.model.toDatabaseModel

class BookFilesScanner(
        private val context: Context,
        private val bookInfoDao: BookInfoDao,
        private val bookParser: BookParser
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
                ?.map { bookParser.parse(it).toDatabaseModel() }
                ?.let { bookInfoDao.insertAll(it) }
    }
}