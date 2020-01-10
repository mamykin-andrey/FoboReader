package ru.mamykin.store.presentation

import android.widget.Toast
import androidx.fragment.app.Fragment

class BooksStoreRouterImpl(
        private val fragment: Fragment
) : BooksStoreRouter {

    override fun openMyBooksScreen() {
        Toast.makeText(fragment.context, "Open my books screen", Toast.LENGTH_SHORT).show()
    }
}