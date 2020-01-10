package ru.mamykin.my_books.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mamykin.my_books.data.MyBooksRepository
import ru.mamykin.my_books.domain.my_books.BooksListInteractor
import ru.mamykin.my_books.presentation.my_books.MyBooksViewModel

val myBooksModule = module {
    single { DatabaseDependencies.dao(get()) }
    factory { MyBooksRepository(get()) }
    factory { BooksListInteractor(get()) }
    viewModel { MyBooksViewModel(get()) }
}