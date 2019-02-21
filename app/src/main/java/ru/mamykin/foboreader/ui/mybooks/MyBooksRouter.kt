package ru.mamykin.foboreader.ui.mybooks

import android.app.Activity
import android.content.Intent
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.entity.FictionBook
import ru.mamykin.foboreader.ui.bookdetails.BookDetailsActivity
import ru.mamykin.foboreader.ui.readbook.ReadBookActivity

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