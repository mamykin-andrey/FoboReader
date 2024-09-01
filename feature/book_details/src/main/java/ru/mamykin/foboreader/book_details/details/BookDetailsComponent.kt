package ru.mamykin.foboreader.book_details.details

import dagger.BindsInstance
import dagger.Component
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.NavigationApi
import javax.inject.Named
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
internal annotation class BookDetailsScope

@BookDetailsScope
@Component(dependencies = [NavigationApi::class, CommonApi::class], modules = [DatabaseModule::class])
internal interface BookDetailsComponent {

    fun inject(fragment: BookDetailsFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance @Named("bookId") bookId: Long,
            navigationApi: NavigationApi,
            commonApi: CommonApi
        ): BookDetailsComponent
    }
}