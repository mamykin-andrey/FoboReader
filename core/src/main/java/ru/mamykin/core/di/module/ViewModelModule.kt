package ru.mamykin.core.di.module

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import ru.mamykin.core.mvvm.ViewModelFactory
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Singleton
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

//
//    @Binds
//    @IntoMap
//    @ViewModelKey(BookDetailsViewModel::class)
//    abstract fun provideBookDetailsViewModel(viewMode: BookDetailsViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(BooksStoreViewModel::class)
//    abstract fun provideBooksStoreViewModel(viewModel: BooksStoreViewModel): ViewModel
}