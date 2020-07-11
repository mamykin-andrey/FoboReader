package ru.mamykin.foboreader.core.platform

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

interface Navigator {

    fun setActivity(activity: AppCompatActivity)

    fun clearActivity()

    fun openMyBooksScreen()

    fun openTabsScreen()

    fun openBook(id: Long)

    fun openBookDetails(id: Long, sharedImage: ImageView)
}