package ru.mamykin.my_books.di

import dagger.Component
import ru.mamykin.core.di.component.AppComponent
import ru.mamykin.my_books.presentation.MyBooksFragment

@MyBooksScope
@Component(dependencies = [AppComponent::class], modules = [MyBooksModule::class])
interface MyBooksComponent {
    fun inject(fragment: MyBooksFragment)
}