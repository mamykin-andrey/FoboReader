package ru.mamykin.foboreader.app.navigation

import androidx.navigation.NavController
import ru.mamykin.foboreader.app.ui.TabsFragmentDirections
import ru.mamykin.foboreader.book_details.presentation.BookDetailsFragmentDirections
import ru.mamykin.foboreader.book_details.presentation.BookDetailsNavigator
import ru.mamykin.foboreader.core.platform.NavControllerHolder
import ru.mamykin.foboreader.my_books.presentation.MyBooksNavigator
import ru.mamykin.foboreader.store.presentation.BooksStoreNavigator

/**
 * App navigator, which implements feature navigation
 * In case of increase features count should delegate navigation to their navigators
 */
class AppNavigator : NavControllerHolder, BooksStoreNavigator, BookDetailsNavigator, MyBooksNavigator {

    override var navController: NavController? = null

    override fun bookDetailsToReadBook(bookId: Long) {
        navController?.navigate(
            BookDetailsFragmentDirections.bookDetailsToReadBook()
                .setBookId(bookId)
        )
    }

    override fun booksStoreToMyBooksScreen() {
        // TODO: Tabs navcontroller?
//        navController.mActivity?.supportFragmentManager
//            ?.findFragmentById(R.id.fr_main_nav_host)
//            ?.childFragmentManager
//            ?.fragments
//            ?.firstOrNull()
//            ?.let { it as? TabsFragment }
//            ?.openMyBooksTab()
    }

    override fun myBooksToReadBook(bookId: Long) {
        navController?.navigate(
            TabsFragmentDirections.tabsToReadBook()
                .setBookId(bookId)
        )
    }

    override fun myBooksToBookDetails(bookId: Long) {
        navController?.navigate(
            TabsFragmentDirections.tabsToBookDetails()
                .setBookId(bookId)
        )
    }
}