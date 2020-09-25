package ru.mamykin.foboreader.my_books.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mamykin.foboreader.my_books.domain.BookFilesScanner
import ru.mamykin.foboreader.my_books.domain.BookInfoParser
import ru.mamykin.foboreader.my_books.domain.MyBooksInteractor
import ru.mamykin.foboreader.my_books.presentation.MyBooksViewModel

val myBooksModule = module {
    factory { BookInfoParser() }
    factory { BookFilesScanner(androidContext(), get(), get()) }

    factory { MyBooksInteractor(get(), get()) }
    viewModel { MyBooksViewModel(get()) }
}