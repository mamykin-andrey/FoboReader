package ru.mamykin.foboreader.presentation.mybooks

import android.app.Activity
import android.content.Intent
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.domain.entity.FictionBook
import ru.mamykin.foboreader.presentation.bookdetails.BookDetailsActivity
import ru.mamykin.foboreader.presentation.readbook.ReadBookActivity

class MyBooksRouter(
        private val activity: Activity
) {

    fun openBook(bookPath: String) {
        activity.startActivity(ReadBookActivity.getStartIntent(activity, bookPath))
    }

    fun openBookDetails(bookPath: String) {
        activity.startActivity(BookDetailsActivity.getStartIntent(activity, bookPath))
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