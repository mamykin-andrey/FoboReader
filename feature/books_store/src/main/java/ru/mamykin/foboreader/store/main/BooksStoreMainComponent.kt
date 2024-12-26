package ru.mamykin.foboreader.store.main

import dagger.Component
import dagger.Module
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.NetworkApi
import ru.mamykin.foboreader.core.di.api.SettingsApi
import ru.mamykin.foboreader.store.common.BooksStoreApiServiceModule
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
internal annotation class BookCategoriesScope

@BookCategoriesScope
@Component(
    modules = [BookCategoriesModule::class],
    dependencies = [
        NetworkApi::class,
        CommonApi::class,
        SettingsApi::class,
    ]
)
internal interface BooksStoreMainComponent {

    fun booksStoreMainViewModel(): BooksStoreMainViewModel

    @Component.Factory
    interface Factory {

        fun create(
            commonApi: CommonApi,
            networkApi: NetworkApi,
            settingsApi: SettingsApi,
        ): BooksStoreMainComponent
    }
}

@Module(includes = [BooksStoreApiServiceModule::class])
internal class BookCategoriesModule