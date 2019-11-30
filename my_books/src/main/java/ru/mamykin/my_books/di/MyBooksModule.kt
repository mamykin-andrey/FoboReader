package ru.mamykin.my_books.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import ru.mamykin.core.di.module.KotlinViewModelProvider
import ru.mamykin.my_books.presentation.MyBooksViewModel

@Module
class MyBooksModule(
        private val fragment: Fragment
) {
    @MyBooksScope
    @Provides
    fun provideMyBookViewModel(viewModel: MyBooksViewModel): ViewModel {
        return KotlinViewModelProvider.of(fragment, viewModel)
    }
}