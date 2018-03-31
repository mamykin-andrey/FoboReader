package ru.mamykin.foboreader.ui.bookdetails

import android.support.v7.app.AppCompatActivity
import ru.mamykin.foboreader.ui.readbook.ReadBookActivity
import javax.inject.Inject

class BookDetailsRouter @Inject constructor(
        private val activity: AppCompatActivity
) {
    fun openBook(bookId: Int) {
        activity.startActivity(ReadBookActivity.getStartIntent(activity, bookId))
    }
}