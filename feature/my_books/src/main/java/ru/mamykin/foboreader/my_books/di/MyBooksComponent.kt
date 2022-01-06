package ru.mamykin.foboreader.my_books.di

import dagger.Component
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.NavigationApi
import ru.mamykin.foboreader.my_books.presentation.MyBooksFragment
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