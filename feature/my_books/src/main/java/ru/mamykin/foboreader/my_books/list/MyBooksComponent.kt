package ru.mamykin.foboreader.my_books.list

import dagger.Component
import ru.mamykin.foboreader.core.di.api.CommonApi
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
internal annotation class MyBooksScope

@MyBooksScope
@Component(
    dependencies = [CommonApi::class],
    modules = [DatabaseModule::class]
)
internal interface MyBooksComponent {

    fun myBooksViewModel(): MyBooksViewModel

    @Component.Factory
    interface Factory {

        fun create(
            commonApi: CommonApi
        ): MyBooksComponent
    }
}