package ru.mamykin.foboreader.settings.app_language

import dagger.Component
import ru.mamykin.foboreader.core.di.api.SettingsApi
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ChangeLanguageScope

@ChangeLanguageScope
@Component(
    dependencies = [SettingsApi::class],
    modules = []
)
internal interface ChangeLanguageComponent {

    fun viewModel(): ChangeLanguageViewModel

    @Component.Factory
    interface Factory {

        fun create(settingsApi: SettingsApi): ChangeLanguageComponent
    }
}