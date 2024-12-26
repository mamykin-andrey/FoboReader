package ru.mamykin.foboreader.store.list

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal interface BookListModule {

    @Binds
    fun bindFileRepository(impl: FileRepositoryImpl): FileRepository
}