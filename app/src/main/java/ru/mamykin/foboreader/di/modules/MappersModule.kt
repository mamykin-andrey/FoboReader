package ru.mamykin.foboreader.di.modules

import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.common.FolderToFilesListMapper

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@Module
class MappersModule {
    @Provides
    internal fun provideFolderToFilesListMapper(): FolderToFilesListMapper {
        return FolderToFilesListMapper()
    }
}