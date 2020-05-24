package ru.mamykin.foboreader.settings.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.mamykin.foboreader.core.mvvm.BaseViewModel2
import ru.mamykin.foboreader.settings.domain.SettingsInteractor

@FlowPreview
@ExperimentalCoroutinesApi
class SettingsViewModel(
    private val interactor: SettingsInteractor
) : BaseViewModel2<ViewState, SettingsAction, SettingsEvent>(
    ViewState()
) {
    override suspend fun onLoadData() {
        initSettingsFlow()
        interactor.loadData()
    }

    private fun initSettingsFlow() {
        interactor.settingsFlow
            .onEach { sendAction(SettingsAction.SettingsLoaded(it)) }
            .launchIn(viewModelScope)
    }

    override fun onAction(action: SettingsAction): ViewState = when (action) {
        is SettingsAction.SettingsLoaded -> state.copy(
            isNightTheme = action.settings.isNightTheme,
            isAutoBrightness = action.settings.isAutoBrightness,
            brightness = action.settings.brightness,
            contentTextSize = action.settings.contentTextSize
        )
    }

    override suspend fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.BrightnessChanged -> {
                interactor.changeBrightness(event.brightness)
            }
            is SettingsEvent.NightThemeChanged -> {
                interactor.enableNightTheme(event.nightTheme)
            }
            is SettingsEvent.AutoBrightnessChanged -> {
                interactor.enableAutoBrightness(event.autoBrightness)
            }
            is SettingsEvent.IncreaseTextSizeClicked -> {
                interactor.increaseTextSize()
            }
            is SettingsEvent.DecreaseTextSizeClicked -> {
                interactor.decreaseTextSize()
            }
        }
    }
}