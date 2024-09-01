package ru.mamykin.foboreader.store.common

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import ru.mamykin.foboreader.core.data.RetrofitServiceFactory
import ru.mamykin.foboreader.core.di.qualifier.CommonClient
import ru.mamykin.foboreader.store.common.BooksStoreService

@Module
internal class BooksStoreApiServiceModule {

    @Provides
    internal fun provideBooksStoreService(@CommonClient client: OkHttpClient): BooksStoreService =
        RetrofitServiceFactory.create(
            client,
            BooksStoreService.BASE_URL
        )
}