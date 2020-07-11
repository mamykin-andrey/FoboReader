package ru.mamykin.foboreader.my_books.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mamykin.foboreader.core.data.repository.BookInfoRepository
import ru.mamykin.foboreader.my_books.domain.book_details.BookDetailsInteractor
import ru.mamykin.foboreader.my_books.domain.my_books.BookFilesScanner
import ru.mamykin.foboreader.my_books.domain.my_books.BookInfoParser
import ru.mamykin.foboreader.my_books.domain.my_books.MyBooksInteractor
import ru.mamykin.foboreader.my_books.presentation.book_details.BookDetailsViewModel
import ru.mamykin.foboreader.my_books.presentation.my_books.MyBooksViewModel

val myBooksModule = module {
    single { DatabaseDependencies.dao(get()) }
    factory { BookInfoParser() }
    factory { BookFilesScanner(androidContext(), get(), get()) }
    factory { BookInfoRepository(get()) }

    factory { MyBooksInteractor(get(), get()) }
    viewModel { MyBooksViewModel(get()) }

    factory { BookDetailsInteractor(get()) }
    viewModel { (bookId: Long) -> BookDetailsViewModel(bookId, get(), get()) }
}