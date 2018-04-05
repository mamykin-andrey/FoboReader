package ru.mamykin.foboreader.ui.bookdetails

import android.support.v7.app.AppCompatActivity
import ru.mamykin.foboreader.ui.readbook.ReadBookActivity
import javax.inject.Inject

class BookDetailsRouter @Inject constructor(
        private val activity: AppCompatActivity
) {
    fun openBook(bookPath: String) {
        activity.startActivity(ReadBookActivity.getStartIntent(activity, bookPath))
    }
}