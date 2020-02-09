package ru.mamykin.my_books.domain.my_books

import android.content.Context
import ru.mamykin.core.extension.getExternalMediaDir
import ru.mamykin.core.extension.isFictionBook
import ru.mamykin.my_books.data.database.BookInfoDao
import ru.mamykin.my_books.data.model.toDatabaseModel

class BookFilesScanner(
        private val context: Context,
        private val bookInfoDao: BookInfoDao,
        private val bookParser: BookParser
) {
    suspend fun scan() {
        val dir = context.getExternalMediaDir()
                ?: throw IllegalStateException("Can't open media directory!")

        val addedFiles = bookInfoDao.getBooks().map { it.filePath }

        dir.listFiles()
                ?.filter { it.isFictionBook }
                ?.map { it.absolutePath }
                ?.filterNot { addedFiles.contains(it) }
                ?.map { bookParser.parse(it).toDatabaseModel() }
                ?.let { bookInfoDao.insertAll(it) }
    }
}