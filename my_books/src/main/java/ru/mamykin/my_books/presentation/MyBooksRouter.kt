package ru.mamykin.my_books.presentation

import android.app.Activity
import android.content.Intent
import ru.mamykin.core.data.model.FictionBook
import ru.mamykin.my_books.R

class MyBooksRouter(
        private val activity: Activity
) {
    fun openBook(bookPath: String) {
        // ReadBookActivity.start(activity, bookPath)
    }

    fun openBookDetails(bookPath: String) {
        // BookDetailsActivity.start(activity, bookPath)
    }

    fun openBookShareDialog(book: FictionBook) {
        val shareText = activity.getString(R.string.download_on, book.bookTitle, book.docUrl)

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        activity.startActivity(shareIntent)
    }
}