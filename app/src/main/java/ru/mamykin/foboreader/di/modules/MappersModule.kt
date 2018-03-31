package ru.mamykin.foboreader.di.modules

import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.data.mapper.FolderToFilesListMapper
import javax.inject.Singleton

@Module
@Singleton
class MappersModule {

    @Provides
    @Singleton
    internal fun provideFolderToFilesListMapper(): FolderToFilesListMapper {
        return FolderToFilesListMapper()
    }
}