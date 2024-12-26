package ru.mamykin.foboreader.store.list

import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.NetworkApi
import ru.mamykin.foboreader.core.di.api.SettingsApi
import ru.mamykin.foboreader.store.common.BooksStoreApiServiceModule
import javax.inject.Named
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
internal annotation class BookListScope

@BookListScope
@Component(
    modules = [BooksStoreModule::class],
    dependencies = [
        NetworkApi::class,
        CommonApi::class,
        SettingsApi::class,
    ]
)
internal interface BookListComponent {

    fun viewModel(): BooksStoreListViewModel

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance
            @Named("categoryId")
            categoryId: String,
            commonApi: CommonApi,
            networkApi: NetworkApi,
            settingsApi: SettingsApi,
        ): BookListComponent
    }
}

@Module(includes = [BooksStoreApiServiceModule::class])
internal interface BooksStoreModule {

    @Binds
    @BookListScope
    fun bindFileRepository(impl: FileRepositoryImpl): FileRepository
}