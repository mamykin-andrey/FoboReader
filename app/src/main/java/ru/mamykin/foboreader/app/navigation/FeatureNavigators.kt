package ru.mamykin.foboreader.app.navigation

import ru.mamykin.foboreader.book_details.navigation.BookDetailsNavigator
import ru.mamykin.foboreader.my_books.navigation.MyBooksNavigator
import ru.mamykin.foboreader.store.navigation.BooksStoreNavigator

internal val featureNavigators = arrayOf(
    BooksStoreNavigator::class,
    BookDetailsNavigator::class,
    MyBooksNavigator::class
)