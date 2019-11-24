package ru.mamykin.foboreader.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.mamykin.book_details.presentation.BookDetailsViewModel
import ru.mamykin.core.di.qualifiers.ViewModelKey
import ru.mamykin.core.mvvm.ViewModelFactory
import ru.mamykin.my_books.presentation.MyBooksViewModel
import ru.mamykin.store.presentation.BooksStoreViewModel

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
}