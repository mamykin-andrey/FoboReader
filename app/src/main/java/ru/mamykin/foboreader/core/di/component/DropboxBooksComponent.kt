package ru.mamykin.foboreader.core.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.core.di.modules.DropboxBooksModule
import ru.mamykin.foboreader.core.di.scope.FragmentScope
import ru.mamykin.foboreader.presentation.dropboxbooks.DropboxBooksPresenter
import ru.mamykin.foboreader.presentation.dropboxbooks.DropboxBooksFragment

@FragmentScope
@Subcomponent(modules = [DropboxBooksModule::class])
interface DropboxBooksComponent {

    fun getDropboxBooksPresenter(): DropboxBooksPresenter

    fun inject(fragment: DropboxBooksFragment)
}