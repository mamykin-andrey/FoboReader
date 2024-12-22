package ru.mamykin.foboreader.settings.custom_color

import dagger.BindsInstance
import dagger.Component
import javax.inject.Named
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ChooseCustomColorScope

@ChooseCustomColorScope
@Component(dependencies = [])
internal interface ChooseCustomColorComponent {

    fun chooseCustomColorViewModel(): ChooseCustomColorViewModel

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance @Named("title") screenTitle: String,
        ): ChooseCustomColorComponent
    }
}