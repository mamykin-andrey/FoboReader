package ru.mamykin.foboreader.store.common

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import ru.mamykin.foboreader.core.data.RetrofitServiceFactory
import ru.mamykin.foboreader.store.list.FileRepository
import ru.mamykin.foboreader.store.list.FileRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
internal interface BooksStoreBindsModule {

    @Binds
    fun bindFileRepository(impl: FileRepositoryImpl): FileRepository
}

@Module
@InstallIn(ViewModelComponent::class)
internal class BooksStoreProvidesModule {

    @Provides
    fun provideBooksStoreService(client: OkHttpClient): BooksStoreService = RetrofitServiceFactory.create(
        client,
        BooksStoreService.BASE_URL
    )
}