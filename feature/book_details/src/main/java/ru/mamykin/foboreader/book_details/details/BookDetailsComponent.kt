package ru.mamykin.foboreader.book_details.details

import dagger.BindsInstance
import dagger.Component
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.module.CoroutinesModule
import javax.inject.Named
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
internal annotation class BookDetailsScope

@BookDetailsScope
@Component(
    dependencies = [CommonApi::class],
    modules = [DatabaseModule::class, CoroutinesModule::class]
)
internal interface BookDetailsComponent {

    fun viewModel(): BookDetailsViewModel

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance @Named("bookId") bookId: Long,
            commonApi: CommonApi
        ): BookDetailsComponent
    }
}