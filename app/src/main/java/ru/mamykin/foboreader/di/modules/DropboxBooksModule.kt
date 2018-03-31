package ru.mamykin.foboreader.di.modules

import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.data.mapper.FolderToFilesListMapper
import ru.mamykin.foboreader.di.scope.FragmentScope
import ru.mamykin.foboreader.ui.dropbox.DropboxBooksRouter

@FragmentScope
@Module
class DropboxBooksModule(
        private val router: DropboxBooksRouter
) {

    @Provides
    @FragmentScope
    fun provideMapper(): FolderToFilesListMapper {
        return FolderToFilesListMapper()
    }

    @Provides
    @FragmentScope
    fun provideRouter() = router
}