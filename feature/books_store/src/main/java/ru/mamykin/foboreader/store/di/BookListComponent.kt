package ru.mamykin.foboreader.store.di

import dagger.BindsInstance
import dagger.Component
import dagger.Module
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.NavigationApi
import ru.mamykin.foboreader.core.di.api.NetworkApi
import ru.mamykin.foboreader.core.di.api.SettingsApi
import ru.mamykin.foboreader.store.presentation.BooksListFeature
import ru.mamykin.foboreader.store.presentation.BooksListFragment
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
internal annotation class BookListScope

@BookListScope
@Component(
    modules = [
        BooksStoreModule::class,
    ],
    dependencies = [
        NetworkApi::class,
        CommonApi::class,
        NavigationApi::class,
        SettingsApi::class,
    ]
)
internal interface BookListComponent {

    fun inject(fragment: BooksListFragment)

    @Component.Factory
    interface Factory {

        fun create(
            commonApi: CommonApi,
            networkApi: NetworkApi,
            navigationApi: NavigationApi,
            settingsApi: SettingsApi,
            @BindsInstance params: BooksListFeature.BookCategoriesParams,
        ): BookListComponent
    }
}

@Module(includes = [BooksStoreApiServiceModule::class])
internal class BooksStoreModule