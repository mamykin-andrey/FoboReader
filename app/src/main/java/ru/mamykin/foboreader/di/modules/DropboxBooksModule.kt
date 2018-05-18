package ru.mamykin.foboreader.di.modules

import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.entity.mapper.FolderToFilesListMapper
import ru.mamykin.foboreader.di.scope.FragmentScope
import ru.mamykin.foboreader.ui.dropbox.DropboxBooksRouter

@Module
@FragmentScope
class DropboxBooksModule(
        private val router: DropboxBooksRouter
) {
    @Provides
    @FragmentScope
    fun provideMapper() = FolderToFilesListMapper()

    @Provides
    @FragmentScope
    fun provideRouter() = router
}