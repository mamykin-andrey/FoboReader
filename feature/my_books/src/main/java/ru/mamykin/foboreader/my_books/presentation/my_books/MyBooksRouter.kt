package ru.mamykin.foboreader.my_books.presentation.my_books

import android.app.Activity

class MyBooksRouter(
        private val activity: Activity
) {
    fun openBook(id: Long) {
        // ReadBookActivity.start(activity, bookPath)
    }

    fun openBookDetails(id: Long) {
        // BookDetailsActivity.start(activity, bookPath)
    }
}