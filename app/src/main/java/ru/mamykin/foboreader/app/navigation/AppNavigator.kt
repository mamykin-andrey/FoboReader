package ru.mamykin.foboreader.app.navigation

import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.app.presentation.TabsFragmentDirections
import ru.mamykin.foboreader.book_details.navigation.BookDetailsNavigator
import ru.mamykin.foboreader.book_details.presentation.BookDetailsFragmentDirections
import ru.mamykin.foboreader.core.platform.NavControllerHolder
import ru.mamykin.foboreader.my_books.navigation.MyBooksNavigator
import ru.mamykin.foboreader.store.navigation.BooksStoreNavigator

/**
 * App navigator, which implements feature navigation
 * In case of increase features count should delegate navigation to their navigators
 */
class AppNavigator : NavControllerHolder, BooksStoreNavigator, BookDetailsNavigator, MyBooksNavigator {

    override var navController: NavController? = null
    var tabsNavigationView: BottomNavigationView? = null

    override fun bookDetailsToReadBook(bookId: Long) {
        navController?.navigate(
            BookDetailsFragmentDirections.bookDetailsToReadBook()
                .setBookId(bookId)
        )
    }

    override fun booksStoreToMyBooksScreen() {
        tabsNavigationView?.selectedItemId = R.id.my_books
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