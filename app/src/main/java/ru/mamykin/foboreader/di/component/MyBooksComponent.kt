package ru.mamykin.foboreader.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.di.modules.MyBooksModule
import ru.mamykin.foboreader.di.scope.FragmentScope
import ru.mamykin.foboreader.ui.mybooks.MyBooksFragment

@FragmentScope
@Subcomponent(modules = [MyBooksModule::class])
interface MyBooksComponent {

    fun inject(fragment: MyBooksFragment)
}