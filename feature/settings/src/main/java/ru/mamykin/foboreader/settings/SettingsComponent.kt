package ru.mamykin.foboreader.settings

import dagger.Component
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.NavigationApi
import ru.mamykin.foboreader.core.di.api.SettingsApi
import ru.mamykin.foboreader.core.di.module.CoroutinesModule
import ru.mamykin.foboreader.settings.all_settings.SettingsViewModel
import ru.mamykin.foboreader.settings.app_language.ChangeLanguageDialogFragment
import ru.mamykin.foboreader.settings.custom_color.ChooseCustomColorDialogFragment
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class SettingsScope

@SettingsScope
@Component(
    dependencies = [CommonApi::class, SettingsApi::class, NavigationApi::class],
    modules = [CoroutinesModule::class]
)
internal interface SettingsComponent {

    fun settingsViewModel(): SettingsViewModel

    fun inject(fragment: ChooseCustomColorDialogFragment)

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