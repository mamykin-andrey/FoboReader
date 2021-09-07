package ru.mamykin.foboreader.settings.di

import dagger.Component
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.SettingsApi
import ru.mamykin.foboreader.settings.presentation.ColorPickerDialogFragment
import ru.mamykin.foboreader.settings.presentation.SelectAppLanguageDialog
import ru.mamykin.foboreader.settings.presentation.SettingsFragment
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class SettingsScope

// TODO: STORE IT SOMEWHERE
@SettingsScope
@Component(dependencies = [CommonApi::class, SettingsApi::class])
interface SettingsComponent {

    fun inject(fragment: SettingsFragment)

    fun inject(fragment: ColorPickerDialogFragment)

    fun inject(fragment: SelectAppLanguageDialog)

    @Component.Factory
    interface Factory {

        fun create(
            commonApi: CommonApi,
            settingsApi: SettingsApi
        ): SettingsComponent
    }
}