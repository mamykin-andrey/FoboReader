package ru.mamykin.read_book.di

import dagger.Component
import ru.mamykin.core.di.component.AppComponent
import ru.mamykin.read_book.presentation.ReadBookActivity

@ReadBookScope
@Component(dependencies = [AppComponent::class], modules = [ReadBookModule::class])
interface ReadBookComponent {
    fun inject(activity: ReadBookActivity)
}