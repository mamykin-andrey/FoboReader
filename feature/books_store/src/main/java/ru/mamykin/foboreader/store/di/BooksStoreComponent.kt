package ru.mamykin.foboreader.store.di

import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import ru.mamykin.foboreader.core.data.RetrofitServiceFactory
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.NetworkApi
import ru.mamykin.foboreader.core.di.qualifier.CommonClient
import ru.mamykin.foboreader.store.data.network.BooksStoreService
import ru.mamykin.foboreader.store.presentation.BooksListFragment

@Component(modules = [BooksStoreModule::class], dependencies = [NetworkApi::class, CommonApi::class])
interface BooksStoreComponent {

    fun inject(fragment: BooksListFragment)

    @Component.Factory
    interface Factory {

        fun create(commonApi: CommonApi, networkApi: NetworkApi): BooksStoreComponent
    }
}

@Module
internal class BooksStoreModule {

    @Provides
    fun provideBooksStoreService(@CommonClient client: OkHttpClient): BooksStoreService =
        RetrofitServiceFactory.create(
            client,
            BooksStoreService.BASE_URL
        )
}