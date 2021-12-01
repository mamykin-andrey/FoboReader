package ru.mamykin.foboreader.read_book.navigation

import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.mamykin.foboreader.read_book.presentation.ReadBookFragment

class ReadBookScreen(
    private val bookId: Long
) : FragmentScreen {

    override fun createFragment(factory: FragmentFactory) = ReadBookFragment.newInstance(bookId)
}