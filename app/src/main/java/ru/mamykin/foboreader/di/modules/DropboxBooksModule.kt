package ru.mamykin.foboreader.di.modules

import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.data.mapper.FolderToFilesListMapper
import ru.mamykin.foboreader.di.scope.FragmentScope

@FragmentScope
@Module
class DropboxBooksModule {

    @Provides
    @FragmentScope
    fun provideMapper(): FolderToFilesListMapper {
        return FolderToFilesListMapper()
    }
}