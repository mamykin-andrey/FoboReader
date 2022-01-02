package ru.mamykin.foboreader.store.di

import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import ru.mamykin.foboreader.core.data.RetrofitServiceFactory
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.NavigationApi
import ru.mamykin.foboreader.core.di.api.NetworkApi
import ru.mamykin.foboreader.core.di.qualifier.CommonClient
import ru.mamykin.foboreader.store.data.network.BooksStoreService
import ru.mamykin.foboreader.store.presentation.BookCategoriesFragment
import ru.mamykin.foboreader.store.presentation.BooksListFragment

@Component(
    modules = [
        BooksStoreModule::class,
    ],
    dependencies = [
        NetworkApi::class,
        CommonApi::class,
        NavigationApi::class,
    ]
)
internal interface BooksStoreComponent {

    fun inject(fragment: BooksListFragment)

    fun inject(fragment: BookCategoriesFragment)

    @Component.Factory
    interface Factory {

        fun create(
            commonApi: CommonApi,
            networkApi: NetworkApi,
            navigationApi: NavigationApi,
        ): BooksStoreComponent
    }
}

@Module
internal class BooksStoreModule {

    @Provides
    internal fun provideBooksStoreService(@CommonClient client: OkHttpClient): BooksStoreService =
        RetrofitServiceFactory.create(
            client,
            BooksStoreService.BASE_URL
        )
}