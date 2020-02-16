package ru.mamykin.foboreader.my_books.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mamykin.foboreader.my_books.data.MyBooksRepository
import ru.mamykin.foboreader.my_books.domain.book_details.BookDetailsInteractor
import ru.mamykin.foboreader.my_books.domain.my_books.BookFilesScanner
import ru.mamykin.foboreader.my_books.domain.my_books.BookParser
import ru.mamykin.foboreader.my_books.domain.my_books.MyBooksInteractor
import ru.mamykin.foboreader.my_books.presentation.book_details.BookDetailsViewModel
import ru.mamykin.foboreader.my_books.presentation.my_books.MyBooksViewModel

val myBooksModule = module {
    single { DatabaseDependencies.dao(get()) }
    factory { BookParser() }
    factory { BookFilesScanner(androidContext(), get(), get()) }
    factory { MyBooksRepository(get()) }

    factory { MyBooksInteractor(get(), get()) }
    viewModel { MyBooksViewModel(get(), get()) }

    factory { BookDetailsInteractor(get()) }
    viewModel { BookDetailsViewModel(get(), get()) }
}