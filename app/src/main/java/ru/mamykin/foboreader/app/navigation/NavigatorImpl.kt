package ru.mamykin.foboreader.app.navigation

import androidx.navigation.NavController
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.platform.Navigator
import ru.mamykin.foboreader.my_books.presentation.MyBooksNavigator
import ru.mamykin.foboreader.read_book.presentation.ReadBookFragment
import ru.mamykin.foboreader.store.presentation.BooksStoreNavigator

class NavigatorImpl : Navigator, BooksStoreNavigator,
    ru.mamykin.foboreader.book_details.presentation.BookDetailsNavigator, MyBooksNavigator {

    private var navController: NavController? = null

    override fun bind(controller: NavController) {
        this.navController = controller
    }

    override fun unbind() {
        this.navController = null
    }

    override fun openMyBooksScreen() {
//        navController.mActivity?.supportFragmentManager
//            ?.findFragmentById(R.id.fr_main_nav_host)
//            ?.childFragmentManager
//            ?.fragments
//            ?.firstOrNull()
//            ?.let { it as? TabsFragment }
//            ?.openMyBooksTab()
    }

    override fun openBook(bookId: Long) {
        navController?.navigate(R.id.tabs_to_read_book, ReadBookFragment.bundle(bookId))
    }

    override fun openBookDetails(bookId: Long) {
        navController?.navigate(R.id.tabs_to_book_details, ru.mamykin.foboreader.book_details.presentation.BookDetailsFragment.bundle(bookId))
    }
}