package ru.mamykin.foboreader.book_details.di

import dagger.BindsInstance
import dagger.Subcomponent
import ru.mamykin.foboreader.book_details.presentation.BookDetailsFragment
import javax.inject.Named

@Subcomponent
interface BookDetailsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance @Named("bookId") bookId: Long): BookDetailsComponent
    }

    fun inject(fragment: BookDetailsFragment)
}