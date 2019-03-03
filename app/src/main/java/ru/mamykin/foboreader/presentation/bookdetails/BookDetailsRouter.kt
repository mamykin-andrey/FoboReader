package ru.mamykin.foboreader.presentation.bookdetails

import android.support.v7.app.AppCompatActivity
import ru.mamykin.foboreader.presentation.readbook.ReadBookActivity
import javax.inject.Inject

class BookDetailsRouter @Inject constructor(
        private val activity: AppCompatActivity
) {
    fun openBook(bookPath: String) {
        ReadBookActivity.start(activity, bookPath)
    }
}