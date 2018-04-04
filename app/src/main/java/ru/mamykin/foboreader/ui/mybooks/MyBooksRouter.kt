package ru.mamykin.foboreader.ui.mybooks

import android.app.Activity
import android.content.Intent
import ru.mamykin.foboreader.R
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

    fun showBookShareDialog(bookName: String, url: String) {
        val shareText = activity.getString(R.string.download_on, bookName, url)

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        shareIntent.type = "text/plain"

        activity.startActivity(shareIntent)
    }
}