package ru.mamykin.foboreader.app.presentation

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.KoinComponent
import org.koin.core.inject
import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.extension.nightMode

class NightThemeDelegate(
    private val activity: AppCompatActivity
) : KoinComponent {

    private val appSettingsStorage: AppSettingsStorage by inject()

    fun init() {
        appSettingsStorage.nightThemeField.flow
            .onEach { activity.nightMode = it }
            .launchIn(activity.lifecycleScope)
    }
}