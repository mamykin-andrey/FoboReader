package ru.mamykin.foboreader.core.di.modules

import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.domain.entity.mapper.FolderToFilesListMapper
import ru.mamykin.foboreader.core.di.scope.FragmentScope
import ru.mamykin.foboreader.presentation.dropboxbooks.DropboxBooksRouter

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