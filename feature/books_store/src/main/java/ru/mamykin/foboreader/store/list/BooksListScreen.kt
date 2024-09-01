package ru.mamykin.foboreader.store.list

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen

class BooksListScreen(
    private val categoryId: String,
) : FragmentScreen {

    override fun createFragment(factory: FragmentFactory): Fragment = BooksListFragment.newInstance(categoryId)
}