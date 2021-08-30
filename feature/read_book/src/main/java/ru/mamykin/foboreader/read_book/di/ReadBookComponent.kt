package ru.mamykin.foboreader.read_book.di

import dagger.BindsInstance
import dagger.Subcomponent
import ru.mamykin.foboreader.read_book.presentation.ReadBookFragment
import javax.inject.Named

@Subcomponent(modules = [ReadBookModule::class])
interface ReadBookComponent {

    fun inject(fragment: ReadBookFragment)

    @Subcomponent.Factory
    interface Factory {

        fun create(@BindsInstance @Named("bookId") bookId: Long): ReadBookComponent
    }
}