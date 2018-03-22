package ru.mamykin.foboreader.data.repository

import ru.mamykin.foboreader.data.database.BookDatabaseHelper
import ru.mamykin.foboreader.data.model.FictionBook
import rx.Single
import javax.inject.Inject

class BookRepository @Inject constructor(private val bookDbHelper: BookDatabaseHelper) {

    fun getBookInfo(bookId: Int): Single<FictionBook> {
        return Single.just(bookDbHelper.getBookDao()!!.getBook(bookId))
    }
}