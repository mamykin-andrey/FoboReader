package ru.mamykin.foboreader.di.component

import dagger.Subcomponent
import ru.mamykin.foboreader.di.modules.DropboxBooksModule
import ru.mamykin.foboreader.di.scope.FragmentScope
import ru.mamykin.foboreader.ui.dropbox.DropboxBooksFragment

@FragmentScope
@Subcomponent(modules = [DropboxBooksModule::class])
interface DropboxBooksComponent {

    fun inject(fragment: DropboxBooksFragment)
}