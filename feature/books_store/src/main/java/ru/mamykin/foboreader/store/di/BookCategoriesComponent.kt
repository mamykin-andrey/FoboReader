package ru.mamykin.foboreader.store.di

import dagger.Component
import dagger.Module
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.NavigationApi
import ru.mamykin.foboreader.core.di.api.NetworkApi
import ru.mamykin.foboreader.store.presentation.BookCategoriesFragment

@Component(
    modules = [
        BookCategoriesModule::class,
    ],
    dependencies = [
        NetworkApi::class,
        CommonApi::class,
        NavigationApi::class,
    ]
)
internal interface BookCategoriesComponent {

    fun inject(fragment: BookCategoriesFragment)

    @Component.Factory
    interface Factory {

        fun create(
            commonApi: CommonApi,
            networkApi: NetworkApi,
            navigationApi: NavigationApi,
        ): BookCategoriesComponent
    }
}

@Module(includes = [BooksStoreApiServiceModule::class])
internal class BookCategoriesModule