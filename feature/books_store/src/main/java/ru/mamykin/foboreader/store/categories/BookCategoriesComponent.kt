package ru.mamykin.foboreader.store.categories

import dagger.Component
import dagger.Module
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.NavigationApi
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
    modules = [
        BookCategoriesModule::class,
    ],
    dependencies = [
        NetworkApi::class,
        CommonApi::class,
        NavigationApi::class,
        SettingsApi::class,
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
            settingsApi: SettingsApi,
        ): BookCategoriesComponent
    }
}

@Module(includes = [BooksStoreApiServiceModule::class])
internal class BookCategoriesModule