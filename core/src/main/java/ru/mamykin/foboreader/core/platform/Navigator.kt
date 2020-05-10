package ru.mamykin.foboreader.core.platform

import android.widget.ImageView

interface Navigator {

    fun openMyBooksScreen()

    fun openTabsScreen()

    fun openBook(id: Long)

    fun openBookDetails(id: Long, sharedImage: ImageView)
}