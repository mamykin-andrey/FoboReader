package ru.mamykin.foboreader.read_book.di

import dagger.BindsInstance
import dagger.Component
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.NavigationApi
import ru.mamykin.foboreader.core.di.api.NetworkApi
import ru.mamykin.foboreader.core.di.api.SettingsApi
import ru.mamykin.foboreader.read_book.presentation.ReadBookFragment
import javax.inject.Named
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ReadBookScope

@ReadBookScope
@Component(
    dependencies = [NetworkApi::class, NavigationApi::class, CommonApi::class, SettingsApi::class],
    modules = [ReadBookModule::class]
)
interface ReadBookComponent {

    fun inject(fragment: ReadBookFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance @Named("bookId") bookId: Long,
            networkApi: NetworkApi,
            navigationApi: NavigationApi,
            commonApi: CommonApi,
            settingsApi: SettingsApi
        ): ReadBookComponent
    }
}