package ru.mamykin.foboreader.core.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.mamykin.foboreader.core.di.qualifiers.ViewModelKey
import ru.mamykin.foboreader.core.mvvm.ViewModelFactory
import ru.mamykin.foboreader.presentation.bookdetails.BookDetailsViewModel
import ru.mamykin.foboreader.presentation.booksstore.BooksStoreViewModel
import ru.mamykin.foboreader.presentation.devicebooks.DeviceBooksViewModel
import ru.mamykin.foboreader.presentation.mybooks.MyBooksViewModel

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MyBooksViewModel::class)
    abstract fun provideMyBooksViewModel(viewModel: MyBooksViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BookDetailsViewModel::class)
    abstract fun provideBookDetailsViewModel(viewMode: BookDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BooksStoreViewModel::class)
    abstract fun provideBooksStoreViewModel(viewModel: BooksStoreViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DeviceBooksViewModel::class)
    abstract fun provideDeviceBooksViewModel(viewModel: DeviceBooksViewModel): ViewModel
}