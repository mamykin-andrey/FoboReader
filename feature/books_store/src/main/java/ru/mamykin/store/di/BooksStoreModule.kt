package ru.mamykin.store.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mamykin.core.network.FileDownloader
import ru.mamykin.store.data.BooksStoreRepository
import ru.mamykin.store.domain.BooksStoreInteractor
import ru.mamykin.store.presentation.BooksStoreViewModel

val booksStoreModule = module {
    single { NetworkDependencies.client() }
    single { NetworkDependencies.service(get()) }
    factory { BooksStoreRepository(get()) }
    factory { FileDownloader(androidContext(), get()) }
    factory { BooksStoreInteractor(get(), get()) }
    viewModel { BooksStoreViewModel(get(), get()) }
}