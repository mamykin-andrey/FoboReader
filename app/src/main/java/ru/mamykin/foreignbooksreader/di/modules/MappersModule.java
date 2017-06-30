package ru.mamykin.foreignbooksreader.di.modules;

import dagger.Module;
import dagger.Provides;
import ru.mamykin.foreignbooksreader.common.FileToAndroidFileMapper;
import ru.mamykin.foreignbooksreader.common.FolderToFilesListMapper;

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@Module
public class MappersModule {
    @Provides
    FolderToFilesListMapper provideFolderToFilesListMapper() {
        return new FolderToFilesListMapper();
    }

    @Provides
    FileToAndroidFileMapper provideFileToAndroidFileMapper() {
        return new FileToAndroidFileMapper();
    }
}