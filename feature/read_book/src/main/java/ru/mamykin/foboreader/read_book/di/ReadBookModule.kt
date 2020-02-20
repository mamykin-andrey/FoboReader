package ru.mamykin.foboreader.read_book.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.mamykin.foboreader.read_book.data.ReadBookRepository
import ru.mamykin.foboreader.read_book.data.TranslateRepository
import ru.mamykin.foboreader.read_book.domain.ReadBookInteractor
import ru.mamykin.foboreader.read_book.domain.TextToSpeechService
import ru.mamykin.foboreader.read_book.presentation.ReadBookViewModel

val readBookModule = module {
    single { NetworkDependencies.service() }
    factory { ReadBookRepository() }
    factory { TranslateRepository(get()) }
    factory { TextToSpeechService(androidContext()) }
    factory { ReadBookInteractor(get(), get(), get()) }
    viewModel { ReadBookViewModel(get()) }
}