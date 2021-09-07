package ru.mamykin.foboreader.store.di

import dagger.Component
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.NetworkApi
import ru.mamykin.foboreader.store.presentation.BooksStoreFragment

@Component(modules = [BooksStoreModule::class], dependencies = [NetworkApi::class, CommonApi::class])
interface BooksStoreComponent {

    fun inject(fragment: BooksStoreFragment)

    @Component.Factory
    interface Factory {

        fun create(commonApi: CommonApi, networkApi: NetworkApi): BooksStoreComponent
    }
}