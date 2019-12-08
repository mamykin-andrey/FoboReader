package ru.mamykin.store.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mamykin.store.data.BooksStoreRepository
import ru.mamykin.store.data.BooksStoreService
import ru.mamykin.store.domain.BooksStoreInteractor
import ru.mamykin.store.presentation.BooksStoreViewModel

val booksStoreModule = module {
    factory { BooksStoreService.create() }
    factory { BooksStoreRepository(get()) }
    factory { BooksStoreInteractor(get()) }
    viewModel { BooksStoreViewModel(get()) }
}