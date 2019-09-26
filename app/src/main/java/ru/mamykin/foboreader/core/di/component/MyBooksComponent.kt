package ru.mamykin.foboreader.core.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.core.di.modules.MyBooksModule
import ru.mamykin.foboreader.core.di.scope.FragmentScope
import ru.mamykin.foboreader.presentation.mybooks.MyBooksViewModel

@FragmentScope
@Subcomponent(modules = [MyBooksModule::class])
interface MyBooksComponent {

    fun getMyBooksPresenter(): MyBooksViewModel
}