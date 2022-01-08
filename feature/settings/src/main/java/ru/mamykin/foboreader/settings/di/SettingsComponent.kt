package ru.mamykin.foboreader.settings.di

import dagger.Component
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.NavigationApi
import ru.mamykin.foboreader.core.di.api.SettingsApi
import ru.mamykin.foboreader.settings.presentation.ChangeTranslationColorDialogFragment
import ru.mamykin.foboreader.settings.presentation.ChangeLanguageDialogFragment
import ru.mamykin.foboreader.settings.presentation.SettingsFragment
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class SettingsScope

@SettingsScope
@Component(dependencies = [CommonApi::class, SettingsApi::class, NavigationApi::class])
internal interface SettingsComponent {

    fun inject(fragment: SettingsFragment)

    fun inject(fragment: ChangeTranslationColorDialogFragment)

    fun inject(fragment: ChangeLanguageDialogFragment)

    @Component.Factory
    interface Factory {

        fun create(
            commonApi: CommonApi,
            settingsApi: SettingsApi,
            navigationApi: NavigationApi,
        ): SettingsComponent
    }
}