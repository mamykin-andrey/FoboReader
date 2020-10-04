package ru.mamykin.foboreader.store.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mamykin.foboreader.store.data.network.FileDownloader
import ru.mamykin.foboreader.store.data.BooksStoreRepository
import ru.mamykin.foboreader.store.domain.interactor.BooksStoreInteractor
import ru.mamykin.foboreader.store.presentation.BooksStoreViewModel

val booksStoreModule = module {
    single { NetworkDependencies.client() }
    single { NetworkDependencies.service(get()) }
    factory { BooksStoreRepository(get()) }
    factory { FileDownloader(androidContext(), get()) }
    factory { BooksStoreInteractor(get(), get()) }
    viewModel { BooksStoreViewModel(get(), get()) }
}