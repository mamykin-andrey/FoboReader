package ru.mamykin.foboreader.store.common

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import ru.mamykin.foboreader.core.data.RetrofitServiceFactory

@Module
@InstallIn(ViewModelComponent::class)
internal object BooksStoreApiServiceModule {

    @Provides
    fun provideBooksStoreService(client: OkHttpClient): BooksStoreService = RetrofitServiceFactory.create(
        client,
        BooksStoreService.BASE_URL
    )
}