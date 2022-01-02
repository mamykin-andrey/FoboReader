package ru.mamykin.foboreader.store.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.mamykin.foboreader.store.presentation.BooksListFragment

class BooksListScreen(
    private val categoryId: String,
) : FragmentScreen {

    override fun createFragment(factory: FragmentFactory): Fragment = BooksListFragment.newInstance(categoryId)
}