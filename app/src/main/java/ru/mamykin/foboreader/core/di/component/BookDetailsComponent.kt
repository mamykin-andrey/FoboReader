package ru.mamykin.foboreader.core.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.core.di.modules.BookDetailsModule
import ru.mamykin.foboreader.core.di.scope.ActivityScope
import ru.mamykin.foboreader.presentation.bookdetails.BookDetailsPresenter

@ActivityScope
@Subcomponent(modules = [BookDetailsModule::class])
interface BookDetailsComponent {

    fun getBookDetailsPresenter(): BookDetailsPresenter
}