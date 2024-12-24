package ru.mamykin.foboreader.settings.all_settings

import dagger.Component
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.SettingsApi
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class SettingsScope

@SettingsScope
@Component(
    dependencies = [CommonApi::class, SettingsApi::class],
)
internal interface SettingsComponent {

    fun settingsViewModel(): SettingsViewModel

    @Component.Factory
    interface Factory {

        fun create(
            commonApi: CommonApi,
            settingsApi: SettingsApi,
        ): SettingsComponent
    }
}