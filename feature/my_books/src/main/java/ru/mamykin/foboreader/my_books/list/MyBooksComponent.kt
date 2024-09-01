package ru.mamykin.foboreader.my_books.list

import dagger.Component
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.NavigationApi
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
internal annotation class MyBooksScope

@MyBooksScope
@Component(
    dependencies = [NavigationApi::class, CommonApi::class],
    modules = [DatabaseModule::class]
)
internal interface MyBooksComponent {

    fun inject(fragment: MyBooksFragment)

    @Component.Factory
    interface Factory {

        fun create(
            navigationApi: NavigationApi,
            commonApi: CommonApi
        ): MyBooksComponent
    }
}